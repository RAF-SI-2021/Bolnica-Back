package raf.si.bolnica.management.requests;

public class LekarskiIzvestajDTORequestValidator {

    private LekarskiIzvestajDTORequestValidator() {

    }

    public static String validate(LekarskiIzvestajDTO request) {
        if (request.getLbp() == null) return "LBP je obavezno polje";
        if (request.getObjektivniNalaz() == null) return "Objektivni nalaz je obavezno polje";
        if (request.getDijagnoza() == null) return "Dijagnoza je obavezno polje";
        if (request.getPredlozenaTerapija() == null) return "Predlozena terapija je obavezno polje";
        if (request.getSavet() == null) return "Savet je obavezno polje";
        return "OK";
    }
}
