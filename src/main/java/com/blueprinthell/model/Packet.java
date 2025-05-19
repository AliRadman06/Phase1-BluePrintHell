package com.blueprinthell.model;

import javafx.geometry.Point2D;

import java.util.List;

public class Packet {
    public enum ShapeType { SQUARE, TRIANGLE }
    private ShapeType shape;
    private double size; // 2 یا 3

    private List<Point2D> path;
    private int currentIndex = 0;
    private double subProgress = 0.0;

    private double speed;
    private double noise = 0.0;

    private Port destinationPort; // ✅ مقصد نهایی پکت


    public Packet(ShapeType shape, double size, List<Point2D> path, double initialSpeed) {
        this.shape = shape;
        this.size = size;
        this.path = path;
        this.speed = initialSpeed;
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
    public double getSize()       { return size;  }
    public double getNoise()      { return noise; }
    public void   addNoise(double delta) { noise += delta; }
    public double getSpeed()      { return speed; }
    public void   setSpeed(double speed) { this.speed = speed; }
    public void setPath(List<Point2D> path) { this.path = path; }
    public List<Point2D> getPath() { return path; }
    public int getCurrentIndex() { return currentIndex; }
    public void setDestinationPort(Port port) {
        this.destinationPort = port;
    }

    public Port getDestinationPort() {
        return destinationPort;
    }

    public boolean isFinished() {
        return currentIndex >= path.size() - 1;
    }



}
