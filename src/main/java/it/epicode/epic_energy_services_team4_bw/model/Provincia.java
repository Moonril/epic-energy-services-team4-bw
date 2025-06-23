package it.epicode.epic_energy_services_team4_bw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "province")
public class Provincia {
    @Id
    @GeneratedValue
    private int id;
    private String nome;
    private String sigla;
    private String regione;

    @JsonIgnore
    @OneToMany(mappedBy = "provincia")
    private List<Comune> comuni = new ArrayList<>();

}
