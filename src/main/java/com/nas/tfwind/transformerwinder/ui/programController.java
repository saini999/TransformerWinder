package com.nas.tfwind.transformerwinder.ui;

import com.nas.tfwind.transformerwinder.model.AppSettings;
import com.nas.tfwind.transformerwinder.model.model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.ResourceBundle;

public class programController implements Initializable {
    model data = model.getInstance();
    @FXML
    Button progSaveBtn;

    @FXML
    TextField numTurns, sizeWire, bobenLength;
    @FXML
    Slider maxSpeed, startPower;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        setupNumberFields();
        data.reg.setTurns = AppSettings.getTurns();
        data.maxSpeed = AppSettings.getSpeed();
        data.startPower = AppSettings.getPower();
        data.wireSize = AppSettings.getWireSize();
        data.bobenLength = AppSettings.getBobbin();
        numTurns.setText(String.valueOf(data.reg.setTurns));
        sizeWire.setText(String.valueOf(data.wireSize));
        bobenLength.setText(String.valueOf(data.bobenLength));
        maxSpeed.setValue(data.maxSpeed);
        startPower.setValue(data.startPower);
        data.saveData = true;
    }

    @FXML
    void runSave(){
        data.reg.setTurns = Float.parseFloat(numTurns.getText());
        data.maxSpeed = (float)maxSpeed.getValue();
        data.startPower = (float)startPower.getValue();
        data.wireSize = Float.parseFloat(sizeWire.getText());
        data.bobenLength = Float.parseFloat(bobenLength.getText());
        data.saveData = true;
        AppSettings.saveTurns(data.reg.setTurns);
        AppSettings.saveSpeed(data.maxSpeed);
        AppSettings.savePower(data.startPower);
        AppSettings.saveWireSize(data.wireSize);
        AppSettings.saveBobbin(data.bobenLength);
    }

    void setupNumberFields(){
        numTurns.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        sizeWire.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        bobenLength.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
    }



}
