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
    private CheckBox reverseWind, resumeCurrent;
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
            ypos.textProperty().bind(Bindings.format("%.2f",data.ui.yPos));
            curSpeed.progressProperty().bind(data.ui.showSpeed);
            startBtn.disableProperty().set(true);
            stopBtn.disableProperty().set(true);
            resetBtn.disableProperty().set(true);
            data.ui.isConnected.addListener((obs, wasConnected, isConnected) -> {
                if(isConnected){
                    connected();
                } else {
                    disConnected();
                }
            });
            reverseWind.selectedProperty().set(false);
            resumeCurrent.selectedProperty().set(false);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void runMachine(){
        data.runMachine = true;
        uiUpdateOnRunMachine();
    }
    @FXML
    void stopMachine(){
        data.runMachine = false;
        uiUpdateOnRunMachine();
    }

    @FXML
    void reverseWind() {
        data.reverseWind = reverseWind.isSelected();
    }
    @FXML
    void resumeCurrent(){
        data.resumeCurrent = resumeCurrent.isSelected();
    }
    public void connected(){
        uiUpdateOnRunMachine();
    }

    private void uiUpdateOnRunMachine() {
        if(data.runMachine){
            startBtn.setText("Running");
            startBtn.disableProperty().set(true);
            stopBtn.disableProperty().set(false);
            stopBtn.setText("Stop");
            reverseWind.disableProperty().set(true);
            resumeCurrent.disableProperty().set(true);
        } else {
            startBtn.setText("Start");
            startBtn.disableProperty().set(false);
            stopBtn.disableProperty().set(true);
            stopBtn.setText("Stopped");
            reverseWind.disableProperty().set(false);
            resumeCurrent.disableProperty().set(false);
        }
    }

    public void disConnected() {
        startBtn.disableProperty().set(true);
        stopBtn.disableProperty().set(true);
        resetBtn.disableProperty().set(true);
    }


}

