package validation;

import controller.MessageBox;
import javafx.scene.control.TextField;

public class Validation {
    private static String errorMessage = "";
    public static void alertInvalid(String toCheck, String error, Validator validator) {
        if (!validator.test(toCheck))
            errorMessage += error + "\n";
    }

    public static void fireErrorMessage() {
        MessageBox.show(errorMessage, "Input Error");
        errorMessage = "";
    }

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
