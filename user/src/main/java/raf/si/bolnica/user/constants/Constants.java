package raf.si.bolnica.user.constants;

public class Constants {

    public static final String BASE_API = "/api";
    public static final String LOGIN = "/login";

    public static final String JWT_KEY = "QnuQblQWn8H9ggiwfGbCxpPA3gdY1oAe";

    public interface ForgetPassword {
        String DEVELOPER_MESSAGE = "Password didn't match with the original one.";
        String MESSAGE = "Incorrect password.Forgot Password?";
    }

    public interface InvalidAdminUsername {
        String DEVELOPER_MESSAGE = "Admin entity returned null";
        String MESSAGE = "Admin with given username doesn't exits.";
    }
}