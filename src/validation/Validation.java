package validation;

import controller.MessageBox;
import javafx.scene.control.TextField;

/**
 * @author Gia Khanh Dinh
 */

public class Validation {
    private static String errorMessage = "";

    /**
     * Check a user input and add error message if there is an invalid input
     * @param toCheck the string to be checked
     * @param error the error to show if invalid input is provided
     * @param validator the criteria to check the input
     */
    public static void alertInvalid(String toCheck, String error, Validator validator) {
        if (!validator.test(toCheck))
            errorMessage += error + "\n";
    }

    /**
     * Display the error messages in a dialog box
     */
    public static void fireErrorMessage() {
        MessageBox.show(errorMessage, "Input Error");
        errorMessage = "";
    }

    /**
     * Change font fill of a textfield if a criteria is met
     * @param textfield
     * @param validator
     */
    public static void changeFontColor(TextField textfield, Validator validator) {
        if (validator.test(textfield.getText()))
            textfield.setStyle("-fx-text-fill: green");
        else
            textfield.setStyle("-fx-text-fill: red");
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}
