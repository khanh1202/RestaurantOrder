package validation;

import controller.MessageBox;

public class Validation {
    private static String errorMessage = "";
    public static void alertInvalid(String toCheck, String error, Validator validator) {
        if (!validator.test(toCheck))
            errorMessage += error + "\n";
    }

    public static void fireErrorMessage() {
        if (!errorMessage.equals(""))
            MessageBox.show(errorMessage, "Input Error");
        errorMessage = "";
    }
}
