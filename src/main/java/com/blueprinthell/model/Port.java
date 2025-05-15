package com.blueprinthell.model;

import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.Queue;

/**
 * مدلِ پورتِ شبکه: IN یا OUT با صف داخلی
 */
public class  Port {

    public enum Direction { IN, OUT }
    public enum Shape{ SQUARE, TRIANGLE }

    private final NetworkDevice owner;
    private final Direction direction;
    private final Queue<Packet> buffer = new LinkedList<>();
    private final int capacity = 5;
    private final Shape shape;
    private double relativeY = -1 ;
    private Point2D Center;

    public Port(NetworkDevice owner, Direction direction, Shape shape) {
        this.owner     = owner;
        this.direction = direction;
        this.shape  = shape;
    }

    public Direction getDirection() { return direction; }

    public boolean enqueue(Packet p) {
        if (buffer.size() >= capacity) return false; // Packet Lost
        return buffer.offer(p);
    }

    public Packet dequeue() {
        return buffer.poll();
    }

    public Shape getShape() {
        return shape;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(double y) {
        this.relativeY = y;
    }

    public NetworkDevice getOwner() { return owner; }

    public void setCenter() {

        double baseX = owner.getX();
        double baseY = owner.getY();

        double w = 200;
        double h = 200;

        double x = baseX + (direction == Direction.OUT ? w  : 0);
        double y = baseY + relativeY * h;

        this.Center = new Point2D(x, y);
    }

    public Point2D getCenter() {
        return Center;
    }


}
