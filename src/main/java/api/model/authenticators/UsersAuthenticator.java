package api.model.authenticators;

public class UsersAuthenticator {
    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$";
        return email.matches(regex);
    }

    public static boolean isValidNotNull(String input) {
        return input.length() >= 2;
    }
}
