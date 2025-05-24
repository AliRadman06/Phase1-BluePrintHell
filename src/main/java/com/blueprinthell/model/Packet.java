package com.blueprinthell.model;

import com.blueprinthell.logic.GameStats;
import javafx.geometry.Point2D;

import java.util.List;

public class Packet {
    public enum ShapeType { SQUARE, TRIANGLE }
    private ShapeType shape;
    private double size;
    private boolean alive = true;

    private List<Point2D> path;
    private int currentIndex = 0;
    private double subProgress = 0.0;

    private double speed;
    private double noise = 0.0;

    private Port destinationPort;
    private static int counter = 0;
    private final int id;
    private static final double DETECTION_RADIUS = 30.0;
    private double deviation = 0;


    public Packet(ShapeType shape, double size) {
        this.shape = shape;
        this.size = size;
        this.id = counter++;

    }

    public void advance(double dist) {
        while (dist > 0 && currentIndex < path.size() - 1) {
            Point2D a = path.get(currentIndex);
            Point2D b = path.get(currentIndex + 1);
            double segLen = a.distance(b);
            double remain = (1 - subProgress) * segLen;
            if (dist < remain) {
                subProgress += dist / segLen;
                dist = 0;
            } else {
                dist -= remain;
                currentIndex++;
                subProgress = 0;
            }

        }
    }

    public Point2D getCurrentPosition() {
        if (currentIndex >= path.size() - 1) {
            return path.get(path.size() - 1);
        }
        Point2D a = path.get(currentIndex);
        Point2D b = path.get(currentIndex + 1);
        return new Point2D(
                a.getX() + (b.getX() - a.getX()) * subProgress,
                a.getY() + (b.getY() - a.getY()) * subProgress
        );
    }

    public ShapeType getShape() { return shape; }
    public double getSize()       { return switch (shape) {
        case SQUARE -> 2.0;
        case TRIANGLE -> 3.0;
    };
    }
    public double getNoise()      { return noise; }
    public double getSpeed()      { return speed; }
    public void   setSpeed(double speed) { this.speed = speed; }
    public void setPath(List<Point2D> path) { this.path = path; }
    public List<Point2D> getPath() { return path; }
    public int getCurrentIndex() { return currentIndex; }
    public void setDestinationPort(Port port) {
        this.destinationPort = port;
    }
    public int getId() { return id; }

    public double getRadiusOfDetection() {
        return DETECTION_RADIUS;
    }

    public Port getDestinationPort() {
        return destinationPort;
    }

    public double getDeviation() {
        return deviation;
    }

    public boolean isFinished() {
        return currentIndex >= path.size() - 1;
    }

    public void resetProgress() {
        this.currentIndex = 0;
        this.subProgress = 0.0;
    }

    public void increaseNoise(double amount) {
        this.noise += amount;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        if (!alive) return;
        alive = false;
        GameStats.addPacketLoss();
    }


    public void addDeviation(double delta) {
        this.deviation += delta;
        double maxDeviation = getSize() * 10.0;

        if (deviation >= maxDeviation) {
            kill();
        }
    }
    public void setNoise(double noise) {
        this.noise = noise;
    }

}
