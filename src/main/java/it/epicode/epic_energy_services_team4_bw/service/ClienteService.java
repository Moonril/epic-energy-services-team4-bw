package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.ClienteDTO; // A
import it.epicode.epic_energy_services_team4_bw.dto.FattureDto;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
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
    private ClienteRepository clienteRepository;

    public Page<ClienteDTO> findAll(Pageable pageable) {
        Page<Cliente> clientiPage = clienteRepository.findAll(pageable);
        return clientiPage.map(this::convertToDTO);
    }

    public ClienteDTO findById(int id) throws NotFoundException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id=" + id + " non trovato."));
        return convertToDTO(cliente);
    }

    public ClienteDTO save(ClienteDTO clienteDTO) { // AGGIORNATO
        clienteRepository.findByPartitaIva(clienteDTO.getPartitaIva()).ifPresent(cliente -> {
            throw new BadRequestException("Partita IVA " + clienteDTO.getPartitaIva() + " già esistente.");
        });

        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    public ClienteDTO update(int id, ClienteDTO clienteDTO) throws NotFoundException { // AGGIORNATO
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

        Cliente updatedCliente = clienteRepository.save(cliente);
        return convertToDTO(updatedCliente);
    }

    public void delete(int id) throws NotFoundException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id=" + id + " non trovato."));
        clienteRepository.delete(cliente);
    }



    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
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

    private Cliente convertToEntity(ClienteDTO dto) {
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
        return dto;
    }

    private FattureDto convertToFatturaInfoDTO(Fatture fattura) {
        FattureDto dto = new FattureDto();
        dto.setId(fattura.getId());
        dto.setNumero(fattura.getNumero());
        return dto;
    }
}