package com.nas.tfwind.transformerwinder;

import com.ghgande.j2mod.modbus.ModbusException;
import javafx.fxml.FXML;

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
            return;
        }
        sync = !sync;
        try {
            System.out.println("Start Sync");
            buildDataBlock();
            modbus.syncData();
            System.out.println("SYNC LED: " + modbus.getBitInReg(0,0));
            System.out.println("Sync Complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildDataBlock() throws ModbusException {
        modbus.setBitInReg(0, 0, sync);
    }

}
