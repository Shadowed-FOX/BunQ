package com.blackfox.bunq;

import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.maintenance.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import org.postgresql.jdbc.EscapeSyntaxCallMode;

import java.io.IOException;

public class SceneController {
    public void switchSceneLogin(Stage stage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchSceneMain(Stage stage, int id) throws IOException, ClientNotFoundException {
        //Loading root from fxml file.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        //Creating instance of controller and passing logged in client into it.
        MainController mainController = loader.getController();
        mainController.setCurrentClient(HibernateUtil.getClient(id));

        //Setting up view of the scene.
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
