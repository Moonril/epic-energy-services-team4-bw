package it.epicode.epic_energy_services_team4_bw.dto;

import it.epicode.epic_energy_services_team4_bw.enums.TipoSede;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IndirizzoDto {
    @NotEmpty(message = "La via dell'indirizzo non può essere vuota")
    private String via;
    @NotEmpty(message = "Il numero civico non può essere vuoto")
    private String civico;
    @NotEmpty(message = "La località dell'indirizzo non può essere vuota")
    private String localita;
    @NotNull(message = "Il tipo sede non può essere nullo")
    private TipoSede tipoSede;
    @NotNull(message = "Il comune deve essere specificato")
    private int comuneId;
    @NotEmpty(message = "Il cap dev'essere specificato")
    private String cap;
}

