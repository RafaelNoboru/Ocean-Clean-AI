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

import fiap.com.br.Ocean.Clean.AI.Models.ResiduoPlastico;
import fiap.com.br.Ocean.Clean.AI.Models.ResiduoPlasticoModel;
import fiap.com.br.Ocean.Clean.AI.Models.ResiduoPlasticoModelAssembler;
import fiap.com.br.Ocean.Clean.AI.Repository.ResiduoPlasticoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("residuos")
@CacheConfig(cacheNames = "residuosPlasticos")
@Tag(name = "residuosPlasticos")
public class ResiduoController {

    @Autowired
    private ResiduoPlasticoRepository residuoPlasticoRepository;

    @Autowired
    private ResiduoPlasticoModelAssembler assembler;
    

    @GetMapping("{id_residuo}")
    @Operation(
        summary = "Buscar um resíduo pelo ID.",
        description = "Retorna os detalhes do resíduo através do `id` informado como parâmetro de path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Os dados do resíduo foram retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe resíduo com o `id` informado.", useReturnTypeSchema = false)
        }
    )
     public EntityModel<ResiduoPlasticoModel> one(@PathVariable Long id_residuo) {
            ResiduoPlastico residuoPlastico = residuoPlasticoRepository.findById(id_residuo)
                    .orElseThrow(() -> new ResourceNotFoundException("Residuo not found"));
            return EntityModel.of(assembler.toModel(residuoPlastico));
    }
    @GetMapping
    @Operation(
        summary = "Listar todos os resíduos plásticos.",
        description = "Retorna um array com todos os resíduos plásticos no formato do objeto."
    )
    public CollectionModel<ResiduoPlasticoModel> all() {
        List<ResiduoPlastico> residuosPlasticos = residuoPlasticoRepository.findAll();
        List<ResiduoPlasticoModel> residuosPlasticosModels = residuosPlasticos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(residuosPlasticosModels);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Registrar um novo resíduo plástico.",
        description = "Cria um novo resíduo plástico com os dados enviados no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Resíduo plástico registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição.")
        }
    )
    public EntityModel<ResiduoPlasticoModel> newResiduo(@RequestBody ResiduoPlastico newResiduo) {
        ResiduoPlastico savedResiduo = residuoPlasticoRepository.save(newResiduo);
        return EntityModel.of(assembler.toModel(savedResiduo),
                WebMvcLinkBuilder.linkTo(methodOn(ResiduoController.class).one(savedResiduo.getId_residuo())).withSelfRel());
    }

    @DeleteMapping("{id_residuo}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Deletar um resíduo plástico pelo ID.",
        description = "Deleta todos os dados de um resíduo plástico através do ID especificado no parâmetro path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "Resíduo plástico apagado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe resíduo plástico com o `id` informado.")
        }
    )
    public void deleteResiduo(@PathVariable Long id_residuo) {
        residuoPlasticoRepository.deleteById(id_residuo);
    }

    @PutMapping("{id_residuo}")
    @Operation(
        summary = "Atualizar os dados de um resíduo plástico pelo ID.",
        description = "Altera os dados do resíduo plástico especificado no `id`, utilizando as informações enviadas no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Resíduo plástico alterado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição."),
            @ApiResponse(responseCode = "404", description = "Não existe resíduo plástico com o `id` informado.")
        }
    )
    public EntityModel<ResiduoPlasticoModel> replaceResiduo(@RequestBody ResiduoPlastico newResiduo, @PathVariable Long id_residuo) {
        ResiduoPlastico updatedResiduo = residuoPlasticoRepository.findById(id_residuo)
                .map(residuo -> {
                    residuo.setCoordenadaX(newResiduo.getCoordenadaX());
                    residuo.setCoordenadaY(newResiduo.getCoordenadaY());
                    residuo.setMissao(newResiduo.getMissao());
                    return residuoPlasticoRepository.save(residuo);
                })
                .orElseGet(() -> {
                    newResiduo.setId_residuo(id_residuo);
                    return residuoPlasticoRepository.save(newResiduo);
                });

        return EntityModel.of(assembler.toModel(updatedResiduo),
                WebMvcLinkBuilder.linkTo(methodOn(ResiduoController.class).one(updatedResiduo.getId_residuo())).withSelfRel());
    }
}
