package it.epicode.epic_energy_services_team4_bw.model;

import it.epicode.epic_energy_services_team4_bw.enums.StatoFattura;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="fatture")
@Data
public class Fatture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false,precision = 10, scale = 2)
    private BigDecimal importo;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoFattura stato;

    public Fatture() {
    }

    public Fatture(LocalDate data, BigDecimal importo, String numero, Cliente cliente, StatoFattura stato) {
        this.data = data;
        this.importo = importo;
        this.numero = numero;
        this.cliente = cliente;
        this.stato = stato;
    }
}
