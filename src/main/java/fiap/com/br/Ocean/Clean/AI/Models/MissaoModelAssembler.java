package fiap.com.br.Ocean.Clean.AI.Models;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import fiap.com.br.Ocean.Clean.AI.Controller.MissaoController;
import fiap.com.br.Ocean.Clean.AI.Controller.OperadorController;

@Component
public class MissaoModelAssembler extends RepresentationModelAssemblerSupport<Missao, MissaoModel> {

    public MissaoModelAssembler() {
        super(MissaoController.class, MissaoModel.class);
    }

    @Override
    public MissaoModel toModel(Missao missao) {
        MissaoModel model = createModelWithId(missao.getId_missao(), missao);
        model.setId_missao(missao.getId_missao());
        model.setNome(missao.getNome());
        model.setOperador(missao.getOperador());
        model.setResiduosPlasticos(missao.getResiduosPlasticos());

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MissaoController.class).one(missao.getId_missao())).withSelfRel());
if (missao.getOperador() != null) {
    model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OperadorController.class).one(missao.getOperador().getId_operador())).withRel("operador"));
}
        return model;
    }
}
