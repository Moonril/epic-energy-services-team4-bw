package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.FattureDto;
import it.epicode.epic_energy_services_team4_bw.enums.StatoFattura;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.model.Fatture;
import it.epicode.epic_energy_services_team4_bw.repository.ClienteRepository;
import it.epicode.epic_energy_services_team4_bw.repository.FattureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class FattureService {

    @Autowired
    private FattureRepository fattureRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    /**
     * Recupera tutte le fatture paginate.
     * @param pageable Oggetto Pageable per paginazione e ordinamento.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findAll(Pageable pageable) {
        Page<Fatture> fatturePage = fattureRepository.findAll(pageable);
        return fatturePage.map(this::convertToDTO);
    }

    /**
     * Recupera una fattura tramite ID.
     * @param id ID della fattura.
     * @return FattureDto corrispondente.
     * @throws NotFoundException Se la fattura non viene trovata.
     */
    public FattureDto findById(Long id) throws NotFoundException {
        Fatture fattura = fattureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id=" + id + " non trovata."));
        return convertToDTO(fattura);
    }

    /**
     * Salva una nuova fattura.
     * @param fatturaDto DTO della fattura da salvare.
     * @return FattureDto della fattura salvata.
     * @throws BadRequestException Se il numero fattura è già esistente.
     * @throws NotFoundException Se il cliente specificato non viene trovato.
     */
    public FattureDto save(FattureDto fatturaDto) throws NotFoundException, BadRequestException {
        if (fattureRepository.existsByNumeroAndIdNot(fatturaDto.getNumero(), null)) {
            throw new BadRequestException("Numero fattura '" + fatturaDto.getNumero() + "' già esistente.");
        }

        Fatture fattura = convertToEntity(fatturaDto);
        Fatture savedFattura = fattureRepository.save(fattura);
        return convertToDTO(savedFattura);
    }

    /**
     * Aggiorna una fattura esistente.
     * @param id ID della fattura da aggiornare.
     * @param fatturaDto DTO con i dati aggiornati.
     * @return FattureDto della fattura aggiornata.
     * @throws NotFoundException Se la fattura o il cliente non vengono trovati.
     * @throws BadRequestException Se il numero fattura aggiornato è già esistente e appartiene a un'altra fattura.
     */
    public FattureDto update(Long id, FattureDto fatturaDto) throws NotFoundException, BadRequestException {
        Fatture fattura = fattureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id=" + id + " non trovata."));

        if (fattureRepository.existsByNumeroAndIdNot(fatturaDto.getNumero(), id)) {
            throw new BadRequestException("Numero fattura '" + fatturaDto.getNumero() + "' già esistente per un'altra fattura.");
        }

        fattura.setData(fatturaDto.getData());
        fattura.setImporto(fatturaDto.getImporto());
        fattura.setNumero(fatturaDto.getNumero());
        fattura.setStato(fatturaDto.getStato());
        if (fatturaDto.getClienteId() != null) {
            if (fattura.getCliente() == null || !fatturaDto.getClienteId().equals(Long.valueOf(fattura.getCliente().getId()))) {
                Cliente newCliente = clienteRepository.findById(fatturaDto.getClienteId().intValue()) // Converti Long a int per findById
                        .orElseThrow(() -> new NotFoundException("Cliente con id=" + fatturaDto.getClienteId() + " non trovato."));
                fattura.setCliente(newCliente);
            }
        } else {
            throw new BadRequestException("L'ID del cliente non può essere nullo per l'aggiornamento della fattura.");
        }

        Fatture updatedFattura = fattureRepository.save(fattura);
        return convertToDTO(updatedFattura);
    }

    /**
     * Elimina una fattura tramite ID.
     * @param id ID della fattura da eliminare.
     * @throws NotFoundException Se la fattura non viene trovata.
     */
    public void delete(Long id) throws NotFoundException {
        Fatture fattura = fattureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id=" + id + " non trovata."));
        fattureRepository.delete(fattura);
    }

    /**
     * Recupera le fatture filtrate per stato.
     * @param stato Stato della fattura.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByStato(StatoFattura stato, Pageable pageable) {
        return fattureRepository.findByStato(stato, pageable).map(this::convertToDTO);
    }

    /**
     * Recupera le fatture filtrate per data.
     * @param data Data della fattura.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByData(LocalDate data, Pageable pageable) {
        return fattureRepository.findByData(data, pageable).map(this::convertToDTO);
    }

    /**
     * Recupera le fatture filtrate per anno.
     * @param anno Anno della fattura.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByAnno(int anno, Pageable pageable) {
        return fattureRepository.findByAnno(anno, pageable).map(this::convertToDTO);
    }

    /**
     * Recupera le fatture filtrate per intervallo di importo.
     * @param importoMin Importo minimo.
     * @param importoMax Importo massimo.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByImportoBetween(BigDecimal importoMin, BigDecimal importoMax, Pageable pageable) {
        return fattureRepository.findByImportoBetween(importoMin, importoMax, pageable).map(this::convertToDTO);
    }

    /**
     * Recupera le fatture filtrate per ID cliente e stato.
     * @param clienteId ID del cliente.
     * @param stato Lo stato della fattura.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByClienteIdAndStato(Long clienteId, StatoFattura stato, Pageable pageable) {
        return fattureRepository.findByClienteIdAndStato(clienteId, stato, pageable).map(this::convertToDTO);
    }

    /**
     * Recupera le fatture filtrate per ID cliente e data.
     * @param clienteId ID del cliente.
     * @param data La data della fattura.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByClienteIdAndData(Long clienteId, LocalDate data, Pageable pageable) {
        return fattureRepository.findByClienteIdAndData(clienteId, data, pageable).map(this::convertToDTO);
    }

    /**
     * Recupera le fatture filtrate per stato e data.
     * @param stato Lo stato della fattura.
     * @param data La data della fattura.
     * @param pageable Oggetto Pageable.
     * @return Pagina di FattureDto.
     */
    public Page<FattureDto> findByStatoAndData(StatoFattura stato, LocalDate data, Pageable pageable) {
        return fattureRepository.findByStatoAndData(stato, data, pageable).map(this::convertToDTO);
    }


    /**
     * Converte un'entità Fatture in un DTO FattureDto.
     * Gestisce la conversione dell'ID del cliente da int a Long.
     * @param fattura L'entità Fatture da convertire.
     * @return Il DTO FattureDto risultante.
     */
    private FattureDto convertToDTO(Fatture fattura) {
        FattureDto dto = new FattureDto();
        dto.setId(fattura.getId());
        dto.setData(fattura.getData());
        dto.setImporto(fattura.getImporto());
        dto.setNumero(fattura.getNumero());
        dto.setStato(fattura.getStato());
        if (fattura.getCliente() != null) {
            dto.setClienteId(Long.valueOf(fattura.getCliente().getId()));
        } else {
            dto.setClienteId(null);
        }
        return dto;
    }

    /**
     * Converte un DTO FattureDto in un'entità Fatture.
     * Gestisce la conversione dell'ID del cliente da Long a int per trovare il Cliente.
     * @param dto Il DTO FattureDto da convertire.
     * @return L'entità Fatture risultante.
     * @throws NotFoundException Se il cliente specificato nell'ID non viene trovato.
     * @throws BadRequestException Se l'ID del cliente è nullo nel DTO.
     */
    private Fatture convertToEntity(FattureDto dto) throws NotFoundException, BadRequestException {
        Fatture fattura = new Fatture();
        Optional.ofNullable(dto.getId()).ifPresent(fattura::setId);
        fattura.setData(dto.getData());
        fattura.setImporto(dto.getImporto());
        fattura.setNumero(dto.getNumero());
        fattura.setStato(dto.getStato());

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId().intValue())
                    .orElseThrow(() -> new NotFoundException("Cliente con id=" + dto.getClienteId() + " non trovato per la fattura."));
            fattura.setCliente(cliente);
        } else {
            throw new BadRequestException("Cliente ID è obbligatorio per la fattura.");
        }

        return fattura;
    }
}