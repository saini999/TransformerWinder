package com.nas.tfwind.transformerwinder;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class model {
    private static final model INSTANCE = new model();
    public static model getInstance() { return INSTANCE; }
    private model() {}
    /* Coils */


    /* 16-bit Int Regs */
    public final IntegerProperty curTurns = new SimpleIntegerProperty(0);
    public final IntegerProperty setTurns = new SimpleIntegerProperty(0);
    public final IntegerProperty rpm = new SimpleIntegerProperty(0);

    /* 32-bit Int/Float Regs */



    //Functions

    public void setCurTurns(int v){
        runFx(() -> curTurns.set(v));
    }
    public void setSetTurns(int v){
        runFx(() -> setTurns.set(v));
    }
    public void setRpm(int v){
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
