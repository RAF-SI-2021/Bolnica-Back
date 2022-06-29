package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaboratorijskaAnalizaResponseDTO {
    private String nazivAnalize;
    private String skracenica;

    public LaboratorijskaAnalizaResponseDTO(LaboratorijskaAnaliza analiza) {
        this.nazivAnalize = analiza.getNazivAnalize();
        this.skracenica = analiza.getSkracenica();
    }
}
