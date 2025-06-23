package it.epicode.epic_energy_services_team4_bw.model;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private String message;
    private LocalDateTime dataErrore;
}

