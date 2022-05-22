package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.entities.Parametar;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ParametarSaAnalizomResponseDTO {
    private RezultatParametraAnalizeResponseDTO rezultat;
    private ParametarResponseDTO parametar;
    private LaboratorijskaAnalizaResponseDTO analiza;
}
