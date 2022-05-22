package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaboratorijskiRadniNalogResponseDTO {

    private long laboratorijskiRadniNalogId;

    private UUID lbp;

    private Timestamp datumVremeKreiranja;

    private StatusObrade statusObrade = StatusObrade.NEOBRADJEN;

    private UUID lbzTehnicar;

    private UUID lbzBiohemicar;

    public LaboratorijskiRadniNalogResponseDTO(LaboratorijskiRadniNalog nalog) {
        this.laboratorijskiRadniNalogId = nalog.getLaboratorijskiRadniNalogId();
        this.lbp = nalog.getLbp();

        this.datumVremeKreiranja = nalog.getDatumVremeKreiranja();
        this.statusObrade = nalog.getStatusObrade();
        this.lbzTehnicar = nalog.getLbzTehnicar();
        this.lbzBiohemicar = nalog.getLbzBiohemicar();
    }
}
