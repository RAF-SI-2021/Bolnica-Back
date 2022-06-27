package raf.si.bolnica.laboratory.entities;

import org.hibernate.annotations.Type;
import raf.si.bolnica.laboratory.entities.enums.StatusUputa;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;
import raf.si.bolnica.laboratory.requests.CreateUputDTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Uput {

    @Id
    @Column(name = "uput_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uputId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipUputa tip;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbz;

    @Column(nullable = false)
    private Integer izOdeljenjaId;

    @Column(nullable = false)
    private Integer zaOdeljenjeId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbp;

    @Column(nullable = false)
    private Timestamp datumVremeKreiranja;

    @Enumerated(EnumType.STRING)
    private StatusUputa status = StatusUputa.NEREALIZOVAN;

    private String zahtevaneAnalize;

    private String komentar;

    private String uputnaDijagnoza;

    private String razlogUpucivanja;

    // FKs
    @OneToOne(mappedBy = "uput", fetch = FetchType.EAGER)
    private LaboratorijskiRadniNalog laboratorijskiRadniNalog;

    public Uput(){

    }
    public Uput(CreateUputDTO DTO) {
        this.tip = DTO.getTip();
        this.datumVremeKreiranja=(Timestamp.valueOf(LocalDateTime.now()));
        this.komentar=(DTO.getKomentar());
        this.izOdeljenjaId=(DTO.getIzOdeljenjaId());
        this.razlogUpucivanja=(DTO.getRazlogUpucivanja());
        this.zaOdeljenjeId=(DTO.getZaOdeljenjeId());
        this.lbp=(UUID.fromString(DTO.getLbp()));
        this.lbz=(UUID.fromString(DTO.getLbz()));
        this.zahtevaneAnalize=(DTO.getZahtevaneAnalize());
        this.uputnaDijagnoza=(DTO.getUputnaDijagnoza());
    }

    public long getUputId() {
        return uputId;
    }

    public void setUputId(long uputId) {
        this.uputId = uputId;
    }

    public TipUputa getTip() {
        return tip;
    }

    public void setTip(TipUputa tip) {
        this.tip = tip;
    }

    public UUID getLbz() {
        return lbz;
    }

    public void setLbz(UUID lbz) {
        this.lbz = lbz;
    }

    public Integer getIzOdeljenjaId() {
        return izOdeljenjaId;
    }

    public void setIzOdeljenjaId(Integer izOdeljenjaId) {
        this.izOdeljenjaId = izOdeljenjaId;
    }

    public Integer getZaOdeljenjeId() {
        return zaOdeljenjeId;
    }

    public void setZaOdeljenjeId(Integer zaOdeljenjeId) {
        this.zaOdeljenjeId = zaOdeljenjeId;
    }

    public UUID getLbp() {
        return lbp;
    }

    public void setLbp(UUID lbp) {
        this.lbp = lbp;
    }

    public Timestamp getDatumVremeKreiranja() {
        return datumVremeKreiranja;
    }

    public void setDatumVremeKreiranja(Timestamp datumVremeKreiranja) {
        this.datumVremeKreiranja = datumVremeKreiranja;
    }

    public StatusUputa getStatus() {
        return status;
    }

    public void setStatus(StatusUputa status) {
        this.status = status;
    }

    public String getZahtevaneAnalize() {
        return zahtevaneAnalize;
    }

    public void setZahtevaneAnalize(String zahtevaneAnalize) {
        this.zahtevaneAnalize = zahtevaneAnalize;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getUputnaDijagnoza() {
        return uputnaDijagnoza;
    }

    public void setUputnaDijagnoza(String uputnaDijagnoza) {
        this.uputnaDijagnoza = uputnaDijagnoza;
    }

    public String getRazlogUpucivanja() {
        return razlogUpucivanja;
    }

    public void setRazlogUpucivanja(String razlogUpucivanja) {
        this.razlogUpucivanja = razlogUpucivanja;
    }

    public LaboratorijskiRadniNalog getLaboratorijskiRadniNalog() {
        return laboratorijskiRadniNalog;
    }

    public void setLaboratorijskiRadniNalog(LaboratorijskiRadniNalog laboratorijskiRadniNalog) {
        this.laboratorijskiRadniNalog = laboratorijskiRadniNalog;
    }
}
