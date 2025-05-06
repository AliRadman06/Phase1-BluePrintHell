package com.blueprinthell.model;

/** سیستم پردازش: دریافت و پردازش Packet */
public class ProcessingSystem extends NetworkDevice {
    public ProcessingSystem(String id, double x, double y) {
        super(id, x, y);
        
    }

    @Override
    public SystemType getType() {
        return SystemType.PROCESSING;
    }

    @Override
    public void initialize() {
        // TODO: آماده‌سازی اولیه
    }

    @Override
    public void update(double deltaTime) {
        // TODO: منطقِ پردازشِ Packetهای وارد شده
    }
}
