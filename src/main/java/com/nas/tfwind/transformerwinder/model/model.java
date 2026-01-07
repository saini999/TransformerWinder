package com.nas.tfwind.transformerwinder.model;

public class model {
    private static final model INSTANCE = new model();
    public static model getInstance() { return INSTANCE; }
    private model() {}
    public boolean runMachine = false, appRevSpindleDir, saveData, isConnected, stepperReady, reverseWind, resumeCurrent, enableJog = false;
    public float accelration, deccelration, maxSpeed, startPower, wireSize, bobenLength, workOffset;
    /* Coils */
    public final ControlReg control = new ControlReg();
    public final FeedbackReg feedback = new FeedbackReg();
    /* Registers */
    public final Registers reg = new Registers();
    /* UI Fields */
    public final uiModel ui = new uiModel();

}
