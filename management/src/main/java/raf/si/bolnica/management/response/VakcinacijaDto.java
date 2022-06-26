package raf.si.bolnica.management.response;

import lombok.Data;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.entities.Vakcinacija;

import java.util.Date;

@Data
public class VakcinacijaDto {

    private Long id;
    private Vakcina vakcina;
    private Date datumVakcinacije;
    private Long zdravstveniKartonId;

    public VakcinacijaDto(Vakcinacija vakcinacija, Long id) {
        this.id = vakcinacija.getId();
        this.vakcina = vakcinacija.getVakcina();
        this.datumVakcinacije = vakcinacija.getDatumVakcinacije();
        this.zdravstveniKartonId = id;
    }
}
