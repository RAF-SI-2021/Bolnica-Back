package raf.si.bolnica.management.requests;

public class LekarskiIzvestajFilterDTORequestValidator {

    private LekarskiIzvestajFilterDTORequestValidator() {

    }

    public static String validate(LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO) {
        if (lekarskiIzvestajFilterDTO.getLbp() == null) return "LBP je obavezno polje";
        return "OK";
    }

    public static Integer checkRequest(LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO) {
        if (lekarskiIzvestajFilterDTO.getDate() == null && lekarskiIzvestajFilterDTO.getEnd() == null) {
            return 0;
        }
        if (lekarskiIzvestajFilterDTO.getDate() != null && lekarskiIzvestajFilterDTO.getEnd() == null) {
            return 1;
        }
        if (lekarskiIzvestajFilterDTO.getDate() != null && lekarskiIzvestajFilterDTO.getEnd() != null) {
            return 2;
        }
        return 0;
    }

}
