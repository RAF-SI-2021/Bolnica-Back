package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;
import raf.si.bolnica.management.entities.enums.StatusTermina;

@Getter
@Setter
public class UpdateZakazaniTerminPrijemaStatusDTO {

    private long id;
    private StatusTermina status;

}
