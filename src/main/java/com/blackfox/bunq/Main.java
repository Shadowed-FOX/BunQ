package com.blackfox.bunq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.blackfox.bunq.database.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Parent root = FxmlLoader.getPane("main");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 960, 600);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("BunQ");
        stage.show();
    }


    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        Logger.getLogger("com.mchange").setLevel(Level.WARNING);
        HibernateUtil.getSessionFactory();
        // fetchingExample("Dziady", "123dziady");

        launch();
        HibernateUtil.close();
    }
}
