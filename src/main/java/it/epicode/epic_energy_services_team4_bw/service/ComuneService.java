package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.ComuneDto;
import it.epicode.epic_energy_services_team4_bw.dto.ProvinciaDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Comune;
import it.epicode.epic_energy_services_team4_bw.model.Provincia;
import it.epicode.epic_energy_services_team4_bw.repository.ComuneRepository;
import it.epicode.epic_energy_services_team4_bw.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private ProvinciaRepository provinciaRepository;

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


    //test import comuni test
    public void importaComuni() {
        try {
            String contenuto = Files.readString(Path.of(getClass().getClassLoader().getResource("fileImport/comuni-italiani.csv").toURI()));

            String[] righe = contenuto.split("\n");

            for (int i = 1; i < righe.length; i++) {
                String riga = righe[i].trim();

                if (riga.isEmpty()) continue;

                String[] colonne = riga.split(";");

                if (colonne.length < 2) continue;

                String nomeComune = colonne[0].trim();
                String siglaProvincia = colonne[1].trim();

                try {
                    int provinciaId = Integer.parseInt(siglaProvincia);

                    Provincia provincia = provinciaRepository.findById(provinciaId).orElse(null);
                    if (provincia == null) {
                        System.out.println("Provincia con ID " + provinciaId + " non trovata. Riga: " + riga);
                        continue;
                    }

                    Comune comune = new Comune();
                    comune.setNome(nomeComune);
                    comune.setProvincia(provincia);

                    comuneRepository.save(comune);

                } catch (NumberFormatException e) {
                    System.out.println("ID provincia non valido: " + siglaProvincia + " nella riga: " + riga);
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
