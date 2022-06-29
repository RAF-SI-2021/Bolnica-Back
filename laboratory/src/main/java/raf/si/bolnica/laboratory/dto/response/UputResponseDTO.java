package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.Uput;
import raf.si.bolnica.laboratory.entities.enums.StatusUputa;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UputResponseDTO {
    //neophodni parametri
    private Long uputId;
    private TipUputa tip;
    private UUID lbz;
    private Integer izOdeljenjaId;
    private Integer zaOdeljenjeId;
    private UUID lbp;
    private Timestamp datumVremeKreiranja;
    private StatusUputa status;


    //opcioni parametri
    private String zahtevaneAnalize;
    private String komentar;
    private String uputnaDijagnoza;
    private String razlogUpucivanja;

    public UputResponseDTO(Uput uput) {
        this.uputId = uput.getUputId();
        this.tip = uput.getTip();
        this.lbz = uput.getLbz();
        this.izOdeljenjaId = uput.getIzOdeljenjaId();
        this.zaOdeljenjeId = uput.getZaOdeljenjeId();
        this.lbp = uput.getLbp();
        this.datumVremeKreiranja = uput.getDatumVremeKreiranja();
        this.zahtevaneAnalize = uput.getZahtevaneAnalize();
        this.komentar = uput.getKomentar();
        this.uputnaDijagnoza = uput.getUputnaDijagnoza();
        this.razlogUpucivanja = uput.getRazlogUpucivanja();
        this.status = uput.getStatus();
    }
}