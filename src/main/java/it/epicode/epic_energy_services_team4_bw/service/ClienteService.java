package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.ClienteRequestDTO;
import it.epicode.epic_energy_services_team4_bw.dto.ClienteResponseDTO;
import it.epicode.epic_energy_services_team4_bw.dto.IndirizzoDTO;
import it.epicode.epic_energy_services_team4_bw.dto.FatturaInfoDTO;
//import it.epicode.epic_energy_services_team4_bw.exceptions.BadRequestException;
//import it.epicode.epic_energy_services_team4_bw.exceptions.ResourceNotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import it.epicode.epic_energy_services_team4_bw.model.Fatture;
import it.epicode.epic_energy_services_team4_bw.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepo;

    public Page<ClienteResponseDTO> findAll(Pageable pageable) {
        Page<Cliente> clientiPage = clienteRepo.findAll(pageable);
        return clientiPage.map(this::convertToResponseDTO);
    }

    public ClienteResponseDTO findById(int id) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id=" + id + " non trovato."));
        return convertToResponseDTO(cliente);
    }

    public ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO) {
        clienteRepo.findByPartitaIva(clienteRequestDTO.getPartitaIva()).ifPresent(cliente -> {
            throw new BadRequestException("Partita IVA " + clienteRequestDTO.getPartitaIva() + " già esistente.");
        });

        Cliente cliente = convertToEntity(clienteRequestDTO);
        Cliente savedCliente = clienteRepo.save(cliente);
        return convertToResponseDTO(savedCliente);
    }

    public ClienteResponseDTO update(int id, ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id=" + id + " non trovato."));

        cliente.setRagioneSociale(clienteRequestDTO.getRagioneSociale());
        cliente.setPartitaIva(clienteRequestDTO.getPartitaIva());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setDataInserimento(clienteRequestDTO.getDataInserimento());
        cliente.setDataUltimoContatto(clienteRequestDTO.getDataUltimoContatto());
        cliente.setFatturatoAnnuale(clienteRequestDTO.getFatturatoAnnuale());
        cliente.setTelefono(clienteRequestDTO.getTelefono());
        cliente.setEmailContatto(clienteRequestDTO.getEmailContatto());
        cliente.setNomeContatto(clienteRequestDTO.getNomeContatto());
        cliente.setCognomeContatto(clienteRequestDTO.getCognomeContatto());
        cliente.setTipoCliente(clienteRequestDTO.getTipoCliente());

        Cliente updatedCliente = clienteRepo.save(cliente);
        return convertToResponseDTO(updatedCliente);
    }

    public void delete(int id) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id=" + id + " non trovato."));
        clienteRepo.delete(cliente);
    }

    private ClienteResponseDTO convertToResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setRagioneSociale(cliente.getRagioneSociale());
        dto.setPartitaIva(cliente.getPartitaIva());
        dto.setEmail(cliente.getEmail());
        dto.setDataInserimento(cliente.getDataInserimento());
        dto.setDataUltimoContatto(cliente.getDataUltimoContatto());
        dto.setFatturatoAnnuale(cliente.getFatturatoAnnuale());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmailContatto(cliente.getEmailContatto());
        dto.setNomeContatto(cliente.getNomeContatto());
        dto.setCognomeContatto(cliente.getCognomeContatto());
        dto.setLogoAziendaleUrl(cliente.getLogoAziendale());
        dto.setTipoCliente(cliente.getTipoCliente());

        if (cliente.getIndirizzi() != null) {
            dto.setIndirizzi(cliente.getIndirizzi().stream()
                    .map(this::convertToIndirizzoDTO)
                    .collect(Collectors.toList()));
        }
        if (cliente.getFatture() != null) {
            dto.setFatture(cliente.getFatture().stream()
                    .map(this::convertToFatturaInfoDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private Cliente convertToEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setRagioneSociale(dto.getRagioneSociale());
        cliente.setPartitaIva(dto.getPartitaIva());
        cliente.setEmail(dto.getEmail());
        cliente.setDataInserimento(dto.getDataInserimento());
        cliente.setDataUltimoContatto(dto.getDataUltimoContatto());
        cliente.setFatturatoAnnuale(dto.getFatturatoAnnuale());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmailContatto(dto.getEmailContatto());
        cliente.setNomeContatto(dto.getNomeContatto());
        cliente.setCognomeContatto(dto.getCognomeContatto());
        cliente.setTipoCliente(dto.getTipoCliente());
        return cliente;
    }

    private IndirizzoDTO convertToIndirizzoDTO(Indirizzo indirizzo) {
        IndirizzoDTO dto = new IndirizzoDTO();
        dto.setId(indirizzo.getId());
        dto.setVia(indirizzo.getVia());
        dto.setCivico(indirizzo.getCivico());
        dto.setLocalita(indirizzo.getLocalita());
        dto.setCap(indirizzo.getCap());
        dto.setTipoSede(indirizzo.getTipoSede());
        if (indirizzo.getComune() != null) {
            dto.setNomeComune(indirizzo.getComune().getNome());
            if (indirizzo.getComune().getProvincia() != null) {
                dto.setSiglaProvincia(indirizzo.getComune().getProvincia().getSigla());
            }
        }
        return dto;
    }

    private FatturaInfoDTO convertToFatturaInfoDTO(Fatture fattura) {
        FatturaInfoDTO dto = new FatturaInfoDTO();
        dto.setId(fattura.getId());
        dto.setNumero(fattura.getNumero());
        dto.setData(fattura.getData());
        dto.setImporto(fattura.getImporto());
        if (fattura.getStato() != null) {
            dto.setStato(fattura.getStato().getNome());
        }
        return dto;
    }

}
