package it.epicode.epic_energy_services_team4_bw.service;

import com.cloudinary.Cloudinary;
import it.epicode.epic_energy_services_team4_bw.dto.UtenteDto;
import it.epicode.epic_energy_services_team4_bw.enums.TipoUtente;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.exception.UnauthorizedException;
import it.epicode.epic_energy_services_team4_bw.model.Utente;
import it.epicode.epic_energy_services_team4_bw.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    public Utente saveUtente(UtenteDto utenteDto){
        Utente utente = new Utente();
        utente.setNome(utenteDto.getNome());
        utente.setEmail(utenteDto.getEmail());
        utente.setUsername(utenteDto.getUsername());
        utente.setCognome(utenteDto.getCognome());
        utente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
        utente.setTipoUtente(TipoUtente.USER);

        return utenteRepository.save(utente);
    }

    public Utente getUtente(int id) throws NotFoundException {
        return utenteRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Utente con id "
                        + id + " non trovato"));
    }

    public Page<Utente> getAllUtenti(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return utenteRepository.findAll(pageable);
    }

    public Utente updateUtente(int id, UtenteDto utenteDto)
            throws NotFoundException {

        Utente utenteAutenticato = (Utente) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!utenteAutenticato.getTipoUtente().name().equals("ADMIN") && utenteAutenticato.getId() != id) {
            throw new UnauthorizedException("Non puoi modificare un altro utente.");
        }

        Utente utenteDaAggiornare = getUtente(id);

        utenteDaAggiornare.setNome(utenteDto.getNome());
        utenteDaAggiornare.setEmail(utenteDto.getEmail());
        utenteDaAggiornare.setUsername(utenteDto.getUsername());
        utenteDaAggiornare.setCognome(utenteDto.getCognome());
        if (!passwordEncoder.matches(utenteDto.getPassword(), utenteDaAggiornare.getPassword())){
            utenteDaAggiornare.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
        }


        return utenteRepository.save(utenteDaAggiornare);
    }

    public String patchUtente(int id, MultipartFile file) throws NotFoundException, IOException {
        Utente utenteDaPatchare = getUtente(id);

        String url = (String)cloudinary.uploader().upload(file.getBytes(),
                Collections.emptyMap()).get("url");

        utenteDaPatchare.setAvatar(url);

        utenteRepository.save(utenteDaPatchare);

        return url;
    }


    public void deleteUtente(int id) throws NotFoundException {
        Utente utenteAutenticato = (Utente) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!utenteAutenticato.getTipoUtente().name().equals("ADMIN") && utenteAutenticato.getId() != id) {
            throw new UnauthorizedException("Non puoi eliminare un altro utente.");
        }

        Utente utenteDaEliminare = getUtente(id);

        utenteRepository.delete(utenteDaEliminare);
    }

}
