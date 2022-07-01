package raf.si.bolnica.management.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LekarskiIzvestajDTO {
    //LBP pacijenta, Objektivni nalaz, Dijagnoza, Predložena terapija, Savet, Indikator poverljivost
    private String lbp;
    private String objektivniNalaz;
    private String dijagnoza;
    private String predlozenaTerapija;
    private String savet;
    private boolean indikatorPoverljivosti;


}
