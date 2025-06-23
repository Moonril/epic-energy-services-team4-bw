package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,Integer>, JpaSpecificationExecutor<Cliente> {
    Optional<Cliente> findByPartitaIva(String partitaIva);

}
