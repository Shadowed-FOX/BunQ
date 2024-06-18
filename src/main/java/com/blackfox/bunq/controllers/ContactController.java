package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.ClientReceiver;
import com.blackfox.bunq.database.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactController {
    @FXML
    private Text name, id;

    private NewTransactionController newTransactionController;
    private ClientReceiver clientReciever;

    public void setNewTransactionController(NewTransactionController newTransactionController){
        this.newTransactionController=newTransactionController;
    }

    public void getData(ClientReceiver reciever){
        Client clientReciever = reciever.getReceiverAsClient();
        this.clientReciever = reciever;
        name.setText(clientReciever.getFirstname() + " " + clientReciever.getLastname());
        id.setText(String.valueOf(clientReciever.getId()));
    }

    @FXML
    protected void onGetFromContactList() throws ClientNotFoundException, IOException {;
        HibernateUtil.getActiveClient().removeReceiver(clientReciever);
        newTransactionController.contactGenerate();
    }
}
