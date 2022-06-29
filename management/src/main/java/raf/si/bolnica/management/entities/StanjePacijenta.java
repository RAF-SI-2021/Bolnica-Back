package raf.si.bolnica.management.entities;
import lombok.NoArgsConstructor;
import raf.si.bolnica.management.requests.SetPatientsStateDTO;
import org.hibernate.annotations.Type;

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
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID lbpPacijenta;

    @Column(nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID lbzRegistratora;

    @Column(nullable = false)
    private Timestamp datumVreme;

    private String temperatura;

    private String krvniPritisak;

    private String puls;

    private String primenjeneTerapije;

    private String opis;

    public long getStanjePacijentaId() {
        return stanjePacijentaId;
    }

    public void setStanjePacijentaId(long stanjePacijentaId) {
        this.stanjePacijentaId = stanjePacijentaId;
    }

    public UUID getLbpPacijenta() {
        return lbpPacijenta;
    }

    public void setLbpPacijenta(UUID lbpPacijenta) {
        this.lbpPacijenta = lbpPacijenta;
    }

    public UUID getLbzRegistratora() {
        return lbzRegistratora;
    }

    public void setLbzRegistratora(UUID lbzRegistratora) {
        this.lbzRegistratora = lbzRegistratora;
    }

    public Timestamp getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Timestamp datumVreme) {
        this.datumVreme = datumVreme;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getKrvniPritisak() {
        return krvniPritisak;
    }

    public void setKrvniPritisak(String krvniPritisak) {
        this.krvniPritisak = krvniPritisak;
    }

    public String getPuls() {
        return puls;
    }

    public void setPuls(String puls) {
        this.puls = puls;
    }

    public String getPrimenjeneTerapije() {
        return primenjeneTerapije;
    }

    public void setPrimenjeneTerapije(String primenjeneTerapije) {
        this.primenjeneTerapije = primenjeneTerapije;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public StanjePacijenta(SetPatientsStateDTO dto){
        this.lbpPacijenta = dto.getLbpPacijenta();
        this.lbzRegistratora = dto.getLbzRegistratora();
        this.temperatura = dto.getTemperatura();
        this.krvniPritisak = dto.getKrvniPritisak();
        this.puls   =    dto.getPuls();
        this.primenjeneTerapije = dto.getPrimenjeneTerapije();
        this.opis = dto.getOpis();
        this.datumVreme = dto.getDatumVreme();

    }
}
