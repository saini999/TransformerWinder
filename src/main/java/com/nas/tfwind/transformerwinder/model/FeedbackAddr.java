package com.nas.tfwind.transformerwinder.model;

public enum FeedbackAddr {
    STEP_DONE(0),
    AT_ZERO(1),
    ENC_AT_ZERO(2),
    MOTOR_RUNNING(3),
    PARAMS_SAVED(4),
    REF_DONE(5);

    public final int addr;

    FeedbackAddr(int addr) {
        this.addr = addr;
    }
}