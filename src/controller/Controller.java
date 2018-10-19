package controller;

import database.Database;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.MenuItem;
import models.Menu;
import models.Order;
import models.OrderList;
import validation.Validation;
import validation.Validator;


/**
 * Takes care of all logics of the application
 * @author Gia Khanh Dinh
 */
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
    private ListView<Order> orderServedLV;
    @FXML
    private Label displayLbl;
    @FXML
    private TableView displayTable;
    @FXML
    private Button dispOrderBtn;
    @FXML
    private Button dispChoiceBtn;
    @FXML
    private Button clearBtn;
    private RadioButton chosenButton;
    private ClientPoint client;
    @FXML
    public void initialize() throws Exception {
        client = new ClientPoint("localhost", this);
        client.start();
        Database.instance().setUp();
        registerButtons();
        chosenButton = (RadioButton) mealGroup.getSelectedToggle();
        getExistingOrders();
    }

    /**
     * Add event handler to all control elements
     */
    private void registerButtons() {
        enterDataBtn.setOnAction(event -> {
            //Check all input fields from the users and display error if there is an invalid input
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
            Validation.alertInvalid(beverageList.getValue() != null? beverageList.getValue().toString() : "", "Beverage must be selected", (beverage) -> {
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

        //Asynchronously check the input from the users and change text color if input is valid or not
        tablenumTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Validation.changeFontColor(tablenumTF, (tablenum) -> {
                    return Validator.testIsNumericFrom1To10(tablenum);
                });
            }
        });

        //Check if a radio button is selected. If yes, users can select the food and beverage
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

        dispChoiceBtn.setOnAction(event -> {
            Validation.alertInvalid(foodList.getValue() != null? foodList.getValue().toString() : "", "Food must be selected", (food) -> {
                return !Validator.testIsNull(food);
            });
            Validation.alertInvalid(beverageList.getValue() != null? beverageList.getValue().toString() : "", "Beverage must be selected", (beverage) -> {
                return !Validator.testIsNull(beverage);
            });
            if (!Validation.getErrorMessage().equals(""))
                Validation.fireErrorMessage();
            else {
                displayChoice();
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

    /**
     * Create and manipulate a table to display order at a specified table
     */
    private void displayOrder() {
        ObservableList<Order> atTable = OrderList.instance().orderAtTable(Integer.valueOf(tablenumTF.getText()));
        if (atTable.size() != 0) {
            displayLbl.setText("Ordered Items at Table " + tablenumTF.getText());
            displayTable.getColumns().clear();
            displayTable.setItems(atTable);
            TableColumn<Order, String> customerCol = new TableColumn<>("Customer Name");
            TableColumn<Order, String> orderedCol = new TableColumn<>("Ordered Items");
            customerCol.setCellValueFactory(new PropertyValueFactory<Order, String>("Customer_name"));
            orderedCol.setCellValueFactory(new PropertyValueFactory<Order, String>("Ordered"));
            displayTable.getColumns().addAll(customerCol, orderedCol);
        }
        else {
            MessageBox.show("No order at table " + tablenumTF.getText(), "Empty result");
        }
    }

    /**
     * Create and manipulate a table to display choice selected by user
     */
    private void displayChoice() {
        displayLbl.setText("Menu Choices and Nutrition Information");
        displayTable.getColumns().clear();
        displayTable.setItems(Menu.instance().getItemsByName(foodList.getValue().toString(), beverageList.getValue().toString()));
        TableColumn<MenuItem, String> itemnameCol = new TableColumn<>("Item Name");
        TableColumn<MenuItem, Double> energyCol = new TableColumn<>("Energy");
        TableColumn<MenuItem, Double> proteinCol = new TableColumn<>("Protein");
        TableColumn<MenuItem, Double> carbohydrateCol = new TableColumn<>("Carbohydrate");
        TableColumn<MenuItem, Double> fatCol = new TableColumn<>("Total Fat");
        TableColumn<MenuItem, Double> fibreCol = new TableColumn<>("Fibre");
        TableColumn<MenuItem, Integer> priceCol = new TableColumn<>("Price");
        itemnameCol.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("Name"));
        energyCol.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("Energy"));
        proteinCol.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("Protein"));
        carbohydrateCol.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("Carbohydrate"));
        fatCol.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("Fat"));
        fibreCol.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("Fibre"));
        priceCol.setCellValueFactory(new PropertyValueFactory<MenuItem, Integer>("Price"));
        displayTable.getColumns().addAll(itemnameCol, energyCol, proteinCol, carbohydrateCol, fatCol, fibreCol, priceCol);
    }

    private void clearDisplay() {
        displayLbl.setText("");
        displayTable.getColumns().clear();
    }

    /**
     * Update the waiting and served list as soon as there is an update from this interface or other interface
     */
    private void getExistingOrders() {
        orderWaitingLV.getItems().clear();
        orderServedLV.getItems().clear();
        orderWaitingLV.getItems().addAll(OrderList.instance().waitingOrders());
        orderServedLV.getItems().addAll(OrderList.instance().servedOrders());
    }

    /**
     * Add an order to the database and notify other clients to update their interfaces
     */
    private void placeOrder() {
        Order lastOrder = OrderList.instance().lastOrder();
        Order newOrder = new Order(lastOrder == null? 1 : lastOrder.getOrder_id() + 1, custnameTF.getText(),
                Integer.valueOf(tablenumTF.getText()), foodList.getValue().getName(), beverageList.getValue().getName(), "waiting");
        Database.instance().addOrder(newOrder, foodList.getValue().getId(), beverageList.getValue().getId());
        orderWaitingLV.getItems().add(newOrder);
        client.notifyServer();
    }

    private void resetForm() {
        custnameTF.setText("");
        tablenumTF.setText("");
        mealGroup.getSelectedToggle().setSelected(false);
    }

    /**
     * instantiate choices to the combo boxes based on meal type chosen by users
     * @param meal
     */
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

    public void updateOrders() {
        OrderList.instance().fetchOrders();
        Platform.runLater(() -> getExistingOrders());
    }

    public void stopClient() {
        client.setShouldStop(true);
        client.quitServer();
    }

}
