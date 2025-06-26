package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.enums.TipoSede;
import it.epicode.epic_energy_services_team4_bw.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,Integer>, JpaSpecificationExecutor<Cliente>,
        PagingAndSortingRepository<Cliente, Integer>
{
    Optional<Cliente> findByPartitaIva(String partitaIva);
    @Query("""
    SELECT c FROM Cliente c
    JOIN c.indirizzi i
    WHERE i.tipoSede = :tipo
    ORDER BY i.comune.provincia.nome
""")
    Page<Cliente> findAllOrderByProvinciaSedeLegale(@Param("tipo") TipoSede tipo, Pageable pageable);

    List<Cliente> findByFatturatoAnnualeGreaterThanEqual(BigDecimal fatturatoMin);
    List<Cliente> findByDataInserimentoAfter(LocalDate data);
    List<Cliente> findByDataUltimoContattoAfter(LocalDate data);
    List<Cliente> findByRagioneSocialeContainingIgnoreCase(String parteNome);



}
