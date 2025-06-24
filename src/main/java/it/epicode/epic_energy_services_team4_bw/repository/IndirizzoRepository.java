package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.model.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface IndirizzoRepository extends JpaRepository <Indirizzo, Integer>, PagingAndSortingRepository <Indirizzo, Integer> {
    Optional<Indirizzo> findById(int id);
}
