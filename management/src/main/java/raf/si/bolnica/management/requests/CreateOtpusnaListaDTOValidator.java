package raf.si.bolnica.management.requests;

public class CreateOtpusnaListaDTOValidator {

    private CreateOtpusnaListaDTOValidator() {

    }

    public static String validate(CreateOtpusnaListaDTO req) {

        if (req.getPbo() == -1) return "Pbo je obavezno polje";
        if (req.getLbp() == null) return "Lbp je obavezno polje";
        if (req.getAnamneza() == null) return "Anamneza je obavezno polje";
        if (req.getZakljucak() == null) return "Zakljucak je obavezno polje";
        return "OK";
    }

}
