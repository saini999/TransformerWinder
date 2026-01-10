package com.nas.tfwind.transformerwinder.ui;
import com.fazecast.jSerialComm.SerialPort;
import com.nas.tfwind.transformerwinder.logicHandlers.LogicScheduler;
import com.nas.tfwind.transformerwinder.modbus.mbIO;
import com.nas.tfwind.transformerwinder.modbus.modbusHandler;
import com.nas.tfwind.transformerwinder.model.AppSettings;
import com.nas.tfwind.transformerwinder.model.model;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
public class deviceSetupController {

    model data = model.getInstance();

    public class SerialUtils {
        public static List<String> getAvailablePorts() {
            List<String> ports = new ArrayList<>();
            for (SerialPort port : SerialPort.getCommPorts()) {
                ports.add(port.getSystemPortName());
            }
            return ports;
        }
    }

    @FXML
    private ComboBox<String> portList;
    @FXML
    private ComboBox<Integer> baudList;
    @FXML
    private Label modBusStatus;
    @FXML
    private Button connectBtn;

    @FXML
    private TextField encRes, stepPerRev, screwPitch, gearRatio, acceleration, deceleration;


    @FXML
    public void initialize() {
        refreshPorts();
        updateBaudList();
        modbusHandler modbus = modbusHandler.getInstance();

        boolean isConnected = modbus.isConnected();
        updateStatus(isConnected);
        setupNumberFields();
        data.reg.encRes = AppSettings.getEnc();
        data.reg.stepPerRev = AppSettings.getSteps();
        data.reg.screwPitch = AppSettings.getScrew();
        data.reg.gearRatio = AppSettings.getGear();
        data.accelration = AppSettings.getAccel();
        data.deccelration = AppSettings.getDecel();
        encRes.setText(String.valueOf(data.reg.encRes));
        stepPerRev.setText(String.valueOf(data.reg.stepPerRev));
        screwPitch.setText(String.valueOf(data.reg.screwPitch));
        gearRatio.setText(String.valueOf(data.reg.gearRatio));
        acceleration.setText(String.valueOf(data.accelration));
        deceleration.setText(String.valueOf(data.deccelration));
        data.saveData = true;
    }

    private void updateStatus(boolean isConnected) {
        if(isConnected){
            connectBtn.setText("Disconnect");
            connectBtn.getStyleClass().clear();
            connectBtn.getStyleClass().add("btn-danger");
            connectBtn.getStyleClass().add("btn");
            modBusStatus.setText("Status: ✅ Connected!");

        } else {
            connectBtn.setText("Connect");
            connectBtn.getStyleClass().clear();
            connectBtn.getStyleClass().add("btn-success");
            connectBtn.getStyleClass().add("btn");
            modBusStatus.setText("Status: ❌ Disconnected.");
        }
    }

    @FXML
    private void refreshPorts(){
        portList.getItems().clear();
        portList.getItems().addAll(SerialUtils.getAvailablePorts());
    }

    boolean firstRun = true;

    @FXML
    private void connectModbus(){
        String selectedPort = portList.getValue();
        int selectedBaud = baudList.getValue() == null ? 0 : baudList.getValue();
        if (selectedPort == null) {
            System.out.println("Please select a port first!");
            modBusStatus.setText("Please select a port first!");
            return;
        }
        if(selectedBaud == 0){
            System.out.println("Please select a Baud Rate");
            modBusStatus.setText("Please select a Baud Rate");
            return;
        }
        modbusHandler modbus = modbusHandler.getInstance();
        mbIO mbio = new mbIO(modbus);
        boolean isConnected = modbus.isConnected();
        if(isConnected){
            mbio.stopModbusTask();
            LogicScheduler.getInstance().stop();
            data.isConnected = false;
            modbus.disconnect();
            data.ui.setIsConnected(false);
        }
        else {
            boolean connected = modbus.connect(selectedPort, selectedBaud, 1000);
            System.out.println(connected ? "✅ Connected!" : "❌ Failed to connect");
            if(connected) {
                mbio.runModbusTask();
                data.isConnected = true;
                LogicScheduler.getInstance().start();
                data.ui.setIsConnected(true);
            }
        }
        isConnected = modbus.isConnected();
        updateStatus(isConnected);
    }

    @FXML
    private void runSave(){
        data.reg.encRes = Integer.parseInt(encRes.getText());
        data.reg.stepPerRev = Integer.parseInt(stepPerRev.getText());
        data.reg.screwPitch = Float.parseFloat(screwPitch.getText());
        data.reg.gearRatio = Float.parseFloat(gearRatio.getText());
        data.accelration  = Math.min(Float.parseFloat(acceleration.getText()), 100.0f);
        data.deccelration = Math.min(Float.parseFloat(deceleration.getText()), 100.0f);
        data.saveData = true;
        AppSettings.saveEnc(data.reg.encRes);
        AppSettings.saveSteps(data.reg.stepPerRev);
        AppSettings.saveScrew(data.reg.screwPitch);
        AppSettings.saveGear(data.reg.gearRatio);
        AppSettings.saveAccel(data.accelration);
        AppSettings.saveDecel(data.deccelration);
    }

    private void setupNumberFields(){
        encRes.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        stepPerRev.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        screwPitch.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        gearRatio.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        acceleration.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
        deceleration.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        ));
    }


    private void updateBaudList(){
        int baud[] = {110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200};
        baudList.getItems().clear();
        for (int j : baud) {
            baudList.getItems().add(j);
        }
    }
}


