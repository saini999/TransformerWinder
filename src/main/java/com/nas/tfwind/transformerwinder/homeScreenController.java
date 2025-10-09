package com.nas.tfwind.transformerwinder;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class homeScreenController {

    @FXML
    private TabPane tabContainer;

    @FXML
    public void initialize() {
        try {
            AnchorPane jogPage = FXMLLoader.load(getClass().getResource("jog.fxml"));
            tabContainer.getTabs().get(0).setContent(jogPage);
            AnchorPane programPage = FXMLLoader.load(getClass().getResource("programSetup.fxml"));
            tabContainer.getTabs().get(1).setContent(programPage);
            AnchorPane settingsPage = FXMLLoader.load(getClass().getResource("deviceSetup.fxml"));
            tabContainer.getTabs().get(2).setContent(settingsPage);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

