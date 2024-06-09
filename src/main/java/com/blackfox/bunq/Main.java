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

    static void fetchingExample(String username, String password) {
        try {
            ClientAuth clientAuth = HibernateUtil.getClientAuth(username);
            System.out.println("username: " + clientAuth.getUsername());
            System.out.println("checkPassword: " + clientAuth.checkPassword(password));
            // clientAuth.setPassword("haslo123");

            Client client = HibernateUtil.getClient(clientAuth.getId());
            System.out.println("firstname: " + client.getFirstname());
            System.out.println("lastname: " + client.getLastname());
            System.out.println("created_at: " + client.getCreatedAt());
        } catch (ClientNotFoundException ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        try {
            // int clientId = HibernateUtil.createClient("cebulla", "cebulion", "Andzej", "Grochowalski");
            fetchingExample("cebulla", "cebulion");
        } catch (Exception ex) {
            System.err.println(ex);
        }

        launch();
        HibernateUtil.close();
    }
}
