package com.blueprinthell.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * مدلِ پورتِ شبکه: IN یا OUT با صف داخلی
 */
public class Port {

    public enum Direction { IN, OUT }

    private final NetworkDevice owner;
    private final Direction direction;
    private final Queue<Packet> buffer = new LinkedList<>();
    private final int capacity = 5;

    public Port(NetworkDevice owner, Direction direction) {
        this.owner     = owner;
        this.direction = direction;
    }

    public Direction getDirection() { return direction; }

    public boolean enqueue(Packet p) {
        if (buffer.size() >= capacity) return false; // Packet Lost
        return buffer.offer(p);
    }

    public Packet dequeue() {
        return buffer.poll();
    }
}
