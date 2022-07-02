package raf.si.bolnica.management.constants;

public class Constants {

    private Constants() {
    }

    public static final String VISA_MED_SESTRA = "ROLE_VISA_MED_SESTRA";
    public static final String MED_SESTRA = "ROLE_MED_SESTRA";
    public static final String NACELNIK_ODELJENJA = "ROLE_NACELNIK_ODELJENJA";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String RECEPCIONER = "ROLE_RECEPCIONER";
    public static final String ROLE_VISI_LABORATORIJSKI_TEHNICAR = "ROLE_VISI_LABORATORIJSKI_TEHNICAR";
    public static final String ROLE_LABORATORIJSKI_TEHNICAR = "ROLE_LABORATORIJSKI_TEHNICAR";
    public static final String ROLE_MEDICINSKI_BIOHEMICAR = "ROLE_MEDICINSKI_BIOHEMICAR";
    public static final String ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE = "ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE";

    public static final String BASE_API = "/api";
    public static final String UPDATE_PATIENT_MEDICAL_RECORD_BLOODTYPE_RHFACTOR = "/update-medical-record";
    public static final String ADD_ALLERGEN_TO_PATIENT = "/add-allergen";
    public static final String ADD_VACCINE_TO_PATIENT = "/add-vaccine";

    public static final String REGISTER_LEKARSKI_IZVESTAJ = "/add-lekarski-izvestaj";
    public static final String SEARCH_LEKARSKI_IZVESTAJ = "/search-lekarski-izvestaj";
    public static final String REGISTER_OTPUSNA_LISTA = "/add-otpusna-lista";
    public static final String SEARCH_OTPUSNA_LISTA = "/search-otpusna-lista";

    public interface MissingRequestFields {
        String DEVELOPER_MESSAGE = "Invalid request";
        String MESSAGE = "All fields are required.";
    }

    public interface AllergenNotExist {
        String DEVELOPER_MESSAGE = "Invalid request";
        String MESSAGE = "Allergen not exist.";
    }

    public static final String NACELNIK = "ROLE_DR_SPEC_ODELJENJA";
    public static final String SPECIJALISTA = "ROLE_DR_SPEC";
    public static final String SPECIJLISTA_POV = "ROLE_DR_SPEC_POV";

    public interface VaccineNotExist {
        String DEVELOPER_MESSAGE = "Invalid request";
        String MESSAGE = "Vaccine not exist.";
    }

}

