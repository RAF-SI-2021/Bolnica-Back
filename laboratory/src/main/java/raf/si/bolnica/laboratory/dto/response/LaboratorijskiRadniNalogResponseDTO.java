package raf.si.bolnica.laboratory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    private UputResponseDTO uput;

    public LaboratorijskiRadniNalogResponseDTO(LaboratorijskiRadniNalog nalog) {
        this.laboratorijskiRadniNalogId = nalog.getLaboratorijskiRadniNalogId();
        this.lbp = nalog.getLbp();
        this.datumVremeKreiranja = nalog.getDatumVremeKreiranja();
        this.statusObrade = nalog.getStatusObrade();
        this.lbzTehnicar = nalog.getLbzTehnicar();
        this.lbzBiohemicar = nalog.getLbzBiohemicar();
//        this.uput = new UputResponseDTO(nalog.getUput());
    }
}
