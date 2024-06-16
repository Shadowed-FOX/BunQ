package com.blackfox.bunq.old;

import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneController {
    private static SceneController instance = null;

    private Scene login;
    private Scene main;
    private FXMLLoader mainLoader;
    private MainController mainController;

    public static synchronized SceneController getInstance() throws IOException {
        if (instance == null)
            instance = new SceneController();
        return instance;
    }

    private SceneController() throws IOException {
        login = new Scene(new FXMLLoader(getClass().getResource("login.fxml")).load());
        mainLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        main = new Scene(mainLoader.load());
        mainController = mainLoader.getController();
    }

    public void switchSceneLogin(Stage stage) throws IOException {
        stage.setScene(login);
        stage.show();
    }

    public void switchSceneMain(Stage stage, int id) throws IOException, ClientNotFoundException {
        mainController.setCurrentClient(HibernateUtil.getClient(id));

        stage.setScene(main);
        stage.show();
    }
}
