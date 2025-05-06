package com.blueprinthell.model;

/** سیستم شروع: تولیدکنندهٔ اولیهٔ Packet */
public class StartSystem extends NetworkDevice {
    public StartSystem(String id, double x, double y) {
        super(id, x, y);
    }

    @Override
    public SystemType getType() {
        return SystemType.START;
    }

    @Override
    public void initialize() {
        // TODO: تولید Packet اولیه و افزودن به outPorts
    }

    @Override
    public void update(double deltaTime) {
        // TODO: منطق فرستادن Packetها از outPorts
    }
}
