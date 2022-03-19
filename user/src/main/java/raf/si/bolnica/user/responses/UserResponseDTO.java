package raf.si.bolnica.user.responses;

import java.util.Set;

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

    private Set<String> roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
