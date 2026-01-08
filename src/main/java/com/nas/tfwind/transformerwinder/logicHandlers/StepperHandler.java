package com.nas.tfwind.transformerwinder.logicHandlers;

import com.nas.tfwind.transformerwinder.model.model;

public class StepperHandler {
    private final model data = model.getInstance();
    private final static StepperHandler INSTANCE = new StepperHandler();
    public static StepperHandler getInstance() { return INSTANCE;}
    private StepperHandler() {}

    public void runStepperTask() throws InterruptedException {
        //System.out.println("Running StepperTask");
        if (data.enableJog){
            if(Math.abs(data.reg.curYPos - data.reg.setYPos) <= 0.001f) {
                data.control.moveStep = true;
                data.stepperReady = false;
                //System.out.println("Moving Stepper");
            }
            return;
        }

        if (!data.runMachine) {
            data.control.moveStep = false;
            data.stepperReady = false;
            return;
        }

        boolean canResume =
                data.resumeCurrent &&
                        data.reg.curYPos >= data.workOffset &&
                        data.reg.curYPos <= (data.workOffset + data.bobenLength);

        if (!data.stepperReady) {

            // Resume immediately
            if (canResume) {
                data.stepperReady = true;
            }
            // Go to work offset
            else {
                data.reg.setYPos = data.workOffset;
                data.control.moveStep = true;

                Thread.sleep(1000);
                if (data.feedback.stepDone) {
                    data.stepperReady = true;
                    data.control.moveStep = false;
                }
                return;
            }
        }

        float travel = data.reg.curTurns * data.wireSize;
        float effectiveLen = data.bobenLength;
        float cycleLen = 2f * effectiveLen;

        float startOffset = canResume
                ? Math.max(0f, Math.min(
                data.reg.curYPos - data.workOffset, effectiveLen))
                : 0f;

        if (data.reverseWind) {
            travel = cycleLen - travel;
        }

        float posInCycle = (travel + startOffset) % cycleLen;
        if (posInCycle < 0f) {
            posInCycle += cycleLen;
        }

        float pos = (posInCycle <= effectiveLen)
                ? posInCycle
                : cycleLen - posInCycle;

        data.reg.setYPos = data.workOffset + pos;

        final float POS_EPS = 0.001f;
         if(Math.abs(data.reg.setYPos - data.reg.curYPos) > POS_EPS){
             data.control.moveStep = true;
         } else if (data.feedback.stepDone) {
             data.control.moveStep = false;
         }
    }

}
