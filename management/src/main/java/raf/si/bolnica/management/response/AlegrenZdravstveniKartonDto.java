package raf.si.bolnica.management.response;

import lombok.Data;
import raf.si.bolnica.management.entities.AlergenZdravstveniKarton;

@Data
public class AlegrenZdravstveniKartonDto {

    private Long id;
    private String alergen;
    private Long zdravstveniKartonId;

    public AlegrenZdravstveniKartonDto(AlergenZdravstveniKarton alergenZdravstveniKarton, Long id) {
        this.id = alergenZdravstveniKarton.getId();
        this.alergen = alergenZdravstveniKarton.getAlergen().getNaziv();
        this.zdravstveniKartonId = id;
    }
}
