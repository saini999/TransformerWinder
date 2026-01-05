package com.nas.tfwind.transformerwinder;

public class SettingsHandler {

    public static SettingsHandler instance;

    private SettingsHandler() {}

    public static synchronized SettingsHandler getInstance() {
        if (instance == null) {
            instance = new SettingsHandler();
        }
        return instance;
    }



}
