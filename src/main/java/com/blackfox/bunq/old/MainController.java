package com.blackfox.bunq.old;

import com.blackfox.bunq.database.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button closeBtn;
    @FXML
    private VBox window;
    @FXML
    private Text name, funds;
    private Client currentClient;

    private double screenX=0;
    private double screenY=0;

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

    @FXML
    protected void onLogOutBtnPress(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        SceneController.getInstance().switchSceneLogin(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void setCurrentClient(Client client){
        this.currentClient = client;
        name.setText("Hi " + currentClient.getFirstname() + "!");
        funds.setText(currentClient.getBalance() + " PLN");
    }
}
