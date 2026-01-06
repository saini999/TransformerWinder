package com.nas.tfwind.transformerwinder.model;

import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class uiModel {
    public FloatProperty curTurns = new SimpleFloatProperty(0);
    public FloatProperty setTurns = new SimpleFloatProperty(0);
    public FloatProperty rpm = new SimpleFloatProperty(0);

    //Functions

    public void setCurTurns(float v){
        runFx(() -> curTurns.set(v));
    }
    public void setSetTurns(float v){
        runFx(() -> setTurns.set(v));
    }
    public void setRpm(float v){
        runFx(() -> rpm.set(v));
    }
    /* HELPER */
    private void runFx(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }
}
