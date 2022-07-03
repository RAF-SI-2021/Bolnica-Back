package raf.si.bolnica.user.exceptions;

import org.springframework.http.HttpStatus;


public class UnauthorisedException extends RuntimeException {
    private ErrorResponse errorResponse;

    public UnauthorisedException(String message, String developerMessage) {
        super(message);
        errorResponse = new ErrorResponse();

        errorResponse.setDeveloperMsg(developerMessage);
        errorResponse.setErrorMsg(message);
        errorResponse.setResponseCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setResponseStatus(HttpStatus.UNAUTHORIZED);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

