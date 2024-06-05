package fiap.com.br.Ocean.Clean.AI.Models;

import java.util.List; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Missao{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_missao;

    @NotBlank(message = "Nome da missão é obrigatório")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_operador")
    @JsonIgnoreProperties("missoes")
    private Operador operador;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("missao")
    private List<ResiduoPlastico> residuosPlasticos;

}
