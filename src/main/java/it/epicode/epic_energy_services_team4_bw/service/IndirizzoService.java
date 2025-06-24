package it.epicode.epic_energy_services_team4_bw.service;

import com.cloudinary.Cloudinary;

import it.epicode.epic_energy_services_team4_bw.dto.IndirizzoDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Comune;
import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import it.epicode.epic_energy_services_team4_bw.repository.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class IndirizzoService {
    @Autowired
    private IndirizzoRepository indirizzoRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    //Metodo Save
    public Indirizzo saveIndirizzo(IndirizzoDto indirizzoDto) throws NotFoundException {
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(indirizzoDto.getVia());
        indirizzo.setCivico(indirizzoDto.getCivico());
        indirizzo.setLocalita(indirizzoDto.getLocalita());
        indirizzo.setCap(indirizzoDto.getCap());
        Comune comune = comuneService.getComune(indirizzoDto.getComuneId());

        indirizzo.setComune(comune);
        //setto la mia email per farmi arrivare la conferma
//        sendMail("lucaferr95@gmail.com");

        return indirizzoRepository.save(indirizzo);
    }

    //Metodo getAll con paginazione

    public Page<Indirizzo> getAllIndirizzi(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return indirizzoRepository.findAll(pageable);
    }

    //Metodo get indirizzo

    public Indirizzo getIndirizzo(int id) throws NotFoundException {
        return indirizzoRepository.findById(id).orElseThrow(() -> new NotFoundException("Indirizzo con id: " + id + " non trovato"));

    }
    //Metodo update
    public Indirizzo updateIndirizzo(int id, IndirizzoDto indirizzoDto) throws NotFoundException {
        Indirizzo indirizzoDaAggiornare = getIndirizzo(id);
        return indirizzoRepository.save(indirizzoDaAggiornare);

    }

    //metodo delete indirizzo
    public void deleteIndirizzo(int id) throws NotFoundException {
        Indirizzo indirizzoDaRimuovere = getIndirizzo(id);
        indirizzoRepository.delete(indirizzoDaRimuovere);
    }
    //invio email
    private void sendMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registrazione servizio rest");
        message.setText("Registrazione al servizio rest avvenuta con successo, SIUUUUMMMMM");

        javaMailSender.send(message);
    }
}