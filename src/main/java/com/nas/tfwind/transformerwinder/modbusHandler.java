package com.nas.tfwind.transformerwinder;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class modbusHandler {
    private static modbusHandler instance;
    private ModbusSerialMaster master;
    private final SerialParameters params = new SerialParameters();
    private int slaveId = 1;


    private modbusHandler() {}


    public static synchronized modbusHandler getInstance() {
        if (instance == null) {
            instance = new modbusHandler();
        }
        return instance;
    }


    public boolean connect(String port, int baud, int timeout) {
        try {
            params.setPortName(port);
            params.setBaudRate(baud);
            params.setDatabits(8);
            params.setStopbits(1);
            params.setParity("None");
            params.setEncoding("rtu");
            params.setEcho(false);

            master = new ModbusSerialMaster(params, timeout);
            master.connect();
            System.out.println("✅ Modbus connected on " + port);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to connect Modbus: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (master != null && master.isConnected()) {
                master.disconnect();
                System.out.println("🔌 Modbus disconnected.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Disconnect error: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return master != null && master.isConnected();
    }

    public void setSlaveId(int id) {
        this.slaveId = id;
    }

    // ======================
    //  Read Operations
    // ======================

    public boolean getCoil(int addr) throws ModbusException {
        ensureConnected();
        return master.readCoils(slaveId, addr, 1).getBit(0);
    }

    public int getHreg(int addr) throws ModbusException {
        ensureConnected();
        var resp = master.readMultipleRegisters(slaveId, addr, 1);
        return resp[0].getValue();
    }

    // ======================
    //  Write Operations
    // ======================

    public void setCoil(int addr, boolean val) throws ModbusException {
        ensureConnected();
        master.writeCoil(slaveId, addr, val);
    }

    public void setHreg(int addr, int val) throws ModbusException {
        ensureConnected();
        Register[] reg = { new SimpleRegister(val) };
        master.writeMultipleRegisters(slaveId, addr, reg);
    }

    // ======================
    //  Internal Helper
    // ======================
    private void ensureConnected() throws ModbusException {
        if (master == null || !master.isConnected()) {
            throw new ModbusException("Modbus not connected");
        }
    }
}
