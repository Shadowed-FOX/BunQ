package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ContactController {
    @FXML
    private Text name, id;

    public void getData(Client reciever){
        name.setText(reciever.getFirstname() + " " + reciever.getLastname());
        id.setText(String.valueOf(reciever.getId()));
    }
}
