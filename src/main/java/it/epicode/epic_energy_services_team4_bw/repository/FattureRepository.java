package it.epicode.epic_energy_services_team4_bw.repository;

import it.epicode.epic_energy_services_team4_bw.enums.StatoFattura;
import it.epicode.epic_energy_services_team4_bw.model.Fatture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FattureRepository extends JpaRepository<Fatture, Integer> {
    Optional<Fatture> findByNumero(String numero);

    Page<Fatture> findByStato(StatoFattura stato, Pageable pageable);

    Page<Fatture> findByData(LocalDate data, Pageable pageable);

    @Query("SELECT f FROM Fatture f WHERE FUNCTION('YEAR', f.data) = :anno")
    Page<Fatture> findByAnno(@Param("anno") int anno, Pageable pageable);

    Page<Fatture> findByImportoBetween(BigDecimal importoMin, BigDecimal importoMax, Pageable pageable);


    Page<Fatture> findByClienteIdAndStato(int clienteId, StatoFattura stato, Pageable pageable);


    Page<Fatture> findByClienteIdAndData(int clienteId, LocalDate data, Pageable pageable);

    Page<Fatture> findByStatoAndData(StatoFattura stato, LocalDate data, Pageable pageable);


    @Query("SELECT COUNT(f) > 0 FROM Fatture f WHERE f.numero = :numero AND (:id IS NULL OR f.id <> :id)")
    boolean existsByNumeroAndIdNot(@Param("numero") String numero, @Param("id") Integer id);
}