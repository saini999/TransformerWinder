package com.nas.tfwind.transformerwinder.logicHandlers;

import com.nas.tfwind.transformerwinder.model.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class LogicScheduler {
    private final ScheduledExecutorService executor;
    private LogicScheduler() {
        this.executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "LogicScheduler");
            t.setDaemon(true);
            return t;
        });
    }
    private static final LogicScheduler INSTANCE = new LogicScheduler();
    public static LogicScheduler getInstance() { return INSTANCE; }
    private volatile boolean running = false;
    public synchronized void start() {
        if (running) return;
        executor.scheduleAtFixedRate(this::safeRun, 0, 20, TimeUnit.MILLISECONDS);
        running = true;
    }

    public synchronized void stop() {
        if (!running) return;
        executor.shutdownNow();
        running = false;
    }

    private void safeRun()  {
        try {
            runTasks();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void runTasks() throws InterruptedException {
        if(!model.getInstance().isConnected){
            executor.shutdownNow();
            running = false;
        }
        if(model.getInstance().saveData){
            model.getInstance().control.updateSettings = true;
        }
        SpindleHandler.getInstance().spindleTask();
        StepperHandler.getInstance().runStepperTask();


        //at last after all tasks complete
        if(model.getInstance().saveData){
            model.getInstance().saveData = false;
        }
    }
}
