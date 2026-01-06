package com.nas.tfwind.transformerwinder.ui;
import com.fazecast.jSerialComm.SerialPort;
import com.nas.tfwind.transformerwinder.logicHandlers.LogicScheduler;
import com.nas.tfwind.transformerwinder.modbus.mbIO;
import com.nas.tfwind.transformerwinder.modbus.modbusHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
public class deviceSetupController {

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
    public void initialize() {
        refreshPorts();
        updateBaudList();
        modbusHandler modbus = modbusHandler.getInstance();

        boolean isConnected = modbus.isConnected();
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
            modbus.disconnect();
        }
        else {
            boolean connected = modbus.connect(selectedPort, selectedBaud, 1000);
            System.out.println(connected ? "✅ Connected!" : "❌ Failed to connect");
            mbio.runModbusTask();
            LogicScheduler.getInstance().start();
        }
        isConnected = modbus.isConnected();
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

    private void updateBaudList(){
        int baud[] = {110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200};
        baudList.getItems().clear();
        for (int j : baud) {
            baudList.getItems().add(j);
        }
    }
}


