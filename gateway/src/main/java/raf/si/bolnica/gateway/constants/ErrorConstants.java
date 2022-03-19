package raf.si.bolnica.gateway.constants;

public class ErrorConstants {

    public interface TokenInvalid {
        String DEVELOPER_MESSAGE = "Request not authorized.";
        String MESSAGE = "Unmatched JWT token.";
    }
}
