package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.Client;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewTransactionController {
    @FXML
    private TextField accnumb, title, ammount, name, surname;
    @FXML
    private Button closeBtn;

    private Client currentClient;

    @FXML
    protected void onAcceptPaymentPress() throws Exception {
        int Accnumb = Integer.parseInt(accnumb.getText());
        String Title = title.getText();
        float Ammount = Float.parseFloat(ammount.getText());
        String Name = name.getText();
        String Surname = surname.getText();

        try {
            currentClient.makeTransaction(Ammount, HibernateUtil.getClient(Accnumb), Title);
        } catch (Exception ex){

        }
    }

    @FXML
    protected void exitEvent(){
        System.out.println("XDDDD");
    }

    public void postInitialize(){
        currentClient=HibernateUtil.getActiveClient();
    }

    public void setCurrentClient(Client client){
        this.currentClient=client;
    }
}
