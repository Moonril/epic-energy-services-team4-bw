package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.ProvinciaDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Provincia;
import it.epicode.epic_energy_services_team4_bw.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public Provincia saveProvincia(ProvinciaDto provinciaDto) throws NotFoundException {

        Provincia provincia = new Provincia();

        provincia.setNome(provinciaDto.getNome());
        provincia.setSigla(provinciaDto.getSigla());
        provincia.setRegione(provinciaDto.getRegione());

        return provinciaRepository.save(provincia);
    }

    public Page<Provincia> getAllProvince(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return provinciaRepository.findAll(pageable);
    }

    public Provincia getProvincia(int id) throws NotFoundException{
        return provinciaRepository.findById(id).orElseThrow(() -> new NotFoundException("Provincia con id: " + id + " non trovata"));
    }

    public Provincia updateProvincia(int id, ProvinciaDto provinciaDto) throws NotFoundException{
        Provincia provinciaDaAggiornare = getProvincia(id);

        provinciaDaAggiornare.setNome(provinciaDto.getNome());
        provinciaDaAggiornare.setSigla(provinciaDto.getSigla());
        provinciaDaAggiornare.setRegione(provinciaDto.getRegione());


        return provinciaRepository.save(provinciaDaAggiornare);
    }

    public void deleteProvincia(int id) throws NotFoundException{
        Provincia provinciaDaCancellare = getProvincia(id);

        provinciaRepository.delete(provinciaDaCancellare);
    }
}
