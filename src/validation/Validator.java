package validation;

/**
 * @author Gia Khanh Dinh
 */
public interface Validator {
    boolean test(String string);

    /**
     * check if a string is empty or not
     * @param string string to be checked
     * @return true if a string is empty
     */
    static boolean testIsNull(String string) {
        return string == null || string.equals("");
    }

    /**
     * check if a string is a number from 1 to 10
     * @param string a string to be checked
     * @return true if a string is a number from 1 to 10
     */
    static boolean testIsNumericFrom1To10(String string) {
        return !testIsNull(string) && string.matches("[1-9]|10");
    }
}
