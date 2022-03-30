package raf.si.bolnica.management.entities;

import raf.si.bolnica.management.entities.enums.RezultatLecenja;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class IstorijaBolesti {

    @Id
    @Column(name = "bolest_pacijenta_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bolestPacijentaId;
    @Column(nullable = false, length = 3)
    private String dijagnoza;
    private Boolean indikatorPoverljivosti = false;
    @Column(nullable = false)
    private Date datumPocetkaZdravstvenogProblema;
    private Date datumZavrsetkaZdravstvenogProblema;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RezultatLecenja rezultatLecenja;
    @Column(nullable = false)
    private String opisTekucegStanja;
    @Column(nullable = false)
    private Date podatakValidanOd;
    @Column(nullable = false)
    private Date podatakValidanDo;
    @Column(nullable = false)
    private Boolean podaciValidni;
    private Boolean obrisan = false;

}
