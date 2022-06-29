package raf.si.bolnica.laboratory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UputHistoryRequestDTO {
    private String lbp;
    private Timestamp odDatuma;
    private Timestamp doDatuma;
}
