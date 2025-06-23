package it.epicode.epic_energy_services_team4_bw.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
