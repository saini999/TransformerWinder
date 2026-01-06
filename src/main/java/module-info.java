module com.nas.tfwind.transformerwinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fazecast.jSerialComm;
    requires org.kordamp.bootstrapfx.core;
    requires org.slf4j;
    requires j2mod;
    //opens com.nas.tfwind.transformerwinder to javafx.fxml;
    //exports com.nas.tfwind.transformerwinder;
    exports com.nas.tfwind.transformerwinder.model;
    opens com.nas.tfwind.transformerwinder.model to javafx.fxml;
    exports com.nas.tfwind.transformerwinder.ui;
    opens com.nas.tfwind.transformerwinder.ui to javafx.fxml;
    exports com.nas.tfwind.transformerwinder.modbus;
    opens com.nas.tfwind.transformerwinder.modbus to javafx.fxml;
    exports com.nas.tfwind.transformerwinder.logicHandlers;
    opens com.nas.tfwind.transformerwinder.logicHandlers to javafx.fxml;
}