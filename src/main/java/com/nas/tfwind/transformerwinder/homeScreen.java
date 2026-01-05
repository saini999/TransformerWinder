package com.nas.tfwind.transformerwinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class homeScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(homeScreen.class.getResource("homeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 360);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("NAS: Transformer Winder");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/app_icon.jpg")));
        stage.setResizable(false);
        stage.setWidth(680);
        stage.setHeight(360);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();



    }
}
