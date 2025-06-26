package it.epicode.epic_energy_services_team4_bw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.epic_energy_services_team4_bw.enums.TipoSede;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Indirizzo {
    @Id
    @GeneratedValue
    private int id;
    private String via;
    private String civico;
    private String localita;
    private String cap;
    @Enumerated(EnumType.STRING)
    private TipoSede tipoSede;
    @ManyToOne
    @JoinColumn(name = "comune_id")
    private Comune comune;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
