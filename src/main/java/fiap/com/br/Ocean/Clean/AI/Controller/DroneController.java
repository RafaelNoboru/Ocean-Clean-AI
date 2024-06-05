package fiap.com.br.Ocean.Clean.AI.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fiap.com.br.Ocean.Clean.AI.Models.Drone;
import fiap.com.br.Ocean.Clean.AI.Models.DroneModel;
import fiap.com.br.Ocean.Clean.AI.Models.DroneModelAssembler;
import fiap.com.br.Ocean.Clean.AI.Repository.DroneRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("drones")
@CacheConfig(cacheNames = "drones")
@Tag(name = "drones")
public class DroneController {

    @Autowired
   
    private DroneRepository droneRepository;
    
    @Autowired
    private DroneModelAssembler assembler;
    
    @GetMapping("{id_drone}")
    @Operation(
        summary = "Buscar um drone pelo ID.",
        description = "Retorna os detalhes do drone através do `id` informado como parâmetro de path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Os dados do drone foram retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe drone com o `id` informado.", useReturnTypeSchema = false)
        }
    )
    public EntityModel<DroneModel> one(@PathVariable Long id_drone) {
            Drone drone = droneRepository.findById(id_drone)
                    .orElseThrow(() -> new ResourceNotFoundException("Drone not found"));
            return EntityModel.of(assembler.toModel(drone));
    }
        
    @GetMapping
    @Operation(
        summary = "Listar todos os drones.",
        description = "Retorna um array com todos os drones no formato do objeto."
    )
    public CollectionModel<DroneModel> all() {
        List<Drone> drones = droneRepository.findAll();
        List<DroneModel> droneModels = drones.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(droneModels);
    }
    
    @GetMapping("ultimos")
    @Operation(
        summary = "Listar os últimos 10 drones cadastrados.",
        description = "Retorna um array com todos os últimos 10 drones cadastrados no formato do objeto."
    )
    public List<Drone> getLast10() {
        return droneRepository.findLast10();
    }
    @GetMapping("ordemalfabetica")
    @Operation(
        summary = "Listar todos os drones cadastrados em ordem alfabética.",
        description = "Retorna um array com todos os drones cadastrados em ordem alfabética e no formato do objeto."
    )
    public List<Drone> getOrderedByName() {
        return droneRepository.findAllOrderedByName();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Registrar um novo drone.",
        description = "Cria um novo drone com os dados enviados no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Drone registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição.")
        }
    )
    public EntityModel<DroneModel> newDrone(@RequestBody Drone newDrone) {
        Drone savedDrone = droneRepository.save(newDrone);
        return EntityModel.of(assembler.toModel(savedDrone),
                WebMvcLinkBuilder.linkTo(methodOn(DroneController.class).one(savedDrone.getId_drone())).withSelfRel());
    }

    @DeleteMapping("{id_drone}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Deletar um drone pelo ID.",
        description = "Deleta todos os dados de um drone através do ID especificado no parâmetro path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "Drone apagado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe drone com o `id` informado.")
        }
    )
    public void deleteDrone(@PathVariable Long id_drone) {
        droneRepository.deleteById(id_drone);
    }

    @PutMapping("{id_drone}")
    @Operation(
        summary = "Atualizar os dados de um drone pelo ID.",
        description = "Altera os dados do drone especificado no `id`, utilizando as informações enviadas no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Drone alterado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição."),
            @ApiResponse(responseCode = "404", description = "Não existe drone com o `id` informado.")
        }
    )
    public EntityModel<DroneModel> replaceDrone(@RequestBody Drone newDrone, @PathVariable Long id_drone) {
        Drone updatedDrone = droneRepository.findById(id_drone)
                .map(drone -> {
                    drone.setNome(newDrone.getNome());
                    drone.setCoordenadaX(newDrone.getCoordenadaX());
                    drone.setCoordenadaY(newDrone.getCoordenadaY());
                    drone.setResiduosColetados(newDrone.getResiduosColetados());
                    drone.setNivelBateria(newDrone.getNivelBateria());
                    drone.setOperador(newDrone.getOperador());
                    return droneRepository.save(drone);
                })
                .orElseGet(() -> {
                    newDrone.setId_drone(id_drone);
                    return droneRepository.save(newDrone);
                });

        return EntityModel.of(assembler.toModel(updatedDrone),
                WebMvcLinkBuilder.linkTo(methodOn(DroneController.class).one(updatedDrone.getId_drone())).withSelfRel());
    }
}