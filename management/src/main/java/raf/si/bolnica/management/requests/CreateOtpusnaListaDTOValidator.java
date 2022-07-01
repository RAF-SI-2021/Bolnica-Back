package raf.si.bolnica.management.requests;

public class CreateOtpusnaListaDTOValidator {

    private CreateOtpusnaListaDTOValidator() {

    }

    public static String validate(CreateOtpusnaListaDTO req) {

        if (req.getPbo() == 0) return "Pbo je obavezno polje";
        if (req.getLbp() == null) return "Lbp je obavezno polje";
        if (req.getPrateceDijagnoze() == null) return "Pratece dijagnoze su obavezno polje";
        if (req.getAnamneza() == null) return "Anamneza je obavezno polje";
        if (req.getAnalize() == null) return "Analize je obavezno polje";
        if (req.getTokBolesti() == null) return "Tok bolesti je obavezno polje";
        if (req.getZakljucak() == null) return "Zakljucak je obavezno polje";
        if (req.getTerapija() == null) return "Terapija je obavezno polje";
        return "OK";
    }

}
