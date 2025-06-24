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


}