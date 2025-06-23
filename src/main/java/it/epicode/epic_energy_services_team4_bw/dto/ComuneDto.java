package it.epicode.epic_energy_services_team4_bw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ComuneDto {
    @NotEmpty(message = "Il campo nome non può essere vuoto")
    private String nome;

    private int provinciaId;
}
