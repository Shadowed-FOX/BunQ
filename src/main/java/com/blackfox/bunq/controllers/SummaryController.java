package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import javafx.scene.text.Text;
import javafx.fxml.FXML;

public class SummaryController {
    private Client currentClient;
    @FXML
    private Text income;

    @FXML
    public void initialize(){

    }

    public void setCurrentClient(Client client){
        this.currentClient=client;
        income.setText(currentClient.getFirstname());
    }
}
