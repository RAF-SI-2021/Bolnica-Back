package raf.si.bolnica.gateway.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponseDTO {

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long userId, String name, String surname, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
    }

    private Long userId;

    private String name;

    private String surname;

    private String password;

    private String email;

    private Set<RoleResponseDTO> roles;
}
