package fiap.com.br.Ocean.Clean.AI.Models;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MissaoModel extends RepresentationModel<MissaoModel> {

    private Long id_missao;
    private String nome;
    private Operador operador;
    private List<ResiduoPlastico> residuosPlasticos;
    
}
