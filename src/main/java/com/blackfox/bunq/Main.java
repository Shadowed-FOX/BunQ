package com.blackfox.bunq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import org.hibernate.Session;
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
        // przykładowe użycie bazy
        // aby cokolwiek na niej zrobić należy otworzyć nową sesję
        // dane pobiera się podając oczekiwaną Klasę.class oraz id
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Client client = session.get(Client.class, 69999999);
            if (client != null)
                System.out.println("Fetched username: " + client.getUsername());
            else
                System.out.println("Couldn't fetch a user");
            session.close();
        }

        launch();
        HibernateUtil.close();
    }
}
