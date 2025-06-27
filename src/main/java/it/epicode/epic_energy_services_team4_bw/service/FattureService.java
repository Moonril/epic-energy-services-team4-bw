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

    @Autowired
    private ClienteService clienteService;

    public Fatture saveFattura(FattureDto fattureDto) throws NotFoundException {
        if (fattureRepository.existsByNumeroAndIdNot(fattureDto.getNumero(), null)) {
            throw new BadRequestException("Numero fattura '" + fattureDto.getNumero() + "' già esistente.");
        }
        Cliente cliente = clienteService.findClienteById(fattureDto.getClienteId());

        Fatture fattura = new Fatture();
        fattura.setData(fattureDto.getData());
        fattura.setImporto(fattureDto.getImporto());
        fattura.setNumero(fattureDto.getNumero());
        fattura.setStato(fattureDto.getStato());
        fattura.setCliente(cliente);

        return fattureRepository.save(fattura);
    }

    public Page<Fatture> getAllFatture(Pageable pageable) {
        return fattureRepository.findAll(pageable);
    }

    public Fatture getFatturaById(int id) throws NotFoundException {
        return fattureRepository.findById(id).orElseThrow(() -> new NotFoundException("Fattura con id=" + id + " non trovata."));
    }

    public Fatture updateFattura(int id, FattureDto fattureDto) throws NotFoundException {
        Fatture fattura = getFatturaById(id);

        if (fattureRepository.existsByNumeroAndIdNot(fattureDto.getNumero(), id)) {
            throw new BadRequestException("Numero fattura '" + fattureDto.getNumero() + "' già esistente per un'altra fattura.");
        }

        Cliente cliente = clienteService.findClienteById(fattureDto.getClienteId());

        fattura.setData(fattureDto.getData());
        fattura.setImporto(fattureDto.getImporto());
        fattura.setNumero(fattureDto.getNumero());
        fattura.setStato(fattureDto.getStato());
        fattura.setCliente(cliente);

        return fattureRepository.save(fattura);
    }

    public void deleteFattura(int id) throws NotFoundException {
        fattureRepository.delete(getFatturaById(id));
    }
    public Page<Fatture> findByStato(StatoFattura stato, Pageable pageable) { return fattureRepository.findByStato(stato, pageable); }
    public Page<Fatture> findByData(LocalDate data, Pageable pageable) { return fattureRepository.findByData(data, pageable); }
    public Page<Fatture> findByAnno(int anno, Pageable pageable) { return fattureRepository.findByAnno(anno, pageable); }
    public Page<Fatture> findByImportoBetween(BigDecimal min, BigDecimal max, Pageable pageable) { return fattureRepository.findByImportoBetween(min, max, pageable); }
    public Page<Fatture> findByClienteId(int clienteId, Pageable pageable){return fattureRepository.findByClienteId(clienteId,pageable);}
}
