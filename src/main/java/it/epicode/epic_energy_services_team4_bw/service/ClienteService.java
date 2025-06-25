package it.epicode.epic_energy_services_team4_bw.service;
import it.epicode.epic_energy_services_team4_bw.dto.ClienteDTO;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Collections;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    @Transactional(readOnly = true)
    public Page<Cliente> findAllClienti(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return clienteRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Cliente findClienteById(int id) throws NotFoundException {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id=" + id + " non trovato."));
    }
    public Cliente saveCliente(ClienteDTO clienteDTO) {
        clienteRepository.findByPartitaIva(clienteDTO.getPartitaIva()).ifPresent(cliente -> {
            throw new BadRequestException("Partita IVA " + clienteDTO.getPartitaIva() + " già esistente.");
        });
        Cliente cliente = new Cliente();
        cliente.setRagioneSociale(clienteDTO.getRagioneSociale());
        cliente.setPartitaIva(clienteDTO.getPartitaIva());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setDataInserimento(clienteDTO.getDataInserimento());
        cliente.setDataUltimoContatto(clienteDTO.getDataUltimoContatto());
        cliente.setFatturatoAnnuale(clienteDTO.getFatturatoAnnuale());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setEmailContatto(clienteDTO.getEmailContatto());
        cliente.setNomeContatto(clienteDTO.getNomeContatto());
        cliente.setCognomeContatto(clienteDTO.getCognomeContatto());
        cliente.setTipoCliente(clienteDTO.getTipoCliente());

        return clienteRepository.save(cliente);
    }

    public Cliente update(int id, ClienteDTO clienteDTO) throws NotFoundException {
        Cliente cliente = findClienteById(id);
        cliente.setRagioneSociale(clienteDTO.getRagioneSociale());
        cliente.setPartitaIva(clienteDTO.getPartitaIva());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setDataInserimento(clienteDTO.getDataInserimento());
        cliente.setDataUltimoContatto(clienteDTO.getDataUltimoContatto());
        cliente.setFatturatoAnnuale(clienteDTO.getFatturatoAnnuale());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setEmailContatto(clienteDTO.getEmailContatto());
        cliente.setNomeContatto(clienteDTO.getNomeContatto());
        cliente.setCognomeContatto(clienteDTO.getCognomeContatto());
        cliente.setTipoCliente(clienteDTO.getTipoCliente());

        return clienteRepository.save(cliente);
    }
    public void deleteCliente(int id) throws NotFoundException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id=" + id + " non trovato."));
        clienteRepository.delete(cliente);
    }

    public List<Cliente> filtraClienti(BigDecimal fatturatoMin, LocalDate dataInserimento,
                                       LocalDate dataUltimoContatto, String parteNome){
        if (fatturatoMin != null) {
            return clienteRepository.findByFatturatoAnnualeGreaterThanEqual(fatturatoMin);
        } else if (dataInserimento != null) {
            return clienteRepository.findByDataInserimentoAfter(dataInserimento);
        } else if (dataUltimoContatto != null) {
            return clienteRepository.findByDataUltimoContattoAfter(dataUltimoContatto);
        } else if (parteNome != null && !parteNome.isEmpty()) {
            return clienteRepository.findByRagioneSocialeContainingIgnoreCase(parteNome);
        } else {
            return clienteRepository.findAll();
        }
    }
    public String patchLogoCliente(int id, MultipartFile file) throws NotFoundException, IOException {
        Cliente clienteDaPatchare = findClienteById(id);
//qui non mi legge cloudinary
        String url = (String)cloudinary.uploader().upload(file.getBytes(),
                Collections.emptyMap()).get("url");

        clienteDaPatchare.setLogoAziendale(url);

        clienteRepository.save(clienteDaPatchare);

        return url;
    }
    public Page<Cliente> getClientiOrdinati(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return clienteRepository.findAll(pageable);
    }

    public Page<Cliente> getClientiOrdinatiPerProvincia(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("sedeLegale.comune.provincia.ragioneSociale"));
        return clienteRepository.findAll(pageable);
    }
    }

