package com.blueprinthell.model;

/** سیستم پایان: مصرف‌کننده یا آمارگیر Packet */
public class EndSystem extends NetworkDevice {
    public EndSystem(String id, double x, double y) {
        super(id, x, y);
        inPorts .add(new Port(this, Port.Direction.IN));
        outPorts.add(new Port(this, Port.Direction.OUT));
    }

    @Override
    public SystemType getType() {
        return SystemType.END;
    }

    @Override
    public void initialize() {
        // TODO: منطقِ اولیه، مثلاً شمارش Packetها
    }

    @Override
    public void update(double deltaTime) {
        // TODO: منطقِ نهایی، مثلاً پاک‌سازی یا آمارگیری
    }
}
