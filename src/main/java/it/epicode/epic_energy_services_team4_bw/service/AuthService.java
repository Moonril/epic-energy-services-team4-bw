package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.dto.LoginDto;
import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.model.Utente;
import it.epicode.epic_energy_services_team4_bw.repository.UtenteRepository;
import it.epicode.epic_energy_services_team4_bw.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login(LoginDto loginDto) throws NotFoundException {
        Utente utente = utenteRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                ()->new NotFoundException("Utente con username/password non trovato"));
        if (passwordEncoder.matches(loginDto.getPassword(), utente.getPassword())){
            return jwtTool.createToken(utente);
        }else {
            throw new NotFoundException("Utente con username/password non trovato");
        }


    }
}
