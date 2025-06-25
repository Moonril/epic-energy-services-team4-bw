package it.epicode.epic_energy_services_team4_bw.controller;

import it.epicode.epic_energy_services_team4_bw.dto.ProvinciaDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.exception.ValidationException;
import it.epicode.epic_energy_services_team4_bw.model.Provincia;
import it.epicode.epic_energy_services_team4_bw.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/province")
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Provincia> getAllProvince(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        return provinciaService.getAllProvince(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Provincia getProvinciaById(@PathVariable int id) throws NotFoundException {
        return provinciaService.getProvincia(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Provincia saveProvincia(@RequestBody @Validated ProvinciaDto provinciaDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return provinciaService.saveProvincia(provinciaDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Provincia updateProvincia(@PathVariable int id, @RequestBody @Validated ProvinciaDto provinciaDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return provinciaService.updateProvincia(id, provinciaDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteProvincia(@PathVariable int id) throws NotFoundException {
        provinciaService.deleteProvincia(id);
    }


    @PostMapping("/import")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String importaProvince() {
        provinciaService.importaProvince();
        return "Province importate!";
    }
}
