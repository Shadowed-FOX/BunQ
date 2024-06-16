package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.fxml.FXML;


public class ManageController {
    @FXML
    private Text firstName, surName, accId, usernameField;

    private Client currentClient;

    public void postInitialize(){
        currentClient=HibernateUtil.getActiveClient();

        firstName.setText(currentClient.getFirstname());
        surName.setText(currentClient.getLastname());
        accId.setText(String.valueOf(currentClient.getId()));
    }

    @FXML
    protected void exitEvent(ActionEvent event){
        System.out.println("XDDDD");
    }
}
