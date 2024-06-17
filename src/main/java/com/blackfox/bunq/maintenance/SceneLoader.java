package com.blackfox.bunq.maintenance;

import com.blackfox.bunq.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class SceneLoader {
    public static Parent getPane(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("./view/" + filename + ".fxml"));
        return loader.load();
    }
}
