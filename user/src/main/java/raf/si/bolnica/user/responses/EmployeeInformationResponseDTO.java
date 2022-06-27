package raf.si.bolnica.user.responses;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.user.models.User;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeInformationResponseDTO {
    private String name;
    private String surname;
    private String profession;

    public EmployeeInformationResponseDTO(User user){
        this.name = user.getName();
        this.surname = user.getSurname();
        this.profession = user.getZanimanje();
    }



}
