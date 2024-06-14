package com.blackfox.bunq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.blackfox.bunq.database.*;
import com.blackfox.bunq.database.Client.TransactionType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 600);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        fetchingExample("Dziady", "123dziady");

        launch();
        HibernateUtil.close();
    }

    static void fetchingExample(String username, String password) {
        try {
            ClientAuth clientAuth = HibernateUtil.getClientAuth(username);
            System.out.println("username: " + clientAuth.getUsername());
            System.out.println("checkPassword: " + clientAuth.checkPassword(password));
            // clientAuth.setPassword("haslo123");
            // clientAuth.update();

            Client client = HibernateUtil.getClient(clientAuth.getId());
            System.out.println("firstname: " + client.getFirstname());
            System.out.println("lastname: " + client.getLastname());
            System.out.println("balance: " + client.getBalance());
            System.out.println("id: " + client.getId());
            System.out.println("created_at: " + client.getCreatedAt());

            // try {
            //     HibernateUtil.getClient(55406669).transferMoney(50, client, "urodziny 2");
            // } catch (Exception ex) {
            //     System.err.println(ex);
            // }

            var transactions = client.getTransactions(TransactionType.RECEIVED);

            for (int i = 0; i < transactions.size(); i++) {
                System.out.println(i + ". transaction:");
                System.out.println(transactions.get(i).getTitle());
                System.out.println(transactions.get(i).getAmount() + "zł");
                System.out.println(transactions.get(i).getDate());
                System.out.println((transactions.get(i).getSenderId() == client.getId()) ? "debit" : "credit");
                System.out.println();
            }
        } catch (ClientNotFoundException ex) {
            System.err.println(ex);
        }
    }

    static void transcationsExample(int idSender, int intReciver, float ammount, String title) {
        try {
            HibernateUtil.getClient(idSender).transferMoney(ammount, HibernateUtil.getClient(intReciver), title);
        } catch (ClientNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // try {
    // // int clientId = HibernateUtil.createClient("cebulla", "cebulion", "Andzej",
    // // "Grochowalski");
    // fetchingExample("cebulla", "cebulion");
    // } catch (Exception ex) {
    // System.err.println(ex);
    // }

    // try {
    // ClientAuth cAuth = HibernateUtil.getClientAuth("johnik");
    // cAuth.setPassword("1234qwer");
    // cAuth.update();
    // } catch (Exception ex) {
    // System.err.println(ex);
    // }

    // transcationsExample(75601458, 19517347, 20, "kwota na cos");
    // fetchingExample("Dziady", "123dziady");
    // fetchingExample("Dziady", "123dziady");
}
