package it.epicode.epic_energy_services_team4_bw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProvinciaDto {
    @NotEmpty(message = "Il campo nome non può essere vuoto")
    private String nome;
    @NotEmpty(message = "Il campo sigla non può essere vuoto")
    private String sigla;
    @NotEmpty(message = "Il campo regione non può essere vuoto")
    private String regione;

    //collegamenti, non sono sicura che serva
    private int comuniId;


}
