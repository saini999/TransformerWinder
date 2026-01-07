package com.nas.tfwind.transformerwinder.logicHandlers;

import com.nas.tfwind.transformerwinder.model.model;

public class SpindleHandler {

    private static final SpindleHandler INSTANCE = new SpindleHandler();
    public static SpindleHandler getInstance() {return INSTANCE;}
    private SpindleHandler() {}

    private final model data = model.getInstance();
    public void spindleTask(){
        if(data.runMachine){
            startSpindleProcess();
        } else {
            stopSpindleProcess();
        }
    }

    void startSpindleProcess(){
        if(data.reg.curTurns < data.reg.setTurns){
            data.control.motorDir = !data.appRevSpindleDir;
            data.reg.speed = getSpeed(
                    data.reg.setTurns,
                    data.reg.curTurns,
                    data.accelration,
                    data.deccelration,
                    data.maxSpeed,
                    data.startPower
            );
            data.control.runMotor = true;
        } else {
            data.reg.speed = 0;
            data.control.runMotor = false;
        }
    }

    void stopSpindleProcess(){
        data.reg.speed = 0;
        data.control.runMotor = false;
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }


    public static int getSpeed(
            float setTurns,
            float curTurns,
            float acceleration,   // 0–100 (%)
            float deceleration,   // 0–100 (%)
            float maxSpeed,       // 0–255
            float startPower      // 0–255
    ) {
        // Stop condition
        if (curTurns >= setTurns) {
            return 0;
        }

        // Clamp inputs
        acceleration = clamp(acceleration, 0f, 100f);
        deceleration = clamp(deceleration, 0f, 100f);
        maxSpeed     = clamp(maxSpeed * 255f / 100f, 0f, 255f);
        startPower   = clamp(startPower * 255f / 100f, 0f, maxSpeed);

        // Convert % to ramp lengths (turns)
        float accelTurns = Math.max(1f, setTurns * (100f - acceleration) / 200f);
        float decelTurns = Math.max(1f, setTurns * (100f - deceleration) / 200f);

        float pwm;

        // ACCELERATION
        if (curTurns < accelTurns) {
            float t = curTurns / accelTurns;      // 0 → 1
            float s = smoothStep(t);               // S-curve
            pwm = startPower + (maxSpeed - startPower) * s;
        }

        // DECELERATION
        else if (curTurns > setTurns - decelTurns) {
            float t = (setTurns - curTurns) / decelTurns;  // 1 → 0
            float s = smoothStep(t);
            pwm = startPower + (maxSpeed - startPower) * s;
        }

        // CONSTANT SPEED
        else {
            pwm = maxSpeed;
        }

        return (int) clamp(pwm, 0f, 255f);
    }
    private static float smoothStep(float t) {
        t = clamp(t, 0f, 1f);
        return t * t * (3f - 2f * t);
    }



}
