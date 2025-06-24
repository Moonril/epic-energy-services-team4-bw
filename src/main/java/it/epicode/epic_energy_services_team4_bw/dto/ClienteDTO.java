package it.epicode.epic_energy_services_team4_bw.dto;

import it.epicode.epic_energy_services_team4_bw.enums.TipoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
    public class ClienteDTO {

        @NotBlank(message = "La ragione sociale non può essere vuota")
        @Size(min = 3, max = 100, message = "La ragione sociale deve avere tra 3 e 100 caratteri")
        private String ragioneSociale;

        @NotBlank(message = "La Partita IVA non può essere vuota")
        @Size(min = 11, max = 11, message = "La Partita IVA deve essere di 11 caratteri")
        private String partitaIva;

        @Email(message = "L'indirizzo email non è valido")
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
        private String logoAziendaleUrl;
        private List<IndirizzoDto> indirizzi;
        private List<FattureDto> fatture;
    }


