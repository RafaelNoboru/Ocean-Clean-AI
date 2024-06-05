package fiap.com.br.Ocean.Clean.AI.Models;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import fiap.com.br.Ocean.Clean.AI.Controller.MissaoController;

@Component
public class ResiduoPlasticoModelAssembler extends RepresentationModelAssemblerSupport<ResiduoPlastico, ResiduoPlasticoModel> {

    public ResiduoPlasticoModelAssembler() {
        super(MissaoController.class, ResiduoPlasticoModel.class);
    }

    @Override
    public ResiduoPlasticoModel toModel(ResiduoPlastico residuoPlastico) {
        ResiduoPlasticoModel model = createModelWithId(residuoPlastico.getId_residuo(), residuoPlastico);
        model.setId_residuo(residuoPlastico.getId_residuo());
        model.setCoordenadaX(residuoPlastico.getCoordenadaX());
        model.setCoordenadaY(residuoPlastico.getCoordenadaY());
        model.setMissao(residuoPlastico.getMissao());
        
        // Adicionando links HATEOAS
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MissaoController.class).one(residuoPlastico.getMissao().getId_missao())).withRel("missao"));
        
        return model;
    }
}   