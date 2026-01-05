package com.nas.tfwind.transformerwinder;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class homeScreenController {
    private final model data = model.getInstance();
    @FXML
    private TabPane tabContainer;
    @FXML
    private Label curTurns, setTurns, ypos, rpm;

    @FXML
    private ProgressBar curSpeed;
    @FXML
    private Slider overSpeed;
    @FXML
    private Button startBtn, stopBtn, resetBtn, negYjog, posYjog;
    @FXML
    private ComboBox<Float> stepSize;
    @FXML
    public void initialize() {
        try {
            AnchorPane jogPage = FXMLLoader.load(getClass().getResource("jog.fxml"));
            tabContainer.getTabs().get(0).setContent(jogPage);
            AnchorPane programPage = FXMLLoader.load(getClass().getResource("programSetup.fxml"));
            tabContainer.getTabs().get(1).setContent(programPage);
            AnchorPane settingsPage = FXMLLoader.load(getClass().getResource("deviceSetup.fxml"));
            tabContainer.getTabs().get(2).setContent(settingsPage);
            curTurns.textProperty().bind(data.curTurns.asString());
            setTurns.textProperty().bind(data.setTurns.asString());
            rpm.textProperty().bind(data.rpm.asString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

