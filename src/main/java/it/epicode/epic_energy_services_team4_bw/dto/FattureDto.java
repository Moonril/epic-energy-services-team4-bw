package it.epicode.epic_energy_services_team4_bw.dto;

import it.epicode.epic_energy_services_team4_bw.enums.StatoFattura;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FattureDto {

    private Long id;

    @NotNull(message = "La data non può essere nulla")
    @PastOrPresent(message = "La data non può essere nel futuro")
    private LocalDate data;

    @NotNull(message = "L'importo non può essere nullo")
    @DecimalMin(value = "0.01", message = "L'importo deve essere maggiore di zero")
    private BigDecimal importo;

    @NotNull(message = "Il numero fattura non può essere nullo")
    @Size(min = 1, max = 50, message = "Il numero fattura deve avere tra 1 e 50 caratteri")
    private String numero;

    @NotNull(message = "L'ID del cliente non può essere nullo")
    private Long clienteId;

    @NotNull(message = "Lo stato fattura non può essere nullo")
    private StatoFattura stato;


    public  FattureDto() {
    }

    public  FattureDto(Long id, LocalDate data, BigDecimal importo, String numero, Long clienteId, StatoFattura stato) {
        this.id = id;
        this.data = data;
        this.importo = importo;
        this.numero = numero;
        this.clienteId = clienteId;
        this.stato = stato;
    }
}
