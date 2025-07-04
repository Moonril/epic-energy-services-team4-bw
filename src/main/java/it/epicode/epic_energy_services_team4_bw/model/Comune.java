package it.epicode.epic_energy_services_team4_bw.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "comuni")
public class Comune {
    @Id
    @GeneratedValue
    private int id;
    private String nome;



    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    @JsonIgnore
    @OneToMany(mappedBy = "comune")
    private List<Indirizzo> indirizzi = new ArrayList<>();

}
