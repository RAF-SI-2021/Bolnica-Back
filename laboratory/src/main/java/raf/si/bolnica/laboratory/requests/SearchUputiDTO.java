package raf.si.bolnica.laboratory.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchUputiDTO {

    private String lbp;

    private TipUputa tipUputa;
}
