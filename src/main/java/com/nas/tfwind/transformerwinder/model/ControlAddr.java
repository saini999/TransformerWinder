package com.nas.tfwind.transformerwinder.model;

public enum ControlAddr {
    SYNC(0),
    MOVESTEP(1),
    ZEROSTEP(2),
    RESETENC(3),
    DIR_MOTOR(4),
    RUN_MOTOR(5),
    UPDATE_PARAMS(6),
    RUN_REF(7),
    INVERT_STEP(8);

    public final int addr;

    ControlAddr(int addr) {
        this.addr = addr;
    }
}