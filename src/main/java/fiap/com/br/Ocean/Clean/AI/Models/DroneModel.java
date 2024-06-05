package fiap.com.br.Ocean.Clean.AI.Models;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DroneModel extends RepresentationModel<DroneModel> {

    private Long id_drone;
    private String nome;
    private int coordenadaX;
    private int coordenadaY;
    private int residuosColetados;
    private int nivelBateria;
    private Operador operador;

}

