package com.nas.tfwind.transformerwinder;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class modbusHandler {

    //       CONFIG

    private static final int REG_START_ADDR = 0;
    //REG 0 and REG 1 are Control Registers 16 + 16 Bits acts as 32 Coils

    private static final int REG_COUNT = 20;

    private static modbusHandler instance;

    public static synchronized modbusHandler getInstance() {
        if (instance == null) {
            instance = new modbusHandler();
        }
        return instance;
    }

    private modbusHandler() {}

    // MB CORE

    private ModbusSerialMaster master;
    private final SerialParameters params = new SerialParameters();
    private int slaveId = 1;

    private final int[] txRegs = new int[REG_COUNT]; // what we send
    private final int[] rxRegs = new int[REG_COUNT]; // what we receive

    // Connections
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

            System.out.println("Modbus connected on " + port);
            return true;
        } catch (Exception e) {
            System.err.println("Modbus connect failed: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        if (master != null && master.isConnected()) {
            master.disconnect();
        }
    }

    public boolean isConnected() {
        return master != null && master.isConnected();
    }

    public void setSlaveId(int id) {
        this.slaveId = id;
    }

    // MEMORY HELPERS

    // Write full register (TX only)
    public void writeReg(int regIndex, int value) {
        if (!validReg(regIndex)) return;
        txRegs[regIndex] = value & 0xFFFF;
    }

    // Read full register (RX only)
    public int getReg(int regIndex) {
        if (!validReg(regIndex)) return 0;
        return rxRegs[regIndex];
    }

    // Set a single bit inside a register (TX only)
    public void setBitInReg(int regIndex, int bitIndex, boolean value) {
        if (!validReg(regIndex) || bitIndex < 0 || bitIndex > 15) return;

        int mask = 1 << bitIndex;
        if (value) {
            txRegs[regIndex] |= mask;
        } else {
            txRegs[regIndex] &= ~mask;
        }
    }

    // Get a single bit inside a register (RX only)
    public boolean getBitInReg(int regIndex, int bitIndex) {
        if (!validReg(regIndex) || bitIndex < 0 || bitIndex > 15) return false;
        return (rxRegs[regIndex] & (1 << bitIndex)) != 0;
    }

    // clear TX register
    public void clearTxReg(int regIndex) {
        if (!validReg(regIndex)) return;
        txRegs[regIndex] = 0;
    }

    // Sync Modbus Call

    private void dumpTxRegs(Register[] reg) {
        System.out.println("TX REGISTERS:");
        for (int i = 0; i < REG_COUNT; i++) {
            int v = reg[i].getValue() & 0xFFFF;
            System.out.printf("  R[%02d] = %5d (0x%04X) bits=%16s%n", i, v, v, String.format("%16s", Integer.toBinaryString(v)).replace(' ', '0')
            );
        }
    }

    private void dumpRxRegs(Register[] reg) {
        System.out.println("RX REGISTERS:");
        for (int i = 0; i < REG_COUNT; i++) {
            int v = reg[i].getValue() & 0xFFFF;
            System.out.printf("  R[%02d] = %5d (0x%04X) bits=%16s%n", i, v, v, String.format("%16s", Integer.toBinaryString(v)).replace(' ', '0')
            );
        }
    }


    public void setReg0(int val) throws ModbusException {
        Register[] r = { new SimpleRegister(0)};
        r[0].setValue(val);
        master.writeMultipleRegisters(slaveId, REG_START_ADDR, r);
    }
    public int getReg0() throws ModbusException {
        Register[] r = master.readMultipleRegisters(slaveId, REG_START_ADDR, REG_COUNT);
        return r[0].getValue();
    }

    public synchronized void syncData() throws ModbusException {
        ensureConnected();

        // WRITE ALL REGISTERS
        Register[] writeRegs = new Register[REG_COUNT];
        for (int i = 0; i < REG_COUNT; i++) {
            SimpleRegister r = new SimpleRegister(0);
            r.setValue(txRegs[i] & 0xFFFF);   // 🔥 CRITICAL FIX
            writeRegs[i] = r;
        }
        dumpTxRegs(writeRegs);
        master.writeMultipleRegisters(slaveId, REG_START_ADDR, writeRegs);

        // READ ALL REGISTERS
        Register[] readRegs =
                master.readMultipleRegisters(slaveId, REG_START_ADDR, REG_COUNT);

        for (int i = 0; i < REG_COUNT; i++) {
            rxRegs[i] = readRegs[i].getValue();
        }
        dumpRxRegs(readRegs);
    }


    // other Helpers

    private boolean validReg(int idx) {
        return idx >= 0 && idx < REG_COUNT;
    }
    public void setFloat(int regIdx, float value) {
        int bits = Float.floatToIntBits(value);

        txRegs[regIdx]     = (bits >>> 16) & 0xFFFF;
        txRegs[regIdx + 1] =  bits & 0xFFFF;
    }
    public float getFloat(int regIdx) {
        int bits = (rxRegs[regIdx] << 16) | (rxRegs[regIdx + 1] & 0xFFFF);
        return Float.intBitsToFloat(bits);
    }

    private void ensureConnected() throws ModbusException {
        if (master == null || !master.isConnected()) {
            throw new ModbusException("Modbus not connected");
        }
    }
}
