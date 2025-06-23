package it.epicode.epic_energy_services_team4_bw.dto;

import it.epicode.epic_energy_services_team4_bw.enums.TipoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClientRequestDTO {
    @NotBlank(message = "La ragione sociale non può essere vuota")
    @Size(min = 3,max = 100,message = "La ragione sociale dovrebbe avere tra i 3 e i 100 caratteri")
    private String ragioneSociale;
    @NotBlank(message = "La partita IVA non può essere vuota")
    @Size(min = 11,max = 11,message = "La partita IVA ha 11 caratteri")
    private String partitaIva;
    @Email(message = "L'indirizzo email non è  valido")
    @NotBlank(message = "L'email non può essere vuota")
    private String email;
    @NotNull(message = "La data di inserimento non può essere nulla")
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private BigDecimal fatturatoAnnuale;
    private String telefono;
    @Email(message = "L'email del contatto non è valida")
    private String emailContatto;
    private String nomeContatto;
    private String cognomeContatto;
    @NotNull(message = "Il tipo cliente non può essere nullo")
    private TipoCliente tipoCliente;
}
