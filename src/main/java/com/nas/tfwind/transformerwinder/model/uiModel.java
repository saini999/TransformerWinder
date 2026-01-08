package com.nas.tfwind.transformerwinder.model;

import javafx.application.Platform;
import javafx.beans.property.*;

public class uiModel {
    public FloatProperty curTurns = new SimpleFloatProperty(0);
    public FloatProperty setTurns = new SimpleFloatProperty(0);
    public FloatProperty rpm = new SimpleFloatProperty(0);

    public FloatProperty yPos = new SimpleFloatProperty(0);

    public FloatProperty showSpeed = new SimpleFloatProperty(0);

    public BooleanProperty isConnected = new SimpleBooleanProperty(false);
    public BooleanProperty disableJog = new SimpleBooleanProperty(false);





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
    public void showYpos(float v) { runFx(()->yPos.set(v));}
    public void setShowSpeed(float v) {
        if(v<=255.0f){v = v / 255.0f;} else {v=1.0f;}
        final float nv = v;
        runFx(() -> showSpeed.set(nv));
    }



    public void setIsConnected(boolean v){
        runFx(() -> isConnected.set(v));
    }
    public void setDisableJog(boolean v){ runFx(() -> disableJog.set(v));}

    /* HELPER */
    private void runFx(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }
}
