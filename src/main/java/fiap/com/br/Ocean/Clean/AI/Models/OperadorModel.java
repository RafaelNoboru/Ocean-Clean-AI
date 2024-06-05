package fiap.com.br.Ocean.Clean.AI.Models;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OperadorModel extends RepresentationModel<OperadorModel> {

    private Long id_operador;
    private String nome;
    private String email;
    private List<Missao> missoes;
    private List<Drone> drones;
}