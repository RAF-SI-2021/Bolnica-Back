package raf.si.bolnica.laboratory.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetLabExaminationsByDateDTO {

    private Timestamp dateAndTime;
}
