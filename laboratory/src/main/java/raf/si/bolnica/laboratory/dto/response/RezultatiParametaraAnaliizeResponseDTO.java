package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RezultatiParametaraAnaliizeResponseDTO {
    private LaboratorijskiRadniNalogResponseDTO nalog;
    private List<ParametarSaAnalizomResponseDTO> rezultatiAnaliza;
}
