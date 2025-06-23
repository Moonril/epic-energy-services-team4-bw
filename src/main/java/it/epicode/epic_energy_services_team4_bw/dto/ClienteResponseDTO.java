package it.epicode.epic_energy_services_team4_bw.dto;

import it.epicode.epic_energy_services_team4_bw.enums.TipoCliente;
import it.epicode.epic_energy_services_team4_bw.model.Fatture;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ClienteResponseDTO {
    private int id;
    private String ragioneSociale;
    private String email;
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private BigDecimal fatturatoAnnuale;
    private String telefono;
    private String emailContatto;
    private String nomeContatto;
    private String cognomeContatto;
    private String logoAziendale;
    private TipoCliente tipoCliente;
    private List<IndirizzoDTO> indirizzi;
    private List<FattureDTO> fatture;
}
