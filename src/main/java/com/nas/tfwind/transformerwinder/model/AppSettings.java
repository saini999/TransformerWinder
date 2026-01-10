package com.nas.tfwind.transformerwinder.model;

import java.util.prefs.Preferences;

public class AppSettings {
    private static final Preferences prefs =
            Preferences.userNodeForPackage(AppSettings.class);

    public static void saveEnc(int enc){
        prefs.putInt("encRes", enc);
    }
    public static int getEnc(){
        return prefs.getInt("encRes", 0);
    }
    public static void saveSteps(int v){
        prefs.putInt("steps", v);
    }
    public static int getSteps(){
        return prefs.getInt("steps", 0);
    }
    public static void saveScrew(float v){
        prefs.putFloat("screw", v);
    }
    public static float getScrew(){
        return prefs.getFloat("screw", 0f);
    }
    public static void saveGear(float v){
        prefs.putFloat("gear", v);
    }
    public static float getGear(){
        return prefs.getFloat("gear", 0f);
    }
    public static void saveAccel(float v){
        prefs.putFloat("accel", v);
    }
    public static float getAccel(){
        return prefs.getFloat("accel", 0f);
    }
    public static void saveDecel(float v){
        prefs.putFloat("decel", v);
    }
    public static float getDecel(){
        return prefs.getFloat("decel", 0f);
    }
    public static void saveTurns(float v){
        prefs.putFloat("turns", v);
    }
    public static float getTurns(){
        return prefs.getFloat("turns", 0f);
    }
    public static void saveWireSize(float v){
        prefs.putFloat("wireSize", v);
    }
    public static float getWireSize(){
        return prefs.getFloat("wireSize", 0f);
    }
    public static void saveBobbin(float v){
        prefs.putFloat("bobbin", v);
    }
    public static float getBobbin(){
        return prefs.getFloat("bobbin", 0f);
    }
    public static void saveSpeed(float v){
        prefs.putFloat("speed", v);
    }
    public static float getSpeed(){
        return prefs.getFloat("speed", 0f);
    }
    public static void savePower(float v){
        prefs.putFloat("power", v);
    }
    public static float getPower(){
        return prefs.getFloat("power", 0f);
    }


}
