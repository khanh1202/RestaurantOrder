package validation;

public interface Validator {
    boolean test(String string);

    static boolean testIsNull(String string) {
        return string == null || string.equals("");
    }

    static boolean testIsNumericFrom1To10(String string) {
        return !testIsNull(string) && string.matches("[1-9]|10");
    }
}
