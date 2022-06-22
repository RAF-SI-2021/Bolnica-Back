package raf.si.bolnica.laboratory.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.enums.StatusUputa;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetUputStatusDTO {

    private long uputId;

    private StatusUputa statusUputa;
}
