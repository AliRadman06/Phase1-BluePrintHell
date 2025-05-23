package com.blueprinthell.model;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessingSystem extends NetworkDevice {

    private final Queue<Packet> packetBuffer = new LinkedList<>();
    private static final int MAX_BUFFER_SIZE = 5;

    public ProcessingSystem(String id, double x, double y) {
        super(id, x, y);
        
    }

    public boolean addToBuffer(Packet packet) {
        if (packetBuffer.size() < MAX_BUFFER_SIZE) {
            packetBuffer.add(packet);
            return true;
        }
        return false;
    }

    public Queue<Packet> getPacketBuffer() {
        return packetBuffer;
    }

    @Override
    public SystemType getType() {
        return SystemType.PROCESSING;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(double deltaTime) {
    }
}
