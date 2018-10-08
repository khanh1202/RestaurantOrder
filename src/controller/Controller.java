package controller;

import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import validation.Validation;
import validation.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Controller {
    @FXML
    private TextField custnameTF;
    @FXML
    private TextField tablenumTF;
    @FXML
    private Button enterDataBtn;
    @FXML
    public void initialize() throws Exception {
        ResultSet result = Database.instance().executeQuery("select * from test_table");
        while (result.next()) {
            System.out.println("First name: " + result.getString("first_name") + ". Last name: " + result.getString("last_name"));
        }
        registerButtons();
    }

    private void registerButtons() {
        enterDataBtn.setOnAction(event -> {
            Validation.alertInvalid(custnameTF.getText(), "Customer Name is required", (name) -> {
                return !Validator.testIsNull(name);
            });
            Validation.alertInvalid(tablenumTF.getText(), "Table Number must be numeric from 1 to 10", (tablenum) -> {
                return Validator.testIsNumericFrom1To10(tablenum);
            });
            Validation.fireErrorMessage();
        });
    }
}
