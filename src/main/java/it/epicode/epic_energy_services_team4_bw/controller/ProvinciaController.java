package it.epicode.epic_energy_services_team4_bw.controller;

import it.epicode.epic_energy_services_team4_bw.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/provincia")
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;
}
