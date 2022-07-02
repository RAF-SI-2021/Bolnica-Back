package raf.si.bolnica.management.response;

import lombok.Getter;
import raf.si.bolnica.management.entities.AlergenZdravstveniKarton;
import raf.si.bolnica.management.entities.Vakcinacija;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ZdravstveniKartonResponseDTO {

    private final Long zdravstveniKartonId;

    private final Date datumRegistracije;

    private final KrvnaGrupa krvnaGrupa;

    private final RhFaktor rhFaktor;

    private Boolean obrisan = false;

    private final PacijentResponseDTO pacijent;

    private final Set<AlegrenZdravstveniKartonDto> alergeni;

    private final Set<VakcinacijaDto> vakcinacije;

    public ZdravstveniKartonResponseDTO(ZdravstveniKarton zdravstveniKarton) {
        zdravstveniKartonId = zdravstveniKarton.getZdravstveniKartonId();
        datumRegistracije = zdravstveniKarton.getDatumRegistracije();
        obrisan = zdravstveniKarton.getObrisan();
        rhFaktor = zdravstveniKarton.getRhFaktor();
        krvnaGrupa = zdravstveniKarton.getKrvnaGrupa();
        pacijent = new PacijentResponseDTO(zdravstveniKarton.getPacijent());
        Set<AlegrenZdravstveniKartonDto> noviAlergeni = new HashSet<>();
        for (AlergenZdravstveniKarton alergenZdravstveniKarton : zdravstveniKarton.getAlergenZdravstveniKarton()) {
            noviAlergeni.add(new AlegrenZdravstveniKartonDto(alergenZdravstveniKarton, zdravstveniKarton.getZdravstveniKartonId()));
        }
        this.alergeni = noviAlergeni;
        Set<VakcinacijaDto> noveVakcinacije = new HashSet<>();
        for (Vakcinacija vakcinacija : zdravstveniKarton.getVakcinacije()) {
            noveVakcinacije.add(new VakcinacijaDto(vakcinacija, zdravstveniKarton.getZdravstveniKartonId()));
        }
        this.vakcinacije = noveVakcinacije;
    }
}
