package fiap.com.br.Ocean.Clean.AI.Models;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import fiap.com.br.Ocean.Clean.AI.Controller.DroneController;
import fiap.com.br.Ocean.Clean.AI.Controller.OperadorController;

@Component
public class DroneModelAssembler extends RepresentationModelAssemblerSupport<Drone, DroneModel> {

    public DroneModelAssembler() {
        super(DroneController.class, DroneModel.class);
    }

    @Override
    public DroneModel toModel(Drone drone) {
        DroneModel model = createModelWithId(drone.getId_drone(), drone);
        model.setId_drone(drone.getId_drone());
        model.setNome(drone.getNome());
        model.setCoordenadaX(drone.getCoordenadaX());
        model.setCoordenadaY(drone.getCoordenadaY());
        model.setResiduosColetados(drone.getResiduosColetados());
        model.setNivelBateria(drone.getNivelBateria());
        model.setOperador(drone.getOperador());
        
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DroneController.class).one(drone.getId_drone())).withSelfRel());
        if (drone.getOperador() != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OperadorController.class).one(drone.getOperador().getId_operador())).withRel("operador"));
        }
        return model;
    }
}