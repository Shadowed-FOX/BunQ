package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import com.blackfox.bunq.database.Transaction;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

public class LastTransactionController {
    @FXML
    private Text reciever, data, kwota;
    private Client currentClient;

    public void getData(Transaction transaction) throws ClientNotFoundException {
        Client client = new Client();
        if(transaction.getSenderId() == currentClient.getId()){
            client = HibernateUtil.getClient(transaction.getReceiverId());
        } else {
            client = HibernateUtil.getClient(transaction.getSenderId());
        }

        Timestamp Date = transaction.getDate();
        String str = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float ammount = transaction.getAmount();
        df.format(ammount);
        String temp = "";
        if(transaction.getAmount() > 0){
            temp = "+";
        }
        reciever.setText(client.getFirstname() + " " + client.getLastname());
        data.setText(str);
        kwota.setText(temp+transaction.getAmount());
    }

    public void setCurrentClient(Client client){
        this.currentClient=client;
    }
}
