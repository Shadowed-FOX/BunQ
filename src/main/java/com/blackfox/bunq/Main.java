package com.blackfox.bunq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

import com.blackfox.bunq.database.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 600);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Debug/Example
        ClientAuth clientAuth = HibernateUtil.getCientAuth("noradenshi");
        if (clientAuth != null) {
            System.out.println("username: " + clientAuth.getUsername());
            System.out.println("password: " + clientAuth.getPassword());
        }

        launch();
        HibernateUtil.close();
    }
}
