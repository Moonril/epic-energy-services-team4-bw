package it.epicode.epic_energy_services_team4_bw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @NotEmpty(message = "L'email non può essere vuota")
    @Email
    private String email;
    @NotEmpty(message = "La password non può essere vuota")
    private String password;
}
