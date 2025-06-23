package it.epicode.epic_energy_services_team4_bw.dto;

import it.epicode.epic_energy_services_team4_bw.enums.TipoUtente;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UtenteDto {
    @NotEmpty(message = "Il campo nome non può essere vuoto")
    private String nome;
    @NotEmpty(message = "Il campo cognome non può essere vuoto")
    private String cognome;
    @Email(message = "L'email deve avere un formato valido, es: indirizzo@gmail.com")
    private String email;
    @NotEmpty(message = "Il campo username non può essere vuoto")
    private String username;
    @NotEmpty(message = "Il campo password non può essere vuoto")
    private String password;
}
