package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    protected void onHistoryPress(ActionEvent event) throws IOException {
        loadTransactionsHistory(currentClient);
    }

    @FXML
    protected void onAccManagePress(ActionEvent event) throws IOException {
        loadAccManage(currentClient);
    }

    public void loadSummary(Client current) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/acc_summary.fxml"));
        Parent child = loader.load();
        SummaryController summaryController = loader.getController();
        summaryController.setCurrentClient(current);
        pane.getChildren().setAll(child);
    }

    public void loadNewTransaction(Client client) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/new_transaction.fxml"));
        Parent child = loader.load();
        NewTransactionController newTransactionController = loader.getController();
        newTransactionController.setCurrentClient(client);
        pane.getChildren().setAll(child);
    }

    public void loadTransactionsHistory(Client current) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/transactions_history.fxml"));
        Parent child = loader.load();
        TransactionHistoryController transactionHistoryController = loader.getController();
        transactionHistoryController.setCurrentClient(current);
        pane.getChildren().setAll(child);
    }

    public void loadAccManage(Client client) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/acc_manage.fxml"));
        Parent child = loader.load();
        pane.getChildren().setAll(child);
    }

    public void setCurrentClient(Client client) throws IOException {
        this.currentClient=client;
        name.setText("Witaj " + currentClient.getFirstname());
        funds.setText(currentClient.getBalance() + " PLN");
        postInitialize();
    }

    public void setMainController(MainController mainController){
        this.mainController=mainController;
    }

    private void postInitialize() throws IOException{
        loadSummary(currentClient);
    }
}
