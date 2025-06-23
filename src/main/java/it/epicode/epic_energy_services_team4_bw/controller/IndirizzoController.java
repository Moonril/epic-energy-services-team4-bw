package it.epicode.epic_energy_services_team4_bw.controller;



import it.epicode.epic_energy_services_team4_bw.dto.IndirizzoDto;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import it.epicode.epic_energy_services_team4_bw.service.IndirizzoService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    //metodo Post
    @PostMapping("")
    @PreAuthorize(("hasAuthority('ADMIN')")) //hasauthority autorizza un solo ruolo ad accedere all'endpoint
    @ResponseStatus(HttpStatus.CREATED)
    //per gestire la validazione, devo aggiungere @Validated al dto e poi il metodo deve gestire anche il
    //parametro di tipo BindingResult che conterrà tutti gli eventuali errori del dto
    public Indirizzo saveIndirizzo(@RequestBody @Validated IndirizzoDto indirizzoDto,
                                     BindingResult bindingResult) throws NotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            //gli errori li prendiamo dal bindingResult ma vengono restituiti come liste di objectError.
            //il costruttore dell'eccezione accetta una stringa e quindi con lo stream trasformiamo la lista in una stringa
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)->e+s));
        }
        return indirizzoService.saveIndirizzo(indirizzoDto);
    }

    //metodo get all

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")//Autorizza l0utilizzo dell'endpoint da parte di piu ruoli
    public Page<Indirizzo> getAllIndirizzi (@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy){
        return indirizzoService.getAllIndirizzi(page, size, sortBy);
    }

    //metodo get singolo

    @GetMapping("/{id}")
    public Indirizzo getIndirizzo(@PathVariable int id) throws NotFoundException {
        return indirizzoService.getIndirizzo(id);
    }

    //metodo put

    @PutMapping("/{id}")
    public Indirizzo updateIndirizzo (@PathVariable int id, @RequestBody @Validated
    IndirizzoDto indirizzoDto, BindingResult bindingResult)
            throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return indirizzoService.updateIndirizzo(id, indirizzoDto);
    }
    //metodo delete
    @DeleteMapping("/{id}")
    public void deleteIndirizzo(@PathVariable int id) throws NotFoundException {
        indirizzoService.deleteIndirizzo(id
        );
    }

}

