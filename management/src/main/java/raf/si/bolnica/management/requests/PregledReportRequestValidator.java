package raf.si.bolnica.management.requests;

import org.springframework.http.HttpStatus;

public class PregledReportRequestValidator {

    public static String validate(CreatePregledReportRequestDTO request) {
        if (request.getObjektivniNalaz() == null) return  "Objektivni nalaz je obavezno polje!";
        if (request.getLbp() == null) return  "Licni broj pacijenta(Lbp) je obavezno polje!";
        if (request.getZaposleniId() == -1) return  "Id zaposlenog je obavezno polje!";
        return "OK";
    }
}
