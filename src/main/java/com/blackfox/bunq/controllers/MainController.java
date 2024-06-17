package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private VBox window;
    @FXML
    private AnchorPane root;

    private double screenX, screenY;

    @FXML
    public void initialize() throws IOException {
        loadSignIn();
    }

    public void loadSignIn() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/signingforms.fxml"));
        Parent child = loader.load();
        LoginController loginController = loader.getController();
        loginController.setMainController(this);
        root.getChildren().setAll(child);
    }

    public void loadDashboard() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/dashboard.fxml"));
        Parent child = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setMainController(this);
        root.getChildren().setAll(child);
    }

    @FXML
    protected void exitMainEvent(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void menuDragEvent(MouseEvent event) {
        Stage stage = (Stage) window.getScene().getWindow();
        stage.setX(event.getScreenX() - screenX);
        stage.setY(event.getScreenY() - screenY);
    }

    @FXML
    protected void menuPressedEvent(MouseEvent event) {
        screenX = event.getSceneX();
        screenY = event.getSceneY();
    }

    @FXML
    protected void onMinimizeBtnPress(ActionEvent event){
        Stage stage = (Stage) window.getScene().getWindow();
        stage.setIconified(true);
    }
}
