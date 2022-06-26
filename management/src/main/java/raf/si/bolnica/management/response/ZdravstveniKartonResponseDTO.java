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

    private Long zdravstveniKartonId;

    private Date datumRegistracije;

    private KrvnaGrupa krvnaGrupa;

    private RhFaktor rhFaktor;

    private Boolean obrisan = false;

    private PacijentResponseDTO pacijent;

    private Set<AlegrenZdravstveniKartonDto> alergeni;

    private Set<VakcinacijaDto> vakcinacije;

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
