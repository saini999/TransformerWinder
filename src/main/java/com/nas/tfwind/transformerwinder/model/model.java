package com.nas.tfwind.transformerwinder.model;

public class model {
    private static final model INSTANCE = new model();
    public static model getInstance() { return INSTANCE; }
    private model() {}
    public boolean runMachine = false, appRevSpindleDir = false, saveData = false, isConnected=false, stepperReady=false, reverseWind=false, resumeCurrent=false, enableJog = false;
    public float accelration=0f, deccelration=0f, maxSpeed=0f, startPower=0f, wireSize=0f, bobenLength=0f, workOffset=0f;
    /* Coils */
    public final ControlReg control = new ControlReg();
    public final FeedbackReg feedback = new FeedbackReg();
    /* Registers */
    public final Registers reg = new Registers();
    /* UI Fields */
    public final uiModel ui = new uiModel();

}
