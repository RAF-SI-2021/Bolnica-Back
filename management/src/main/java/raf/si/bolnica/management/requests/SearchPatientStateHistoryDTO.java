package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
public class SearchPatientStateHistoryDTO {
    private UUID lbp;
    private Timestamp odDatuma;
    private Timestamp doDatuma;
}
