package raf.si.bolnica.management.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.Operacija;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZdravstveniKartonResponseDTO {

    private Date datumRegistracije;

    private KrvnaGrupa krvnaGrupa;

    private RhFaktor rhFaktor;

    private Boolean obrisan = false;

    private PacijentResponseDTO pacijent;

    public ZdravstveniKartonResponseDTO(ZdravstveniKarton zdravstveniKarton) {
        datumRegistracije = zdravstveniKarton.getDatumRegistracije();
        obrisan = zdravstveniKarton.getObrisan();
        rhFaktor = zdravstveniKarton.getRhFaktor();
        krvnaGrupa = zdravstveniKarton.getKrvnaGrupa();
        pacijent = new PacijentResponseDTO(zdravstveniKarton.getPacijent());
    }
}
