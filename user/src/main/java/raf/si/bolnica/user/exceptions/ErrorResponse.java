package raf.si.bolnica.user.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    public ErrorResponse() {}

    private String errorMsg;

    private String developerMsg;

    private HttpStatus responseStatus;

    private int responseCode;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setDeveloperMsg(String developerMsg) {
        this.developerMsg = developerMsg;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}



