/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latcyrconverter;

import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Gile1974
 */
public class LatCyrConverter extends Application {
    
      @Override
      public void start(Stage stage) throws Exception {
        String version = this.getClass().getPackage().getImplementationVersion();
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("LatCyrConverter.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Converter-" + version);
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/resources/image/Converter.png")));
        stage.setResizable(true);
    }

    public static void main(String[] args) {
        LatCyrConverter.launch((String[])args);
    }
    
}
