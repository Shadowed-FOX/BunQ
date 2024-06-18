package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.database.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionHistoryController {
    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Text senderID, recieverID, tranTitle, tranDate, tranAmmount, tranID;
    @FXML
    private Label senderCred, recieverCred;
    @FXML
    private AnchorPane tranInfo;


    @FXML
    public void initialize() {
        List<Transaction> transactionList = HibernateUtil.getActiveClient().getTransactions();

        for (Transaction transaction : transactionList) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/last_transaction.fxml"));
                AnchorPane pane = loader.load();
                LastTransactionController controller = loader.getController();
                controller.getData(transaction);
                setupPane(pane,transaction);
                vBox.getChildren().add(pane);
            } catch (ClientNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
            scrollPane.setContent(vBox);
        }
    }

    private void setupPane(AnchorPane pane, Transaction transaction){
        pane.setOnMousePressed(event -> {
            int sendID = transaction.getSenderId(), recID = transaction.getReceiverId();
            tranInfo.setVisible(true);

            try {
                String SenderCred = HibernateUtil.getClient(sendID).getFirstname() + HibernateUtil.getClient(sendID).getLastname();
                String RecieverCred = HibernateUtil.getClient(recID).getFirstname() + HibernateUtil.getClient(recID).getLastname();

                senderCred.setText(SenderCred);
                senderID.setText(String.valueOf(sendID));
                recieverCred.setText(RecieverCred);
                recieverID.setText(String.valueOf(recID));
                tranTitle.setText(transaction.getTitle());

                String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(transaction.getDate());
                tranDate.setText(date);

                tranAmmount.setText(String.valueOf(transaction.getAmount()));
                tranID.setText(String.valueOf(transaction.getId()));
            } catch (ClientNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void exitEvent(ActionEvent event){
        tranInfo.setVisible(false);
    }

}
