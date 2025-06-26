package it.epicode.epic_energy_services_team4_bw.controller;



import it.epicode.epic_energy_services_team4_bw.dto.IndirizzoDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import it.epicode.epic_energy_services_team4_bw.service.IndirizzoService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/indirizzi")
public class IndirizzoController {
    @Autowired
    private IndirizzoService indirizzoService;


        @PostMapping("/cliente/{clienteId}")
        @ResponseStatus(HttpStatus.CREATED)
        @PreAuthorize("hasAuthority('ADMIN')")
        public Indirizzo saveIndirizzoPerCliente(@PathVariable int clienteId, @RequestBody @Validated IndirizzoDto indirizzoDto, BindingResult bindingResult) throws NotFoundException {
            if(bindingResult.hasErrors()){
                throw new ValidationException(bindingResult.getAllErrors().stream()
                        .map(objectError -> objectError.getDefaultMessage()).reduce("",(e,s)->e+s));
            }
            return indirizzoService.saveIndirizzo(clienteId, indirizzoDto);
        }

        @GetMapping
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public Page<Indirizzo> getAllIndirizzi(Pageable pageable) {
            return indirizzoService.getAllIndirizzi(pageable);
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public Indirizzo getIndirizzo(@PathVariable int id) throws NotFoundException {
            return indirizzoService.getIndirizzo(id);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public Indirizzo updateIndirizzo(@PathVariable int id, @RequestBody @Validated IndirizzoDto indirizzoDto, BindingResult bindingResult) throws NotFoundException {
            if(bindingResult.hasErrors()){
                throw new ValidationException(bindingResult.getAllErrors().stream()
                        .map(objectError -> objectError.getDefaultMessage()).reduce("",(e,s)->e+s));
            }
            return indirizzoService.updateIndirizzo(id, indirizzoDto);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @PreAuthorize("hasAuthority('ADMIN')")
        public void deleteIndirizzo(@PathVariable int id) throws NotFoundException {
            indirizzoService.deleteIndirizzo(id);
        }
}

