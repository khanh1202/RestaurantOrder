package controller;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private ToggleGroup mealGroup;
    @FXML
    private ComboBox<String> foodList;
    @FXML
    private ComboBox<String> beverageList;
    private RadioButton chosenButton;
    @FXML
    public void initialize() throws Exception {
        ResultSet result = Database.instance().executeQuery("select * from test_table");
        while (result.next()) {
            System.out.println("First name: " + result.getString("first_name") + ". Last name: " + result.getString("last_name"));
        }
        registerButtons();
        chosenButton = (RadioButton) mealGroup.getSelectedToggle();
    }

    private void registerButtons() {
        enterDataBtn.setOnAction(event -> {
            Validation.alertInvalid(custnameTF.getText(), "Customer Name is required", (name) -> {
                return !Validator.testIsNull(name);
            });
            Validation.alertInvalid(tablenumTF.getText(), "Table Number must be numeric from 1 to 10", (tablenum) -> {
                return Validator.testIsNumericFrom1To10(tablenum);
            });
            if (chosenButton == null)
                Validation.alertInvalid("", "Radio button must be checked", (meal) -> {
                    return !Validator.testIsNull(meal);
                });
            Validation.fireErrorMessage();
        });

        mealGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                chosenButton = (RadioButton)newValue.getToggleGroup().getSelectedToggle();
                foodList.setDisable(false);
                beverageList.setDisable(false);
                manipulateComboBoxes(chosenButton.getText());
            }
        });
    }

    private void manipulateComboBoxes(String meal) {
        try {
            foodList.getItems().clear();
            beverageList.getItems().clear();
            ResultSet resultFood = Database.instance().executeQuery("select item_name from menu where item_meal='" + meal + "' and item_type='food'");
            ResultSet resultBeverage = Database.instance().executeQuery("select item_name from menu where item_meal='" + meal + "' and item_type='beverage'");
            while (resultFood.next()) {
                foodList.getItems().add(resultFood.getString("item_name"));
            }
            while (resultBeverage.next()) {
                beverageList.getItems().add(resultBeverage.getString("item_name"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
