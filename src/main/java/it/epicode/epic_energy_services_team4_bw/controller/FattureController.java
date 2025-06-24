package it.epicode.epic_energy_services_team4_bw.controller;

import it.epicode.epic_energy_services_team4_bw.dto.FattureDto;
import it.epicode.epic_energy_services_team4_bw.enums.StatoFattura;
import it.epicode.epic_energy_services_team4_bw.exception.BadRequestException;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.service.FattureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/fatture")
public class FattureController {

    @Autowired
    private FattureService fattureService;


    /**
     * GET /api/fatture
     * Recupera tutte le fatture paginate.
     * Esempio: /api/fatture?page=0&size=10&sort=id,asc
     */
    @GetMapping
    public ResponseEntity<Page<FattureDto>> getAllFatture(Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findAll(pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/{id}
     * Recupera una singola fattura per ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FattureDto> getFatturaById(@PathVariable Long id) throws NotFoundException {
        FattureDto fattura = fattureService.findById(id);
        return new ResponseEntity<>(fattura, HttpStatus.OK);
    }

    /**
     * POST /api/fatture
     * Crea una nuova fattura.
     */
    @PostMapping
    public ResponseEntity<FattureDto> createFattura(@RequestBody @Valid FattureDto fatturaDto) throws NotFoundException, BadRequestException {
        FattureDto savedFattura = fattureService.save(fatturaDto);
        return new ResponseEntity<>(savedFattura, HttpStatus.CREATED);
    }

    /**
     * PUT /api/fatture/{id}
     * Aggiorna una fattura esistente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FattureDto> updateFattura(@PathVariable Long id, @RequestBody @Valid FattureDto fatturaDto) throws NotFoundException, BadRequestException {
        FattureDto updatedFattura = fattureService.update(id, fatturaDto);
        return new ResponseEntity<>(updatedFattura, HttpStatus.OK);
    }

    /**
     * DELETE /api/fatture/{id}
     * Elimina una fattura.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFattura(@PathVariable Long id) throws NotFoundException {
        fattureService.delete(id);
        return new ResponseEntity<>("Fattura con id=" + id + " eliminata con successo.", HttpStatus.NO_CONTENT);
    }


    /**
     * GET /api/fatture/byStato
     * Filtra fatture per stato.
     * Esempio: /api/fatture/byStato?stato=PAGATA
     */
    @GetMapping("/byStato")
    public ResponseEntity<Page<FattureDto>> getFattureByStato(@RequestParam StatoFattura stato, Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByStato(stato, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/byData
     * Filtra fatture per data.
     * Esempio: /api/fatture/byData?data=2023-01-15
     */
    @GetMapping("/byData")
    public ResponseEntity<Page<FattureDto>> getFattureByData(@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate data, Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByData(data, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/byAnno
     * Filtra fatture per anno.
     * Esempio: /api/fatture/byAnno?anno=2023
     */
    @GetMapping("/byAnno")
    public ResponseEntity<Page<FattureDto>> getFattureByAnno(@RequestParam int anno, Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByAnno(anno, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/byImportoBetween
     * Filtra fatture per intervallo di importo.
     * Esempio: /api/fatture/byImportoBetween?min=100.00&max=500.00
     */
    @GetMapping("/byImportoBetween")
    public ResponseEntity<Page<FattureDto>> getFattureByImportoBetween(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByImportoBetween(min, max, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/byClienteAndStato
     * Filtra fatture per ID cliente e stato.
     * Esempio: /api/fatture/byClienteAndStato?clienteId=1&stato=NON_PAGATA
     */
    @GetMapping("/byClienteAndStato")
    public ResponseEntity<Page<FattureDto>> getFattureByClienteAndStato(
            @RequestParam Long clienteId,
            @RequestParam StatoFattura stato,
            Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByClienteIdAndStato(clienteId, stato, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/byClienteAndData
     * Filtra fatture per ID cliente e data.
     * Esempio: /api/fatture/byClienteAndData?clienteId=1&data=2023-03-01
     */
    @GetMapping("/byClienteAndData")
    public ResponseEntity<Page<FattureDto>> getFattureByClienteAndData(
            @RequestParam Long clienteId,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate data,
            Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByClienteIdAndData(clienteId, data, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }

    /**
     * GET /api/fatture/byStatoAndData
     * Filtra fatture per stato e data.
     * Esempio: /api/fatture/byStatoAndData?stato=PAGATA&data=2023-04-20
     */
    @GetMapping("/byStatoAndData")
    public ResponseEntity<Page<FattureDto>> getFattureByStatoAndData(
            @RequestParam StatoFattura stato,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate data,
            Pageable pageable) {
        Page<FattureDto> fatture = fattureService.findByStatoAndData(stato, data, pageable);
        return new ResponseEntity<>(fatture, HttpStatus.OK);
    }
}
