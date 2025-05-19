package com.blueprinthell.model;

import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** سیستم شروع: تولیدکنندهٔ اولیهٔ Packet */
public class StartSystem extends NetworkDevice {

    private int squarePacket = 0;
    private int trianglePacket = 0;
    private final Queue<Packet> packetBuffer = new LinkedList<>();


    public StartSystem(String id, double x, double y) {
        super(id, x, y);
    }

    public void setSquarePacket(int packet) {
        this.squarePacket = packet;
    }

    public void setTrianglePacket(int packet) {
        this.trianglePacket = packet;
    }

    public void generateInitialPackets(List<Point2D> path) {
        for (int i = 0; i < squarePacket; i++) {
            packetBuffer.add(new Packet(Packet.ShapeType.SQUARE, 2, path, 0));
        }
        for (int i = 0; i < trianglePacket; i++) {
            packetBuffer.add(new Packet(Packet.ShapeType.TRIANGLE, 3, path, 0));
        }
    }

    public Queue<Packet> getPacketBuffer() {
        return packetBuffer;
    }

    @Override
    public SystemType getType() {
        return SystemType.START;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(double deltaTime) {
    }
}
