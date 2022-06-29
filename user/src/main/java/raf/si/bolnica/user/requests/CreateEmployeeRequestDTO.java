package raf.si.bolnica.user.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequestDTO {

    private String name;

    private String surname;

    private Date dob;

    private String gender;

    private String jmbg;

    private String address;

    private String city;

    private String contact;

    private String email;

    private String title;

    private String profession;

    private long department;

    private List<String> roles;
}
