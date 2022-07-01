package raf.si.bolnica.management.requests;

public class OtpusnaListaFilterDTORequestValidator {

    private OtpusnaListaFilterDTORequestValidator() {

    }

    public static String validate(OtpusnaLIstaFilterDTO otpusnaLIstaFilterDTO) {
        if (otpusnaLIstaFilterDTO.getLbp() == null) return "Lbp je obavezno polje";
        return "OK";
    }

    public static Integer checkRequest(OtpusnaLIstaFilterDTO otpusnaLIstaFilterDTO) {
        if (otpusnaLIstaFilterDTO.getStart() == null && otpusnaLIstaFilterDTO.getEnd() == null) {
            return 0;
        }
        if (otpusnaLIstaFilterDTO.getStart() != null && otpusnaLIstaFilterDTO.getEnd() == null) {
            return 1;
        }
        if (otpusnaLIstaFilterDTO.getStart() != null && otpusnaLIstaFilterDTO.getEnd() != null) {
            return 2;
        }
        return 0;
    }

}
