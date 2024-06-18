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
    private AnchorPane bar, colorChange;
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
            if(check.isSelected() && HibernateUtil.getClient(Accnumb).isOpen()) {
                HibernateUtil.getActiveClient().saveReceiver(Accnumb);
            }
            HibernateUtil.getActiveClient().makeTransaction(Ammount, HibernateUtil.getClient(Accnumb), Title);
            errField.setText("Transakcja się powiodła.");
            accnumb.clear();
            title.clear();
            ammount.clear();
        } catch (Exception ex) {
            errField.setText(ex.getMessage());
        }
    }

    @FXML
    protected void onFromListClicked() throws IOException {
        bar.setVisible(true);
        colorChange.setVisible(true);
        scrollPane.setVisible(true);
        contactGenerate();
    }

    protected void contactGenerate() throws IOException {
        vBox.getChildren().clear();
        List<ClientReceiver> contacts = HibernateUtil.getActiveClient().getSavedReceivers();

        for(ClientReceiver receiver : contacts){
            if(receiver.getReceiverId() == HibernateUtil.getActiveClient().getId()){
                continue;
            }

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/contact.fxml"));
            Parent child = loader.load();
            ContactController controller = loader.getController();
            controller.setNewTransactionController(this);
            controller.getData(receiver);
            vBox.getChildren().add(child);
            paneSetup((AnchorPane) child, receiver);
        }
        scrollPane.setContent(vBox);
    }

    private void paneSetup(AnchorPane pane, ClientReceiver receiver){
        pane.setOnMouseClicked(e -> {
            accnumb.setText(String.valueOf(receiver.getReceiverId()));
            close();
        });
    }

    @FXML
    public void initialize(){
        CredentialSetup();
        AmmountSetup();
    }

    private void AmmountSetup(){
        ammount.textProperty().addListener((obs,old,niu) -> {
            if(!niu.matches("\\d{0,5}(\\.\\d{0,2})?")){
                ammount.setText(old);
            }
        });
    }

    private void CredentialSetup(){
        accnumb.textProperty().addListener((obs,old,niu) -> {
            if(niu.length() > 8) {
                accnumb.setText(old);
            }

            if(!niu.matches("\\d*\\.?\\d*")){
                accnumb.setText(old);
            }

            String str = accnumb.getText();
            int val;
            try {
                if(!str.isEmpty()) {
                    val = Integer.parseInt(str);
                    name.setText(HibernateUtil.getClient(val).getFirstname());
                    surname.setText(HibernateUtil.getClient(val).getLastname());
                } else {
                    name.setText("");
                    surname.setText("");
                }
            } catch (ClientNotFoundException e) {
                name.setText("");
                surname.setText("");
            }
        });
    }

    protected void close(){
        bar.setVisible(false);
        colorChange.setVisible(false);
        scrollPane.setVisible(false);
    }

    @FXML
    protected void exitEvent() {
        close();
    }
}
