package raf.si.bolnica.user.requests;

import java.io.Serializable;

public class LoginRequestDTO implements Serializable {

    private String userCredential;

    private String password;

    private Long subDepartmentId;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String userCredential, String password, Long subDepartmentId) {
        this.userCredential = userCredential;
        this.password = password;
        this.subDepartmentId = subDepartmentId;
    }

    public String getUserCredential() {
        return userCredential;
    }

    public void setUserCredential(String userCredential) {
        this.userCredential = userCredential;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSubDepartmentId() {
        return subDepartmentId;
    }

    public void setSubDepartmentId(Long subDepartmentId) {
        this.subDepartmentId = subDepartmentId;
    }
}
