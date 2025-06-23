package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer>,
        PagingAndSortingRepository<Utente, Integer> {
    public Optional<Utente> findByUsername(String username);
}
