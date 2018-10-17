package sample;

import controller.Controller;
import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("customer.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Customer");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/customer.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            try {
                Database.instance().close();
                Controller myCtrl = loader.getController();
                myCtrl.stopClient();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
