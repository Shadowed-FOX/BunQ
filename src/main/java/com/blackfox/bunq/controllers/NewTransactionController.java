package com.blackfox.bunq.controllers;

import com.blackfox.bunq.Main;
import com.blackfox.bunq.database.ClientNotFoundException;
import com.blackfox.bunq.database.ClientReceiver;
import com.blackfox.bunq.database.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.annotations.Check;

import java.io.IOException;
import java.util.List;


public class NewTransactionController {
    @FXML
    private TextField accnumb, title, ammount, name, surname;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane bar;
    @FXML
    private VBox vBox;
    @FXML
    private CheckBox check;
    @FXML
    private Text errField;

    @FXML
    protected void onAcceptPaymentPress(ActionEvent event) throws Exception {
        int Accnumb = Integer.parseInt(accnumb.getText());
        String Title = title.getText();
        float Ammount = Float.parseFloat(ammount.getText());

        try {
            if(check.isSelected()) {
                HibernateUtil.getActiveClient().saveReceiver(Accnumb);
            }
            HibernateUtil.getActiveClient().makeTransaction(Ammount, HibernateUtil.getClient(Accnumb), Title);
        } catch (Exception ex) {
            errField.setText(ex.getMessage());
        }
    }

    @FXML
    protected void onFromListClicked(ActionEvent event) throws IOException {
        bar.setVisible(true);
        scrollPane.setVisible(true);
        vBox.getChildren().clear();
        List<ClientReceiver> contacts = HibernateUtil.getActiveClient().getSavedReceivers();

        for(ClientReceiver receiver : contacts){
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/contact.fxml"));
            Parent child = loader.load();
            ContactController controller = loader.getController();
            controller.getData(receiver.getReceiverAsClient());
            vBox.getChildren().add(child);
        }
        scrollPane.setContent(vBox);
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
        bar.setVisible(false);
        scrollPane.setVisible(false);
    }
}
