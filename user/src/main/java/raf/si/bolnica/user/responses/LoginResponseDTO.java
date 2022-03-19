package raf.si.bolnica.user.responses;

import java.io.Serializable;

public class LoginResponseDTO implements Serializable {

    private String token;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
