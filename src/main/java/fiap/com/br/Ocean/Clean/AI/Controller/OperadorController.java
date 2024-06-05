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

import fiap.com.br.Ocean.Clean.AI.Models.Operador;
import fiap.com.br.Ocean.Clean.AI.Models.OperadorModel;
import fiap.com.br.Ocean.Clean.AI.Models.OperadorModelAssembler;
import fiap.com.br.Ocean.Clean.AI.Repository.OperadorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("operadores")
@CacheConfig(cacheNames = "operadores")
@Tag(name = "operadores")
public class OperadorController {

    @Autowired
    private OperadorRepository operadorRepository;

    @Autowired
    private OperadorModelAssembler assembler;

    @GetMapping("{id_operador}")
    @Operation(
        summary = "Buscar um operador pelo ID.",
        description = "Retorna os detalhes do operador através do `id` informado como parâmetro de path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Os dados do operador foram retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe operador com o `id` informado.", useReturnTypeSchema = false)
        }
    )
    public EntityModel<OperadorModel> one(@PathVariable Long id_operador) {
        Operador operador = operadorRepository.findById(id_operador)
                .orElseThrow(() -> new ResourceNotFoundException("Operador not found"));
        return EntityModel.of(assembler.toModel(operador),
                WebMvcLinkBuilder.linkTo(methodOn(OperadorController.class).one(id_operador)).withSelfRel());
    }


    @GetMapping
    @Operation(
        summary = "Listar todos os operadores.",
        description = "Retorna um array com todos os operadores no formato do objeto."
    )
    public CollectionModel<OperadorModel> all() {
        List<Operador> operadores = operadorRepository.findAll();
        List<OperadorModel> operadorModels = operadores.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(operadorModels,
                WebMvcLinkBuilder.linkTo(methodOn(OperadorController.class).all()).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Registrar um novo operador.",
        description = "Cria um novo operador com os dados enviados no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Operador registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição.")
        }
    )
    public EntityModel<OperadorModel> newOperador(@RequestBody Operador newOperador) {
        Operador savedOperador = operadorRepository.save(newOperador);
        return EntityModel.of(assembler.toModel(savedOperador),
                WebMvcLinkBuilder.linkTo(methodOn(OperadorController.class).one(savedOperador.getId_operador())).withSelfRel());
    }

    @DeleteMapping("{id_operador}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Deletar um operador pelo ID.",
        description = "Deleta todos os dados de um operador através do ID especificado no parâmetro path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "Operador apagado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe operador com o `id` informado.")
        }
    )
    public void deleteOperador(@PathVariable Long id_operador) {
        operadorRepository.deleteById(id_operador);
    }

        @PutMapping("{id_operador}")
        @Operation(
            summary = "Atualizar os dados de um operador pelo ID.",
            description = "Altera os dados do operador especificado no `id`, utilizando as informações enviadas no corpo da requisição."
        )
        @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Operador alterado com sucesso."),
                @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição."),
                @ApiResponse(responseCode = "404", description = "Não existe operador com o `id` informado.")
            }
        )
        public EntityModel<OperadorModel> replaceOperador(@RequestBody Operador newOperador, @PathVariable Long id_operador) {
            Operador updatedOperador = operadorRepository.findById(id_operador)
                    .map(operador -> {
                        operador.setNome(newOperador.getNome());
                        operador.setEmail(newOperador.getEmail());
                        operador.setMissoes(newOperador.getMissoes());
                        operador.setDrones(newOperador.getDrones());
                        return operadorRepository.save(operador);
                    })
                    .orElseGet(() -> {
                        newOperador.setId_operador(id_operador);
                        return operadorRepository.save(newOperador);
                    });
    
            return EntityModel.of(assembler.toModel(updatedOperador),
                    WebMvcLinkBuilder.linkTo(methodOn(OperadorController.class).one(updatedOperador.getId_operador())).withSelfRel());
        }
}
