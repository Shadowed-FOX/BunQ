package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.fxml.FXML;

public class ManageController {
    @FXML
    private Text firstName, surName, accId, usernameField;

    public void postInitialize() {
        Client client = HibernateUtil.getActiveClient();
        firstName.setText(client.getFirstname());
        surName.setText(client.getLastname());
        accId.setText(String.valueOf(client.getId()));
    }

    @FXML
    protected void exitEvent(ActionEvent event) {
        System.out.println("XDDDD");
    }
}
