package com.blueprinthell.model;

/** سیستم شروع: تولیدکنندهٔ اولیهٔ Packet */
public class StartSystem extends NetworkDevice {
    public StartSystem(String id, double x, double y) {
        super(id, x, y);
        // مثال: هر سیستم یک پورت IN و یک پورت OUT داشته باشد:
        inPorts .add(new Port(this, Port.Direction.IN));
        outPorts.add(new Port(this, Port.Direction.OUT));
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
