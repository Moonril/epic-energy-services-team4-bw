package it.epicode.epic_energy_services_team4_bw.model;

import it.epicode.epic_energy_services_team4_bw.enums.TipoCliente;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clienti")
public class Cliente {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false, name = "ragione_sociale")
    private String ragioneSociale;
    @Column(nullable = false, unique = true, name = "partita_Iva")
    private String partitaIva;
    @Column(unique = true)
    private String email;
    @Column(nullable = false, name = "data_inserimento")
    private LocalDate dataInserimento;
    @Column(name = "data_ultimo_contatto")
    private LocalDate dataUltimoContatto;
    @Column(name = "fatturato_annuale",precision = 19,scale = 2)
    private BigDecimal fatturatoAnnuale;
    private String telefono;
    @Column(name = "email_contatto")
    private String emailContatto;
    private String nome;
    private String cognome;
    @Column(name = "logo_url")
    private String logoAziendale;
    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;
    @OneToMany(mappedBy = "cliente")
    private List<Indirizzo> indirizzi;
    @OneToMany(mappedBy = "cliente")
    private List<Fatture> fatture=new ArrayList<>();




}
