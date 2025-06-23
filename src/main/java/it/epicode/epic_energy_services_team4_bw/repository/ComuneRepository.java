package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.model.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ComuneRepository extends JpaRepository<Comune, Integer>, PagingAndSortingRepository<Comune, Integer> {
}
