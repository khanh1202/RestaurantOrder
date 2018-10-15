package controller;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.MenuItem;
import models.Menu;
import models.Order;
import models.OrderList;
import validation.Validation;
import validation.Validator;



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
    private ComboBox<MenuItem> foodList;
    @FXML
    private ComboBox<MenuItem> beverageList;
    @FXML
    private ListView<Order> orderWaitingLV;
    @FXML
    private Button dispOrderBtn;
    @FXML
    private Button clearBtn;
    private RadioButton chosenButton;
    @FXML
    public void initialize() {
        Database.instance().setUp();
        registerButtons();
        chosenButton = (RadioButton) mealGroup.getSelectedToggle();
        getExistingOrders();
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
            Validation.alertInvalid(foodList.getValue() != null? foodList.getValue().toString() : "", "Food must be selected", (food) -> {
                return !Validator.testIsNull(food);
            });
            Validation.alertInvalid(beverageList.getValue() != null? foodList.getValue().toString() : "", "Beverage must be selected", (beverage) -> {
                return !Validator.testIsNull(beverage);
            });
            if (!Validation.getErrorMessage().equals(""))
                Validation.fireErrorMessage();
            else {
                MessageBox.show("Order Placed Successfully", "Information");
                placeOrder();
                resetForm();
            }
        });

        mealGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue != null)
                    chosenButton = (RadioButton)newValue.getToggleGroup().getSelectedToggle();
                if (chosenButton != null) {
                    foodList.setDisable(false);
                    beverageList.setDisable(false);
                }
                else {
                    foodList.setDisable(true);
                    beverageList.setDisable(true);
                }

                manipulateComboBoxes(chosenButton.getText());
            }
        });

        dispOrderBtn.setOnAction(event -> {
            Validation.alertInvalid(tablenumTF.getText(), "Table Number must be numeric from 1 to 10", (tablenum) -> {
                return Validator.testIsNumericFrom1To10(tablenum);
            });
            if (!Validation.getErrorMessage().equals(""))
                Validation.fireErrorMessage();
            else {
                displayOrder();
            }
        });

        clearBtn.setOnAction(event -> {
            clearDisplay();
        });
    }

    private void displayOrder() {
        orderWaitingLV.getItems().clear();
        orderWaitingLV.getItems().addAll(OrderList.instance().orderAtTable(Integer.valueOf(tablenumTF.getText())));
    }

    private void clearDisplay() {
        orderWaitingLV.getItems().clear();
        orderWaitingLV.getItems().addAll(OrderList.instance().waitingOrders());
    }

    private void getExistingOrders() {
        orderWaitingLV.getItems().addAll(OrderList.instance().waitingOrders());
    }

    private void placeOrder() {
        Order lastOrder = OrderList.instance().lastOrder();
        Order newOrder = new Order(lastOrder == null? 1 : lastOrder.getOrder_id() + 1, custnameTF.getText(),
                Integer.valueOf(tablenumTF.getText()), foodList.getValue().getName(), beverageList.getValue().getName(), "waiting");
        Database.instance().addOrder(newOrder, foodList.getValue().getId(), beverageList.getValue().getId());
        orderWaitingLV.getItems().add(newOrder);
    }

    private void resetForm() {
        custnameTF.setText("");
        tablenumTF.setText("");
        mealGroup.getSelectedToggle().setSelected(false);
    }

    private void manipulateComboBoxes(String meal) {
        try {
            foodList.getItems().clear();
            beverageList.getItems().clear();
            foodList.getItems().addAll(Menu.instance().getItemsByMeal(meal, "food"));
            beverageList.getItems().addAll(Menu.instance().getItemsByMeal(meal, "beverage"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
