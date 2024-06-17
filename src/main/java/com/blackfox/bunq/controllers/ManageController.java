package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientCredentialsException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.maintenance.SceneLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageController {
    @FXML
    private Text firstName, surName, accId, usernameField, errPasswordText, errNewUsernameText;
    @FXML
    private TextField NewUsernameField, ConfirmUsernameField;
    @FXML
    private AnchorPane newUserName, newPassword, destroyAcc, colorChange;
    @FXML
    private StackPane stackPane;
    @FXML
    private PasswordField password1Field, password2Field, ConfirmUsernamePassword;

    @FXML
    public void initialize() {
        getInfo();
    }

    private void getInfo() {
        Client client = HibernateUtil.getActiveClient();
        firstName.setText(client.getFirstname());
        surName.setText(client.getLastname());
        accId.setText(String.valueOf(client.getId()));
        usernameField.setText(HibernateUtil.getActiveClientAuth().getUsername());
    }

    @FXML
    protected void onPasswordChangePress(ActionEvent event) {
        backgroundColor();
        newPassword.setVisible(true);
    }

    @FXML
    protected void onAccountDestroyConfirm(ActionEvent event) throws IOException {
        String usernamefield = ConfirmUsernameField.getText();

        if (usernamefield.equals(HibernateUtil.getActiveClientAuth().getUsername())) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent newParent = SceneLoader.getPane("main");
            Scene newScene = new Scene(newParent, 960, 600);
            String css = Main.class.getResource("./style.css").toExternalForm();
            newScene.getStylesheets().add(css);
            stage.setScene(newScene);
        } else {

        }
    }

    @FXML
    protected void onNewUsernameConfirm(ActionEvent event) {
        String newUsername = NewUsernameField.getText();
        String confirmPassword1 = ConfirmUsernamePassword.getText();
        try {
            if(HibernateUtil.getActiveClientAuth().checkPassword(confirmPassword1)){
                HibernateUtil.getActiveClientAuth().updateUsername(newUsername);
                errNewUsernameText.setText("Zmiana nazwy użytkownika powiodła się");
                NewUsernameField.clear();
                getInfo();
            } else {
                errNewUsernameText.setText("Nieprawidłowe hasło");
            }
        } catch (ClientCredentialsException e) {
            errNewUsernameText.setText(e.getMessage());
            ConfirmUsernamePassword.clear();
        } finally {
            ConfirmUsernamePassword.clear();
        }
    }

    @FXML
    protected void onNewPasswordConfirm(ActionEvent event) {
        String password1 = password1Field.getText();
        String password2 = password2Field.getText();

        if (!password1.equals(password2)) {
            errPasswordText.setText("Hasła nie są takie same.");
        } else {
            try {
                HibernateUtil.getActiveClientAuth().updatePassword(password1);
                errPasswordText.setText("Hasło zostało zmienione.");
                password1Field.clear();
                password2Field.clear();

            } catch (ClientCredentialsException e) {
                errPasswordText.setText(e.getMessage());
            }
        }
    }

    private void backgroundColor() {
        colorChange.setVisible(true);
        stackPane.setVisible(true);
    }

    @FXML
    protected void onAccountDestroyPress(ActionEvent event) {
        backgroundColor();
        destroyAcc.setVisible(true);
    }

    @FXML
    protected void onUsernameChangePress(ActionEvent event) {
        backgroundColor();
        newUserName.setVisible(true);
    }

    @FXML
    protected void exitEvent(ActionEvent event) {
        newUserName.setVisible(false);
        newPassword.setVisible(false);
        destroyAcc.setVisible(false);
        colorChange.setVisible(false);
        stackPane.setVisible(false);
    }
}
