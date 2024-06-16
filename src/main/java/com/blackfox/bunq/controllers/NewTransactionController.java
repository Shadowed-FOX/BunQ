package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewTransactionController {
    @FXML
    private TextField accnumb, title, ammount, name, surname;
    @FXML
    private Button closeBtn;

    @FXML
    protected void onAcceptPaymentPress() throws Exception {
        int Accnumb = Integer.parseInt(accnumb.getText());
        String Title = title.getText();
        float Ammount = Float.parseFloat(ammount.getText());
        String Name = name.getText();
        String Surname = surname.getText();

        try {
            HibernateUtil.getActiveClient().makeTransaction(Ammount, HibernateUtil.getClient(Accnumb), Title);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @FXML
    protected void exitEvent() {
        System.out.println("XDDDD");
    }
}
