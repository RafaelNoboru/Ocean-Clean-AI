package fiap.com.br.Ocean.Clean.AI.Models;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResiduoPlasticoModel extends RepresentationModel<ResiduoPlasticoModel> {

    private Long id_residuo;
    private int coordenadaX;
    private int coordenadaY;
    private Missao missao;
}
