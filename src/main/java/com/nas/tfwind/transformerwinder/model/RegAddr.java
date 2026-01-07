package com.nas.tfwind.transformerwinder.model;

public enum RegAddr {
    CONTROL(0),
    FEEDBACK(1),
    SET_TURNS(2),
    CUR_TURNS(4),
    SET_YPOS(6),
    CUR_YPOS(8),
    RPM(10),
    SPEED(12),
    POWER(13),
    ENC_RES(14),
    STEP_RES(15),
    SCREW_PITCH(16),
    GEAR_RATIO(18);
    public final int addr;
    RegAddr(int addr){
        this.addr = addr;
    }
}
