package raf.si.bolnica.laboratory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RezultatParametraAnalizeSaveRequestDTO {
    private long nalogId;
    private long parametarId;
    private String rezultat;
}
