package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProvinciaRepository extends JpaRepository<Provincia, Integer>, PagingAndSortingRepository<Provincia, Integer> {
}
