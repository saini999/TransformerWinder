package com.nas.tfwind.transformerwinder.ui;


import com.nas.tfwind.transformerwinder.model.model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


public class jogController {

    @FXML
    Label ss, jg;
    @FXML
    ComboBox<Float> stepSize;
    @FXML
    Button enableJog,yNeg,yPos,setZero,setWork,gotoZero,gotoWork,gotoEnd;
    @FXML
    CheckBox reverseDir;

    model data = model.getInstance();
    float[] stepSizeList = {0.01f,0.1f,0.5f,1.0f,2.0f,5.0f,10.0f,50.0f,100.0f};

    float stepSizeCurrent;
    @FXML
    public void initialize(){
        stepSize.getItems().clear();
        for (float v : stepSizeList) {
            stepSize.getItems().add(v);
        }
        stepSize.setValue(stepSizeList[3]); //1.0f default
        stepSizeCurrent = stepSize.getValue();
        data.enableJog = false;
        disableJog(true);
        data.ui.disableJog.addListener((observableValue, aBoolean, t1) -> {
            if(data.ui.disableJog.getValue()){
                disableJog(true);
                data.enableJog = false;
                enableJog.disableProperty().set(true);
            } else {
                disableJog(false);
                data.enableJog = false;
                enableJog.disableProperty().set(false);
            }
        });
    }

    @FXML
    void enableJog() {
        if(!data.runMachine) {
            if (data.enableJog) {
                data.enableJog = false;
                //System.out.println("Disabling Jog");
                disableJog(true);
            } else {
                data.enableJog = true;
                //System.out.println("Enabling Jog");
                disableJog(false);
            }
            System.out.println("Jog Mode: " + data.enableJog);
        }
    }
    @FXML
    void yNeg() {
        data.reg.setYPos = data.reg.curYPos - stepSizeCurrent;
    }
    @FXML
    void yPos() {
        data.reg.setYPos = data.reg.curYPos + stepSizeCurrent;
    }
    @FXML
    void stepSize() {
        stepSizeCurrent = stepSize.getValue();
    }
    @FXML
    void setZero() {
        data.control.setStepZero = true;
        data.reg.setYPos = 0;
        data.reg.curYPos = 0;
    }
    @FXML
    void setWork() {
        data.workOffset = data.reg.curYPos;
    }
    @FXML
    void gotoZero() {
        data.reg.setYPos = 0;
    }
    @FXML
    void gotoWork() {
        data.reg.setYPos = data.workOffset;
    }
    @FXML
    void gotoEnd() {
        data.reg.setYPos = data.workOffset + data.bobenLength;
    }
    @FXML
    void reverseDir() {
        data.control.invertStepDir = reverseDir.isSelected();
    }

    public void disableJog(boolean v){
        if(v){
            enableJog.setText("Enable Jog");
        } else {
            enableJog.setText("Disable Jog");
        }
        yNeg.disableProperty().set(v);
        yPos.disableProperty().set(v);
        stepSize.disableProperty().set(v);
        setZero.disableProperty().set(v);
        setWork.disableProperty().set(v);
        gotoZero.disableProperty().set(v);
        gotoEnd.disableProperty().set(v);
        gotoWork.disableProperty().set(v);
        reverseDir.disableProperty().set(v);
        ss.disableProperty().set(v);
        jg.disableProperty().set(v);
    }

}
