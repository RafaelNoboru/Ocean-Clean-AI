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

import fiap.com.br.Ocean.Clean.AI.Models.Missao;
import fiap.com.br.Ocean.Clean.AI.Models.MissaoModel;
import fiap.com.br.Ocean.Clean.AI.Models.MissaoModelAssembler;
import fiap.com.br.Ocean.Clean.AI.Repository.MissaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("missoes")
@CacheConfig(cacheNames = "missoes")
@Tag(name = "missoes")
public class MissaoController {

    @Autowired
    private MissaoRepository missaoRepository;

    @Autowired
    private MissaoModelAssembler assembler;

    @GetMapping("{id_missao}")
    @Operation(
        summary = "Buscar uma missão pelo ID.",
        description = "Retorna os detalhes da missão através do `id` informado como parâmetro de path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Os dados da missão foram retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe missão com o `id` informado.", useReturnTypeSchema = false)
        }
    )
    public EntityModel<MissaoModel> one(@PathVariable Long id_missao) {
        Missao missao = missaoRepository.findById(id_missao)
                .orElseThrow(() -> new ResourceNotFoundException("Missao not found"));
        return EntityModel.of(assembler.toModel(missao),
                WebMvcLinkBuilder.linkTo(methodOn(MissaoController.class).one(id_missao)).withSelfRel());
    }

    @GetMapping
    @Operation(
        summary = "Listar todas as missões.",
        description = "Retorna um array com todas as missões no formato do objeto."
    )
    public CollectionModel<MissaoModel> all() {
        List<Missao> missoes = missaoRepository.findAll();
        List<MissaoModel> missaoModels = missoes.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(missaoModels,
                WebMvcLinkBuilder.linkTo(methodOn(MissaoController.class).all()).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Registrar uma nova missão.",
        description = "Cria uma nova missão com os dados enviados no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Missão registrada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição.")
        }
    )
    public EntityModel<MissaoModel> newMissao(@RequestBody Missao newMissao) {
        Missao savedMissao = missaoRepository.save(newMissao);
        return EntityModel.of(assembler.toModel(savedMissao),
                WebMvcLinkBuilder.linkTo(methodOn(MissaoController.class).one(savedMissao.getId_missao())).withSelfRel());
    }

    @DeleteMapping("{id_missao}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Deletar uma missão pelo ID.",
        description = "Deleta todos os dados de uma missão através do ID especificado no parâmetro path."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "Missão apagada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não existe missão com o `id` informado.")
        }
    )
    public void deleteMissao(@PathVariable Long id_missao) {
        missaoRepository.deleteById(id_missao);
    }

    @PutMapping("{id_missao}")
    @Operation(
        summary = "Atualizar os dados de uma missão pelo ID.",
        description = "Altera os dados da missão especificada no `id`, utilizando as informações enviadas no corpo da requisição."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Missão alterada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição."),
            @ApiResponse(responseCode = "404", description = "Não existe missão com o `id` informado.")
        }
    )
    public EntityModel<MissaoModel> replaceMissao(@RequestBody Missao newMissao, @PathVariable Long id_missao) {
        Missao updatedMissao = missaoRepository.findById(id_missao)
                .map(missao -> {
                    missao.setNome(newMissao.getNome());
                    missao.setOperador(newMissao.getOperador());
                    missao.setResiduosPlasticos(newMissao.getResiduosPlasticos());
                    return missaoRepository.save(missao);
                })
                .orElseGet(() -> {
                    newMissao.setId_missao(id_missao);
                    return missaoRepository.save(newMissao);
                });

        return EntityModel.of(assembler.toModel(updatedMissao),
                WebMvcLinkBuilder.linkTo(methodOn(MissaoController.class).one(updatedMissao.getId_missao())).withSelfRel());
    }
}
