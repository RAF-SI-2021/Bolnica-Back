package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ParametarSaAnalizomResponseDTO {
    private RezultatParametraAnalizeResponseDTO rezultat;
    private ParametarResponseDTO parametar;
    private LaboratorijskaAnalizaResponseDTO analiza;
}
