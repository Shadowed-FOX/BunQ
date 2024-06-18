package com.blackfox.bunq.controllers;

import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class NewTransactionController {
    @FXML
    private TextField accnumb, title, ammount, name, surname;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;

    @FXML
    protected void onAcceptPaymentPress(ActionEvent event) throws Exception {
        int Accnumb = Integer.parseInt(accnumb.getText());
        String Title = title.getText();
        float Ammount = Float.parseFloat(ammount.getText());

        try {
            HibernateUtil.getActiveClient().makeTransaction(Ammount, HibernateUtil.getClient(Accnumb), Title);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    protected void onFromListClicked(ActionEvent event){
        scrollPane.setVisible(true);


    }

    @FXML
    public void initialize(){
        CredentialSetup();
    }

    private void CredentialSetup(){
        accnumb.textProperty().addListener((obs,old,niu) -> {
            String str = accnumb.getText();

            if(niu.length() > 8) {
                str = str.substring(0, 8);
                accnumb.setText(str);
            }

            int val;
            try {
                if(!str.isEmpty()) {
                    val = Integer.parseInt(str);
                    name.setText(HibernateUtil.getClient(val).getFirstname());
                    surname.setText(HibernateUtil.getClient(val).getLastname());
                }
            } catch (ClientNotFoundException e) {
                name.setText("");
                surname.setText("");
            }
        });
    }

    @FXML
    protected void exitEvent() {
        scrollPane.setVisible(false);
    }
}
