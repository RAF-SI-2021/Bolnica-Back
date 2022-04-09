package raf.si.bolnica.management.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;

import javax.persistence.Column;
import java.sql.Date;
import java.util.UUID;

@Data
public class CreatePregledReportRequestDTO {

    private UUID lbp;

    private long zaposleniId = -1;

    private Boolean indikatorPoverljivosti;

    private String glavneTegobe;

    private String sadasnjaBolest;

    private String licnaAnamneza;

    private String porodicnaAnamneza;

    private String misljenjePacijenta;

    private String objektivniNalaz;

    private String dijagnoza;

    private String predlozenaTerapija;

    private String savet;

    private RezultatLecenja rezultatLecenja;

    private String opisTekucegStanja;
}
