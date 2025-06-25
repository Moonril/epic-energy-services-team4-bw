package it.epicode.epic_energy_services_team4_bw.controller;

import it.epicode.epic_energy_services_team4_bw.dto.ClienteDTO;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.exception.ValidationException;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/clienti")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Cliente> getAllClienti(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return clienteService.findAllClienti(page, size, sortBy);
    }

    //ordinamenti
    @GetMapping("/ordinati")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getClientiOrdinati(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy
    ) {
        return clienteService.getClientiOrdinati(page, size, sortBy);
    }

    @GetMapping("/ordinati-provincia")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getClientiOrdinatiPerProvincia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return clienteService.getClientiOrdinatiPerProvincia(page, size);
    }

    @GetMapping("/filtro")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> filtraClienti(
            @RequestParam BigDecimal fatturatoMin,
            @RequestParam LocalDate dataInserimento,
            @RequestParam LocalDate dataUltimoContatto,
            @RequestParam String parteNome
            ){
        return clienteService.filtraClienti(fatturatoMin, dataInserimento, dataUltimoContatto, parteNome);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Cliente getClienteById(@PathVariable int id) throws NotFoundException {
        return clienteService.findClienteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente saveCliente(@RequestBody @Validated ClienteDTO clienteDTO, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("",(e,s)->e+s));
        }
        return clienteService.saveCliente(clienteDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente updateCliente(@PathVariable int id, @RequestBody @Validated ClienteDTO clienteDTO, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("",(e,s)->e+s));
        }
        return clienteService.update(id, clienteDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCliente(@PathVariable int id) throws NotFoundException {
        clienteService.deleteCliente(id);
    }
    @PatchMapping("/{id}/logo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String patchLogo(@PathVariable int id, @RequestParam MultipartFile file) throws NotFoundException, IOException {
        return clienteService.patchLogoCliente(id, file);
    }
}
