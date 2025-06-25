package it.epicode.epic_energy_services_team4_bw.service;
import com.cloudinary.Cloudinary;
import it.epicode.epic_energy_services_team4_bw.dto.ClienteDTO;
import it.epicode.epic_energy_services_team4_bw.dto.IndirizzoDto;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.model.Comune;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import it.epicode.epic_energy_services_team4_bw.repository.ClienteRepository;
import it.epicode.epic_energy_services_team4_bw.repository.ComuneRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    ComuneRepository comuneRepository;

    @Autowired
    private Cloudinary cloudinary;


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
    public Cliente saveCliente(ClienteDTO clienteDTO) throws NotFoundException {
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

        List<Indirizzo> indirizzi = new ArrayList<>();
        if (clienteDTO.getIndirizzi() != null) {
            for (IndirizzoDto iDto : clienteDTO.getIndirizzi()) {
                Indirizzo indirizzo = new Indirizzo();
                indirizzo.setVia(iDto.getVia());
                indirizzo.setCivico(iDto.getCivico());
                indirizzo.setLocalita(iDto.getLocalita());
                indirizzo.setCap(iDto.getCap());

                Comune comune = comuneRepository.findById(iDto.getComuneId())
                        .orElseThrow(() -> new NotFoundException("Comune con id " + iDto.getComuneId() + " non trovato"));
                indirizzo.setComune(comune);

                indirizzo.setCliente(cliente);
                indirizzi.add(indirizzo);
            }
        }

        cliente.setIndirizzi(indirizzi);

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
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findAllOrderByProvincia(pageable);
    }
    }

