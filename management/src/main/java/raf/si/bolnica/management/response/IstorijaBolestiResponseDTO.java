package raf.si.bolnica.management.response;

import lombok.Getter;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;

import java.sql.Date;

@Getter
public class IstorijaBolestiResponseDTO {

    private final String dijagnoza;

    private final Boolean indikatorPoverljivosti;

    private final Date datumPocetkaZdravstvenogProblema;

    private final Date datumZavrsetkaZdravstvenogProblema;

    private final RezultatLecenja rezultatLecenja;

    private final String opisTekucegStanja;

    private final Date podatakValidanOd;

    private final Date podatakValidanDo;

    private final Boolean podaciValidni;

    private final Boolean obrisan;

    public IstorijaBolestiResponseDTO(IstorijaBolesti i) {
        dijagnoza = i.getDijagnoza();
        indikatorPoverljivosti = i.getIndikatorPoverljivosti();
        datumPocetkaZdravstvenogProblema = i.getDatumPocetkaZdravstvenogProblema();
        datumZavrsetkaZdravstvenogProblema = i.getDatumZavrsetkaZdravstvenogProblema();
        rezultatLecenja = i.getRezultatLecenja();
        opisTekucegStanja = i.getOpisTekucegStanja();
        podatakValidanDo = i.getPodatakValidanDo();
        podatakValidanOd = i.getPodatakValidanOd();
        podaciValidni = i.getPodaciValidni();
        obrisan = i.getObrisan();
    }
}
