package raf.si.bolnica.management.entities;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import raf.si.bolnica.management.requests.SetPatientsStateDTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class StanjePacijenta {

    @Id
    @Column(name = "stanje_pacijenta_ud")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stanjePacijentaId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbpPacijenta;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzRegistratora;

    @Column(nullable = false)
    private Timestamp datumVreme;

    private String temperatura;

    private String krvniPritisak;

    private String puls;

    private String primenjeneTerapije;

    private String opis;

    public StanjePacijenta(SetPatientsStateDTO dto) {
        this.lbpPacijenta = dto.getLbpPacijenta();
        this.lbzRegistratora = dto.getLbzRegistratora();
        this.temperatura = dto.getTemperatura();
        this.krvniPritisak = dto.getKrvniPritisak();
        this.puls = dto.getPuls();
        this.primenjeneTerapije = dto.getPrimenjeneTerapije();
        this.opis = dto.getOpis();
        this.datumVreme = dto.getDatumVreme();

    }
}
