package com.blueprinthell.model;

public class EndSystem extends NetworkDevice {
    public EndSystem(String id, double x, double y) {
        super(id, x, y);
    }

    @Override
    public SystemType getType() {
        return SystemType.END;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(double deltaTime) {
    }
}
