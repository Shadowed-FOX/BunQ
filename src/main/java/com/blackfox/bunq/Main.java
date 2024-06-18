package com.blackfox.bunq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.blackfox.bunq.database.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Parent root = FxmlLoader.getPane("main");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/main.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root, 960, 600);

        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("BunQ");
        stage.show();
    }

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        Logger.getLogger("com.mchange").setLevel(Level.WARNING);
        HibernateUtil.getSessionFactory();
                   
           
           
        
            
        // fetchingExample("Dziady", "123dziady");
      
        launch();
        testWithRecivers();
        HibernateUtil.close();
    }
    public static void testWithRecivers()
    {
        try {
            Client test=  HibernateUtil.getClient(75601458);
                        Client test2=  HibernateUtil.getClient(19517347);
                        
            test.SafeReciver(HibernateUtil.getClient(57161548));
            System.out.println(test.getRecivers().size());
            for(var i : test.getRecivers())
            {
                System.out.println(i.getReceiverId()+" "+i.getReceiverAsClient().getLastname());  
            }
        } catch (ClientNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
