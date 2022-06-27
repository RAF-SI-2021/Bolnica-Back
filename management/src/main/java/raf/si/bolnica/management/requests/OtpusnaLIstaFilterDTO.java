package raf.si.bolnica.management.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpusnaLIstaFilterDTO {

    private String lbp;
    private Date start;
    private Date end;

}
