package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.maintenance.SceneLoader;
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
        load("acc_summary");
    }

    public void loadNewTransaction() throws IOException {
        load("new_transaction");
    }

    public void loadTransactionsHistory() throws IOException {
        load("transactions_history");
    }

    public void loadAccManage() throws IOException {
        load("acc_manage");
    }

    public void load(String filename) throws IOException {
        Parent child = SceneLoader.getPane(filename);
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

    @FXML
    public void initialize() throws IOException {
        refreshInfo();
        loadSummary();
    }
}
