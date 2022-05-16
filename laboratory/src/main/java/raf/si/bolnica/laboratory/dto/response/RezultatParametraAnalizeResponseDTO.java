package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RezultatParametraAnalizeResponseDTO {
    private String rezultat;
    private Timestamp datumVreme;
    private UUID lbz;

    public RezultatParametraAnalizeResponseDTO(RezultatParametraAnalize rezultatParametraAnalize) {
        this.rezultat = rezultatParametraAnalize.getRezultat();
        this.datumVreme = rezultatParametraAnalize.getDatumVreme();
        this.lbz =rezultatParametraAnalize.getLbz();
    }
}
