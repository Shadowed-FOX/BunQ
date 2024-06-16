package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Text name, funds;
    @FXML
    private StackPane pane;

    private MainController mainController;
    private Client currentClient;

    @FXML
    protected void onLogOutBtnPress(ActionEvent event) throws IOException {
        mainController.loadSignIn();
    }

    @FXML
    protected void onSummaryPress(ActionEvent event) throws IOException {
        loadSummary(currentClient);
    }

    @FXML
    protected void onNewTransactionPress(ActionEvent event) throws IOException {
        loadNewTransaction(currentClient);
    }

    @FXML
    public void initialize() throws IOException {
        loadSummary(currentClient);
    }

    @FXML
    public void loadSummary(Client client) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/acc_summary.fxml"));
        Parent child = loader.load();
        pane.getChildren().setAll(child);
    }

    @FXML
    public void loadNewTransaction(Client client) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/new_transaction.fxml"));
        Parent child = loader.load();
        pane.getChildren().setAll(child);
    }

    public void setCurrentClient(Client currentClient){
        this.currentClient=currentClient;
        name.setText("Witaj " + currentClient.getFirstname());
        funds.setText(currentClient.getBalance() + " PLN");
    }

    public void setMainController(MainController mainController){
        this.mainController=mainController;
    }
}
