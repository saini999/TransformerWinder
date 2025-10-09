module com.nas.tfwind.transformerwinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fazecast.jSerialComm;
    requires org.kordamp.bootstrapfx.core;
    requires org.slf4j;
    requires j2mod;
    opens com.nas.tfwind.transformerwinder to javafx.fxml;
    exports com.nas.tfwind.transformerwinder;
}