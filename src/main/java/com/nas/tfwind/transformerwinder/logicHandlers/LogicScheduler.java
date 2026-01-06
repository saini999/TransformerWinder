package com.nas.tfwind.transformerwinder.logicHandlers;

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

    private void safeRun() {
        try {
            runTasks();
        } catch (Throwable t) {
            t.printStackTrace(); // never let scheduler die silently
        }
    }

    void runTasks() {
        // to be imported from other handlers
    }
}
