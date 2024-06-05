package fiap.com.br.Ocean.Clean.AI.Models;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import fiap.com.br.Ocean.Clean.AI.Controller.OperadorController;

@Component
public class OperadorModelAssembler extends RepresentationModelAssemblerSupport<Operador, OperadorModel> {

    public OperadorModelAssembler() {
        super(OperadorController.class, OperadorModel.class);
    }

    @Override
    public OperadorModel toModel(Operador operador) {
        OperadorModel model = createModelWithId(operador.getId_operador(), operador);
        model.setId_operador(operador.getId_operador());
        model.setNome(operador.getNome());
        model.setEmail(operador.getEmail());
        model.setMissoes(operador.getMissoes());
        model.setDrones(operador.getDrones());

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OperadorController.class).one(operador.getId_operador())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OperadorController.class).all()).withRel("operadores"));

        return model;
    }
}
