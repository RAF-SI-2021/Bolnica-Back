package raf.si.bolnica.laboratory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaboratorijskiRadniNalogSearchRequestDTO {
    private UUID lbp;
    private Timestamp odDatuma;
    private Timestamp doDatuma;
    private StatusObrade statusObrade;
}
