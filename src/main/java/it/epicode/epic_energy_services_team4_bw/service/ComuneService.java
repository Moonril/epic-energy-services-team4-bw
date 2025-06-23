package it.epicode.epic_energy_services_team4_bw.service;

import it.epicode.epic_energy_services_team4_bw.model.Comune;
import it.epicode.epic_energy_services_team4_bw.repository.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;
}
