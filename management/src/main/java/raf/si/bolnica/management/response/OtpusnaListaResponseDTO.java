package raf.si.bolnica.management.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.OtpusnaLista;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class OtpusnaListaResponseDTO {


    private Timestamp datumPrijema;
    private Timestamp datumOtpustanja;
    private String uputnaDijagnoza;
    private OtpusnaLista otpusnaLista;
    private Object nacelnikOdeljenja;
    private Object ordinirajuciLekar;

}
