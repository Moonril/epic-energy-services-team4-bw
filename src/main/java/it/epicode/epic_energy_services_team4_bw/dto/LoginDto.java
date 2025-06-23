package it.epicode.epic_energy_services_team4_bw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @NotEmpty(message = "Il campo username non può essere vuoto")
    private String username;
    @NotEmpty(message = "Il campo password non può essere vuoto")
    private String password;
}
