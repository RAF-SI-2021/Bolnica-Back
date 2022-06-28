package raf.si.bolnica.management.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOtpusnaListaDTO {

    private long pbo;
    private String lbp;
    private String prateceDijagnoze;
    private String anamneza;
    private String analize;
    private String tokBolesti;
    private String zakljucak;
    private String terapija;

}
