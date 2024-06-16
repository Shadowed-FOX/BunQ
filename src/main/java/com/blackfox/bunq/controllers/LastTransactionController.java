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

public class LastTransactionController {
    @FXML
    private Text reciever, data, kwota;

    public void getData(Transaction transaction) throws ClientNotFoundException {
        Client client;
        String sign = "";

        if (transaction.getSenderId() == HibernateUtil.getActiveClient().getId()) {
            client = HibernateUtil.getClient(transaction.getReceiverId());
            sign = "-";
        } else {
            client = HibernateUtil.getClient(transaction.getSenderId());
            sign = "+";
        }

        Timestamp Date = transaction.getDate();
        String str = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float ammount = transaction.getAmount();
        df.format(ammount);

        reciever.setText(client.getFirstname() + " " + client.getLastname());
        data.setText(str);
        kwota.setText(sign + transaction.getAmount() + " PLN");
    }
}
