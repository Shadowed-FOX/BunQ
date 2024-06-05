package com.blackfox.bunq;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;


public class LoginController {
    @FXML
    private AnchorPane loginPane;
    @FXML
    private AnchorPane registerPane;
    @FXML
    private VBox window;
    @FXML
    private Button closeBtn;
    private double screenX = 0;
    private double screenY = 0;

    @FXML
    protected void onLoginSwitchBtnClick() {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    protected void onRegisterSwitchBtnClick() {
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    protected void exitEvent(ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
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
}