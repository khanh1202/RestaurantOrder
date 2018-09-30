package sample;

import database.Database;
import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Controller {
    @FXML
    public void initialize() throws Exception {
        ResultSet result = Database.instance().executeQuery("select * from test_table");
        while (result.next()) {
            System.out.println("First name: " + result.getString("first_name") + ". Last name: " + result.getString("last_name"));
        }
    }
}
