package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import it.epicode.epic_energy_services_team4_bw.model.Comune;
import it.epicode.epic_energy_services_team4_bw.model.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,Integer>, JpaSpecificationExecutor<Cliente>,
        PagingAndSortingRepository<Cliente, Integer>
{
    Optional<Cliente> findByPartitaIva(String partitaIva);

    List<Cliente> findByFatturatoAnnualeGreaterThanEqual(BigDecimal fatturatoMin);
    List<Cliente> findByDataInserimentoAfter(LocalDate data);
    List<Cliente> findByDataUltimoContattoAfter(LocalDate data);
    List<Cliente> findByNomeContainingIgnoreCase(String parteNome);



}
