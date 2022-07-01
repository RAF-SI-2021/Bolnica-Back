package raf.si.bolnica.management.response;

import lombok.Getter;
import raf.si.bolnica.management.entities.Pregled;

import java.sql.Date;

@Getter
public class PregledResponseDTO {

    private final Date datumPregleda;

    private final Boolean indikatorPoverljivosti;

    private final String glavneTegobe;

    private final String sadasnjaBolest;

    private final String licnaAnamneza;

    private final String porodicnaAnamneza;

    private final String misljenjePacijenta;

    private final String objektivniNalaz;

    private final String dijagnoza;

    private final String predlozenaTerapija;

    private final String savet;

    private final Boolean obrisan;

    public PregledResponseDTO(Pregled p) {
        datumPregleda = p.getDatumPregleda();
        indikatorPoverljivosti = p.getIndikatorPoverljivosti();
        glavneTegobe = p.getGlavneTegobe();
        sadasnjaBolest = p.getSadasnjaBolest();
        licnaAnamneza = p.getLicnaAnamneza();
        porodicnaAnamneza = p.getPorodicnaAnamneza();
        misljenjePacijenta = p.getMisljenjePacijenta();
        objektivniNalaz = p.getObjektivniNalaz();
        dijagnoza = p.getDijagnoza();
        predlozenaTerapija = p.getPredlozenaTerapija();
        savet = p.getSavet();
        obrisan = p.getObrisan();
    }
}
