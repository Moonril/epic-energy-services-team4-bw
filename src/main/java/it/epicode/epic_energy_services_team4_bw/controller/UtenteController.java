package it.epicode.epic_energy_services_team4_bw.controller;

import it.epicode.epic_energy_services_team4_bw.dto.UtenteDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.exception.ValidationException;
import it.epicode.epic_energy_services_team4_bw.model.Utente;
import it.epicode.epic_energy_services_team4_bw.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente saveUtente(@RequestBody @Validated UtenteDto utenteDto, BindingResult bindingResult)
            throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }

        return utenteService.saveUtente(utenteDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente getUtente(@PathVariable int id) throws NotFoundException {
        return utenteService.getUtente(id);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getAllUtenti(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy){
        return utenteService.getAllUtenti(page, size, sortBy);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Utente updateUtente(@PathVariable int id, @RequestBody
                               @Validated UtenteDto utenteDto,
                               BindingResult bindingResult) throws NotFoundException, ValidationException {

        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }
        return utenteService.updateUtente(id, utenteDto);
    }

    @PatchMapping("/{id}")
    public String patchUtente(@PathVariable int id, @RequestBody MultipartFile file)
            throws NotFoundException, IOException {
        return utenteService.patchUtente(id, file);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteUtente(@PathVariable int id) throws NotFoundException {
        utenteService.deleteUtente(id);
    }
}
