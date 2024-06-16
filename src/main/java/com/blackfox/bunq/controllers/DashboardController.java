package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.HibernateUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    @FXML
    protected void onLogOutBtnPress(ActionEvent event) throws IOException {
        mainController.loadSignIn();
    }

    @FXML
    protected void onSummaryPress(ActionEvent event) throws IOException {
        loadSummary();
    }

    @FXML
    protected void onNewTransactionPress(ActionEvent event) throws IOException {
        loadNewTransaction();
    }

    @FXML
    protected void onHistoryPress(ActionEvent event) throws IOException {
        loadTransactionsHistory();
    }

    @FXML
    protected void onAccManagePress(ActionEvent event) throws IOException {
        loadAccManage();
    }

    public void loadSummary() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/acc_summary.fxml"));
        Parent child = loader.load();
        SummaryController summaryController = loader.getController();
        summaryController.postInitialize();
        refreshInfo();
        pane.getChildren().setAll(child);
    }

    public void loadNewTransaction() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/new_transaction.fxml"));
        Parent child = loader.load();
        refreshInfo();
        pane.getChildren().setAll(child);
    }

    public void loadTransactionsHistory() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/transactions_history.fxml"));
        Parent child = loader.load();
        TransactionHistoryController transactionHistoryController = loader.getController();
        transactionHistoryController.postInitialize();
        refreshInfo();
        pane.getChildren().setAll(child);
    }

    public void loadAccManage() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/acc_manage.fxml"));
        Parent child = loader.load();
        ManageController manageController = loader.getController();
        manageController.postInitialize();
        refreshInfo();
        pane.getChildren().setAll(child);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void refreshInfo() {
        name.setText("Witaj " + HibernateUtil.getActiveClient().getFirstname());
        funds.setText(HibernateUtil.getActiveClient().getBalance() + " PLN");
    }

    public void postInitialize() throws IOException {
        refreshInfo();
        loadSummary();
    }
}
