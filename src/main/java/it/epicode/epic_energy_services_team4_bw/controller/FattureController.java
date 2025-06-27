package it.epicode.epic_energy_services_team4_bw.controller;

import it.epicode.epic_energy_services_team4_bw.dto.FattureDto;
import it.epicode.epic_energy_services_team4_bw.enums.StatoFattura;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Fatture;
import it.epicode.epic_energy_services_team4_bw.service.FattureService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/fatture")
public class FattureController {

    @Autowired
    private FattureService fattureService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Fatture> getAllFatture(Pageable pageable) {
        return fattureService.getAllFatture(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Fatture getFatturaById(@PathVariable int id) throws NotFoundException {
        return fattureService.getFatturaById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Fatture saveFattura(@RequestBody @Validated FattureDto fattureDto, BindingResult bindingResult) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).reduce("", (s1, s2) -> s1 + s2));
        }
        return fattureService.saveFattura(fattureDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Fatture updateFattura(@PathVariable int id, @RequestBody @Validated FattureDto fattureDto, BindingResult bindingResult) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).reduce("", (s1, s2) -> s1 + s2));
        }
        return fattureService.updateFattura(id, fattureDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteFattura(@PathVariable int id) throws NotFoundException {
        fattureService.deleteFattura(id);
    }

    // --- Endpoint di Filtro ---

    //FILTRO PER CLIENTE
    @GetMapping("/filtro/{clienteId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Fatture> getFattureByClienteId(@PathVariable int clienteId,Pageable pageable){
        return fattureService.findByClienteId(clienteId,pageable);
    }

    //FILTRO PER STATO
    @GetMapping("/filtro/stato")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Fatture> getFattureByStato(@RequestParam StatoFattura stato, Pageable pageable) {
        return fattureService.findByStato(stato, pageable);
    }

    //FILTRO PER DATA FATTURA
    @GetMapping("/filtro/data")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Fatture> getFattureByData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, Pageable pageable) {
        return fattureService.findByData(data, pageable);
    }

    //FILTRO PER ANNO
    @GetMapping("/filtro/anno")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Fatture> getFattureByAnno(@RequestParam int anno, Pageable pageable) {
        return fattureService.findByAnno(anno, pageable);
    }

    //FILTRO PER RANGE DI IMPORTI
    @GetMapping("/filtro/importo")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Fatture> getFattureByImportoBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max, Pageable pageable) {
        return fattureService.findByImportoBetween(min, max, pageable);
    }
}