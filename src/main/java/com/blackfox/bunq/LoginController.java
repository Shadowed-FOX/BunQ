package com.blackfox.bunq;

import com.blackfox.bunq.database.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField NewUsername;
    @FXML
    private TextField FirstName;
    @FXML
    private TextField SecondName;
    @FXML
    private TextField NewPassword1;
    @FXML
    private TextField NewPassword2;
    @FXML
    private Label AuthMessage;

    private double screenX = 0;
    private double screenY = 0;

    @FXML
    protected void onLoginSwitchBtnClick() {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    protected void onLoginAttemptBtnClick(ActionEvent event) {
        final String AuthUserNameErr = "Niepoprawna nazwa użytkownika";
        final String AuthPasswordErr = "Niepoprawne hasło";
        final String AuthSucc = "Logowanie się powiodło";

        try {
            ClientAuth clA = HibernateUtil.getClientAuth(username.getText());
            if (!clA.checkPassword(password.getText()))
                AuthMessage.setText(AuthPasswordErr);
            else
                AuthMessage.setText(AuthSucc);

        } catch (ClientNotFoundException ex) {
            AuthMessage.setText(AuthUserNameErr);
        }

    }

    @FXML
    protected void onRegisterSwitchBtnClick() {
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    protected void onRegisterAttemptBtnClick(ActionEvent event) {
        String newUsername = NewUsername.getText();
        String newPassword1 = NewPassword1.getText();
        String newPassword2 = NewPassword2.getText();
        String firstName = FirstName.getText();
        String lastName = SecondName.getText();

        // TODO: AuthMessage okazuje się jest widoczny tylko dla logowania
        if (!newPassword1.equals(newPassword2)) {
            System.out.println("Passwords do not match.");
            AuthMessage.setText("Passwords do not match.");
            return;
        }

        try {
            HibernateUtil.createClient(newUsername, newPassword1, firstName, lastName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            AuthMessage.setText(ex.getMessage());
        }

        System.out.println("Client created.");
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
