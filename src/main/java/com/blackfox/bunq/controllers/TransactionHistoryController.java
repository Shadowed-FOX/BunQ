package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.database.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TransactionHistoryController {
    @FXML
    private VBox vBox;

    public void postInitialize() {
        List<Transaction> transactionList = HibernateUtil.getActiveClient().getTransactions();

        for (Transaction transaction : transactionList) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/last_transaction.fxml"));
                AnchorPane pane = loader.load();
                LastTransactionController controller = loader.getController();
                controller.getData(transaction);
                vBox.getChildren().add(pane);
            } catch (ClientNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
