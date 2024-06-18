package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController {
    @FXML
    private AnchorPane loginPane, registerPane;
    @FXML
    private TextField username, password, NewUsername, FirstName, SecondName, NewPassword1, NewPassword2;
    @FXML
    private Text LoginMessage, RegisterMessage;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    protected void onLoginSwitchBtnClick() {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    protected void onLoginAttemptBtnClick(ActionEvent event) {
        final String AuthPasswordErr = "Wrong password";
        final String AuthSucc = "Login was successful";

        try {
            ClientAuth clA = HibernateUtil.getClientAuth(username.getText());
            if (!clA.checkPassword(password.getText())) {
                LoginMessage.setText(AuthPasswordErr);
                password.requestFocus();
            } else {
                LoginMessage.setText(AuthSucc);
                HibernateUtil.setActiveClientAuth(clA);
                mainController.loadDashboard();
            }
        } catch (ClientNotFoundException | IOException ex) {
            LoginMessage.setText(ex.getMessage());
            username.requestFocus();
            username.clear();
        } finally {
            password.clear();
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

        if (!newPassword1.equals(newPassword2)) {
            RegisterMessage.setText("Passwords do not match.");
            return;
        }

        try {
            HibernateUtil.createClient(newUsername, newPassword1, firstName, lastName);
            NewUsername.clear();
            NewPassword1.clear();
            NewPassword2.clear();
            FirstName.clear();
            SecondName.clear();

            onLoginSwitchBtnClick();
            username.setText(newUsername);
            password.requestFocus();
            LoginMessage.setText("Rejestracja się powiodła.");
        } catch (Exception ex) {
            RegisterMessage.setText(ex.getMessage());
        }
    }
}
