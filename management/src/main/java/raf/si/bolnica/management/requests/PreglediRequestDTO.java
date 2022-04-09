package raf.si.bolnica.management.requests;

import lombok.Getter;

import java.sql.Date;


@Getter
public class PreglediRequestDTO {

    private Date from;

    private Date to;

    private Date on;

}
