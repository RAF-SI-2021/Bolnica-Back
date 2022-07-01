package raf.si.bolnica.management.response;

import lombok.Data;
import raf.si.bolnica.management.entities.ZakazaniTerminPrijema;
import raf.si.bolnica.management.entities.enums.StatusTermina;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ZakazaniTerminPrijemaDto {

    private long zakazaniTerminPrijemaId;
    private long odeljenjeId;
    private UUID lbpPacijenta;
    private UUID lbzZaposlenog;
    private Timestamp datumVremePrijema;
    private StatusTermina statusTermina;
    private String napomena;

    public ZakazaniTerminPrijemaDto(ZakazaniTerminPrijema prijem) {
        this.zakazaniTerminPrijemaId = prijem.getZakazaniTerminPrijemaId();
        this.odeljenjeId = prijem.getOdeljenjeId();
        this.lbpPacijenta = prijem.getLbpPacijenta();
        this.lbzZaposlenog = prijem.getLbzZaposlenog();
        this.datumVremePrijema = prijem.getDatumVremePrijema();
        this.statusTermina = prijem.getStatusTermina();
        this.napomena = prijem.getNapomena();
    }

}
