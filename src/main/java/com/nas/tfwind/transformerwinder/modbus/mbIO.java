package com.nas.tfwind.transformerwinder.modbus;

import com.nas.tfwind.transformerwinder.logicHandlers.LogicScheduler;
import com.nas.tfwind.transformerwinder.model.model;

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

    private boolean sync;
    private void updateModbus() {
        if(!modbus.isConnected()){
            scheduler.shutdownNow();
            LogicScheduler.getInstance().stop();
            return;
        }
        sync = !sync;
        try {
            System.out.println("Start Sync");
            buildDataBlock();
            modbus.syncData();
            updateUi();
            System.out.println("Sync Complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildDataBlock() {
        modbus.setBitInReg(0, 0, sync);
        modbus.setFloat(2, 15.0f);//setTurns
        modbus.setFloat(6, 12.0f);//SetYPos
        modbus.writeReg(12, 0);//speed
        modbus.writeReg(13, 0);//power
        modbus.writeReg(14, 1600);//Encoder Res
        modbus.writeReg(15, 200);//Step Per Rev
        modbus.writeReg(16, 10);//Step Per Rev
        modbus.setFloat(18, 1.0f);
        if(!settingUpdated){
            modbus.setBitInReg(0, 6, true);
        }
    }

    boolean settingUpdated = false;

    private void updateUi() {

        data.ui.setRpm(modbus.getFloat(10));
        System.out.println("RPM:" + modbus.getFloat(10));
        data.ui.setCurTurns(modbus.getFloat(4));
        System.out.println("Turn:" + modbus.getFloat(4));
        System.out.println("SYNC LED: " + modbus.getBitInReg(0,0));
    }

}
