package com.nas.tfwind.transformerwinder.modbus;

import com.nas.tfwind.transformerwinder.logicHandlers.LogicScheduler;
import com.nas.tfwind.transformerwinder.model.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class mbIO {
    private final modbusHandler modbus;
    private final ScheduledExecutorService scheduler;

    private final model data = model.getInstance();

    public mbIO(modbusHandler modbus){
        this.modbus = modbus;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void runModbusTask() {
        scheduler.scheduleAtFixedRate(this::updateModbus, 0, 100, TimeUnit.MILLISECONDS);
    }
    public void stopModbusTask() {
        scheduler.shutdownNow();
    }


    private void updateModbus() {
        if(!modbus.isConnected()){
            scheduler.shutdownNow();
            LogicScheduler.getInstance().stop();
            return;
        }
        data.control.sync = !data.control.sync;
        try {
            System.out.println("Start Sync");
            buildDataBlock();
            modbus.syncData();
            handleResponse();
            System.out.println("Sync Complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean setZero = false;

    private void buildDataBlock() {
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.SYNC.addr, data.control.sync);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.MOVESTEP.addr, data.control.moveStep);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.ZEROSTEP.addr, data.control.setStepZero);
        if(data.control.setStepZero && !setZero){
            data.reg.setYPos = 0f;
            data.reg.curYPos = 0f;
            setZero = true;
        }
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.RESETENC.addr, data.control.resetEnc);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.DIR_MOTOR.addr, data.control.motorDir);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.RUN_MOTOR.addr, data.control.runMotor);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.UPDATE_PARAMS.addr, data.control.updateSettings);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.RUN_REF.addr, data.control.runRef);
        modbus.setBitInReg(RegAddr.CONTROL.addr, ControlAddr.INVERT_STEP.addr, data.control.invertStepDir);
        modbus.setFloat(RegAddr.SET_TURNS.addr, data.reg.setTurns);
        modbus.setFloat(RegAddr.SET_YPOS.addr, data.reg.setYPos);
        modbus.writeReg(RegAddr.SPEED.addr, data.reg.speed);
        modbus.writeReg(RegAddr.ENC_RES.addr, data.reg.encRes);
        modbus.writeReg(RegAddr.STEP_RES.addr, data.reg.stepPerRev);
        modbus.setFloat(RegAddr.SCREW_PITCH.addr, data.reg.screwPitch);
        modbus.setFloat(RegAddr.GEAR_RATIO.addr, data.reg.gearRatio);
    }

    private void updateUi() {
        data.ui.setRpm(modbus.getFloat(RegAddr.RPM.addr));
        data.ui.setCurTurns(modbus.getFloat(RegAddr.CUR_TURNS.addr));
        data.ui.setSetTurns(modbus.getFloat(RegAddr.SET_TURNS.addr));
        data.ui.setShowSpeed(modbus.getReg(RegAddr.SPEED.addr));
        data.ui.showYpos(modbus.getFloat(RegAddr.CUR_YPOS.addr));
        data.reg.rpm = modbus.getFloat(RegAddr.RPM.addr);
        data.reg.curTurns = modbus.getFloat(RegAddr.CUR_TURNS.addr);
        data.reg.curYPos = modbus.getFloat(RegAddr.CUR_YPOS.addr);
    }

    private void handleResponse() {
        data.control.updateSettings = modbus.getBitInReg(RegAddr.CONTROL.addr, ControlAddr.UPDATE_PARAMS.addr);
        data.control.setStepZero = modbus.getBitInReg(RegAddr.CONTROL.addr, ControlAddr.ZEROSTEP.addr);
        data.feedback.stepDone = modbus.getBitInReg(RegAddr.FEEDBACK.addr, FeedbackAddr.STEP_DONE.addr);
        if(setZero){
            data.reg.curYPos = 0f;
            data.reg.setYPos = 0f;
            setZero = false;
        }
        updateUi();
        //System.out.println("SetY: " + data.reg.setYPos);
        //System.out.println("CurY: " + data.reg.curYPos);
    }

}
