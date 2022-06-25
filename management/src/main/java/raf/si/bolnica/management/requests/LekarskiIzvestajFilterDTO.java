package raf.si.bolnica.management.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LekarskiIzvestajFilterDTO {

    private String lbp;
    private Date date;
    private Date end;

}
