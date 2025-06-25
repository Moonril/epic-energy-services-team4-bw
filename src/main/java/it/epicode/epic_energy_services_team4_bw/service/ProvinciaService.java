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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

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


    //import file province test
    public void importaProvince() {
        try {
            String contenuto = Files.readString(Path.of(getClass().getClassLoader().getResource("fileImport/province-italiane.csv").toURI()));

            String[] righe = contenuto.split("\n");

            for (int i = 1; i < righe.length; i++) { // salta intestazione
                String riga = righe[i].trim();

                if (riga.isEmpty()) continue;

                String[] colonne = riga.split(";");

                if (colonne.length < 3) continue;

                String sigla = colonne[0].trim();
                String nome = colonne[1].trim();
                String regione = colonne[2].trim();

                Provincia provincia = new Provincia();
                provincia.setSigla(sigla);
                provincia.setNome(nome);
                provincia.setRegione(regione);

                provinciaRepository.save(provincia);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

