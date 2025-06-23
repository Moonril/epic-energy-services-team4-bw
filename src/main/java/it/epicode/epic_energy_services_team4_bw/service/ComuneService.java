package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.ComuneDto;
import it.epicode.epic_energy_services_team4_bw.dto.ProvinciaDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Comune;
import it.epicode.epic_energy_services_team4_bw.model.Provincia;
import it.epicode.epic_energy_services_team4_bw.repository.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaService provinciaService;

    public Comune saveComune(ComuneDto comuneDto) throws NotFoundException {
        Provincia provincia = provinciaService.getProvincia(comuneDto.getProvinciaId());
        Comune comune = new Comune();

        comune.setNome(comuneDto.getNome());
        comune.setProvincia(provincia);

        return comuneRepository.save(comune);
    }


    public Page<Comune> getAllComuni(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return comuneRepository.findAll(pageable);
    }

    public Comune getComune(int id) throws NotFoundException{
        return comuneRepository.findById(id).orElseThrow(() -> new NotFoundException("Comune con id: " + id + " non trovato"));
    }

    public Comune updateComune(int id, ComuneDto comuneDto) throws NotFoundException{
        Comune comuneDaAggiornare = getComune(id);

        comuneDaAggiornare.setNome(comuneDto.getNome());

        if(comuneDaAggiornare.getProvincia().getId()!=comuneDto.getProvinciaId()){
            Provincia provincia = provinciaService.getProvincia(comuneDto.getProvinciaId());
            comuneDaAggiornare.setProvincia(provincia);
        }

        return comuneRepository.save(comuneDaAggiornare);
    }

    public void deleteComune(int id) throws NotFoundException{
        Comune comuneDaCancellare = getComune(id);

        comuneRepository.delete(comuneDaCancellare);
    }
}
