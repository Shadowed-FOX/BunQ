package com.blackfox.bunq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.blackfox.bunq.database.*;
import java.sql.Timestamp;
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
            System.out.println("balance: "+client.getBalance());
            System.out.println("id: "+client.getId());
            System.out.println("created_at: " + client.getCreatedAt());

            var transactions = HibernateUtil.getTransactionList(client.getId());
            int i=1;
            for (var transaction : transactions) {
                System.out.println(i+". transaction:");
                System.out.println(transaction.getTitle());
                System.out.println(transaction.getAmount() + "zł");
                System.out.println(transaction.getDate());
                System.out.println((transaction.getSenderId() == client.getId()) ? "debit" : "credit");
                System.out.println();
                i++;
            }
        } catch (ClientNotFoundException ex) {
            System.err.println(ex);
        }
    }
    static void transcationsExampleeeee(int idSender,int intReciver,float ammount,String title)
    {
        try {
            HibernateUtil.getClient(idSender).transferMoney(ammount, HibernateUtil.getClient(intReciver),title);
        } catch (ClientNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {

        // try {
        //     // int clientId = HibernateUtil.createClient("cebulla", "cebulion", "Andzej",
        //     // "Grochowalski");
        //     fetchingExample("cebulla", "cebulion");
        // } catch (Exception ex) {
        //     System.err.println(ex);
        // }

        // try {
        // ClientAuth cAuth = HibernateUtil.getClientAuth("johnik");
        // cAuth.setPassword("1234qwer");
        // cAuth.update();
        // } catch (Exception ex) {
        // System.err.println(ex);
        // }
         

     //   transcationsExampleeeee(75601458, 19517347, -20f, "ciekawe czy sam tytul mozna wyslac");
 //fetchingExample("Dziady", "123dziady");
 fetchingExample("Dziady", "123dziady");
        launch();
        HibernateUtil.close();
    }
}
