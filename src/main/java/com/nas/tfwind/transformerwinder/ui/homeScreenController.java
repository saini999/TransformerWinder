package com.nas.tfwind.transformerwinder.ui;


import com.nas.tfwind.transformerwinder.model.model;
import javafx.beans.binding.Bindings;
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
            AnchorPane jogPage = FXMLLoader.load(getClass().getResource("/com/nas/tfwind/transformerwinder/ui/jog.fxml"));
            tabContainer.getTabs().get(0).setContent(jogPage);
            AnchorPane programPage = FXMLLoader.load(getClass().getResource("/com/nas/tfwind/transformerwinder/ui/programSetup.fxml"));
            tabContainer.getTabs().get(1).setContent(programPage);
            AnchorPane settingsPage = FXMLLoader.load(getClass().getResource("/com/nas/tfwind/transformerwinder/ui/deviceSetup.fxml"));
            tabContainer.getTabs().get(2).setContent(settingsPage);
            curTurns.textProperty().bind(Bindings.format("%.2f",data.ui.curTurns));
            setTurns.textProperty().bind(Bindings.format("%.2f",data.ui.setTurns));
            rpm.textProperty().bind(Bindings.format("%.2f",data.ui.rpm));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

