package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.database.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionHistoryController {
    @FXML
    private VBox vBox;
    private Client currentClient;

    public void setCurrentClient(Client currentClient){
        this.currentClient=currentClient;
        postInitialize();
    }

    public void postInitialize() {
        List<Transaction> transactionList = currentClient.getTransactions();

        for (Transaction transaction : transactionList) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/last_transaction.fxml"));
                AnchorPane pane = loader.load();

                LastTransactionController controller = loader.getController();
                controller.setCurrentClient(currentClient);
                controller.getData(transaction);
                vBox.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClientNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
