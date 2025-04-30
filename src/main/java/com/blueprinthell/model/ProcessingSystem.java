package com.blueprinthell.model;

public class ProcessingSystem extends NetworkDevice {

    public ProcessingSystem(String id, double x, double y) {
        super(id, x, y);
    }

    @Override
    public SystemType getSystemType() {
        return SystemType.Process;
    }

    @Override
    public void initialize() {  }

    @Override
    public void update(double deltaTime) {}
}
