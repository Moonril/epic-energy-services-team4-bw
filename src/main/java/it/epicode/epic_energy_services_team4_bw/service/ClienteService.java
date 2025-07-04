package it.epicode.epic_energy_services_team4_bw.service;
import com.cloudinary.Cloudinary;
import it.epicode.epic_energy_services_team4_bw.dto.ClienteDTO;
import it.epicode.epic_energy_services_team4_bw.dto.IndirizzoDto;
import it.epicode.epic_energy_services_team4_bw.enums.TipoSede;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;

import it.epicode.epic_energy_services_team4_bw.repository.ClienteRepository;
import it.epicode.epic_energy_services_team4_bw.repository.IndirizzoRepository;
import it.epicode.epic_energy_services_team4_bw.repository.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
    @Autowired
    IndirizzoRepository indirizzoRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSender;


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
    @Transactional
    public Cliente saveCliente(ClienteDTO clienteDTO) throws NotFoundException {
        clienteRepository.findByPartitaIva(clienteDTO.getPartitaIva()).ifPresent(c -> {
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
        cliente.setIndirizzi(new ArrayList<>());

        Cliente savedCliente = clienteRepository.save(cliente);

        if (clienteDTO.getIndirizziId() != null && !clienteDTO.getIndirizziId().isEmpty()) {
            List<Indirizzo> indirizziDaAssociare = indirizzoRepository.findAllById(clienteDTO.getIndirizziId());
            if (indirizziDaAssociare.size() != clienteDTO.getIndirizziId().size()) throw new NotFoundException("Uno o più ID di indirizzi forniti non sono stati trovati.");
            for (Indirizzo indirizzo : indirizziDaAssociare) {
                if (indirizzo.getCliente() != null) throw new BadRequestException("L'indirizzo con id=" + indirizzo.getId() + " è già associato.");
                indirizzo.setCliente(savedCliente);
            }
            indirizzoRepository.saveAll(indirizziDaAssociare);
        }

        sendMail("lucaferr95@gmail.com", cliente);
        return clienteRepository.findById(savedCliente.getId()).get();
    }


    public Cliente updateCliente(int id, ClienteDTO clienteDTO) throws NotFoundException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id=" + id + " non trovato."));
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

    public Page<Cliente> getClientiOrdinatiPerProvinciaSedeLegale(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findAllOrderByProvinciaSedeLegale(TipoSede.SEDE_LEGALE, pageable);
    }



    private void sendMail(String email, Cliente cliente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registrazione Dipendente");
        message.setText("Benvenut* " + cliente.getNomeContatto() +", la tua registrazione è avvenuta con successo!");

        javaMailSender.send(message);
    }
    }

