package com.blueprinthell.model;

public class StartSystem extends NetworkDevice {

    public StartSystem(String id, double x, double y) {
        super(id, x, y);
    }

    @Override
    public SystemType getSystemType() {
        return SystemType.Start;
    }

    @Override
    public void initialize() {} // تولید پکت واسه این سیستم

    @Override
    public void update(double deltaTime) {}  // اپدیت هر فریم

}
