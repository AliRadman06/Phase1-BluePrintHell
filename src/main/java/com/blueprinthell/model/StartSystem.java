package com.blueprinthell.model;

import com.blueprinthell.logic.GameStats;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class StartSystem extends NetworkDevice {

    private int squarePacket = 0;
    private int trianglePacket = 0;
    private final Queue<Packet> packetBuffer = new LinkedList<>();


    public StartSystem(String id, double x, double y) {
        super(id, x, y);
    }

    public void setSquarePacket(int packet) {
        this.squarePacket = packet;
        GameStats.registerPacket(packet);
    }

    public void setTrianglePacket(int packet) {
        this.trianglePacket = packet;
        GameStats.registerPacket(packet);

    }

    public int getSquarePacket() {
        return this.squarePacket;
    }

    public int getTrianglePacket() {
        return this.trianglePacket;
    }

    public void generateInitialPackets(List<Point2D> path) {
        for (int i = 0; i < squarePacket; i++) {
            Packet p = new Packet(Packet.ShapeType.SQUARE, 2);
            p.setPath(path);
            packetBuffer.add(p);

        }

        for (int i = 0; i < trianglePacket; i++) {
            Packet p = new Packet(Packet.ShapeType.TRIANGLE, 3);
            p.setPath(path);
            packetBuffer.add(p);
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
