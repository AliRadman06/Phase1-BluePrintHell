package com.blueprinthell.model;

import java.util.ArrayList;
import java.util.List;

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

    private final List<Packet> packetBuffer = new ArrayList<>();

    public void addToBuffer(Packet packet) {
        if (packetBuffer.size() < 5) {
            packetBuffer.add(packet);
        }
    }

    public List<Packet> getPacketBuffer() {
        return packetBuffer;
    }

}
