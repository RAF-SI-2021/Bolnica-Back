package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.Parametar;
import raf.si.bolnica.laboratory.entities.enums.TipVrednosti;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParametarResponseDTO {

    private long parametarId;

    private String nazivParametra;

    private TipVrednosti tipVrednosti;

    private String jedinicaMere;

    private double donjaGranica;

    private double gornjaGranica;

    public ParametarResponseDTO(Parametar parametar) {
        this.parametarId = parametar.getParametarId();
        this.nazivParametra = parametar.getNazivParametra();
        this.tipVrednosti = parametar.getTipVrednosti();
        this.jedinicaMere = parametar.getJedinicaMere();
        this.donjaGranica = parametar.getDonjaGranica();
        this.gornjaGranica = parametar.getGornjaGranica();
    }
}
