package com.blueprinthell.model;

public class SystemFactory {
    public static NetworkDevice createSystem(SystemType type, String id, double x, double y) {
        return switch(type) {
            case Start      -> new StartSystem(id,x,y);
            case Process -> new ProcessingSystem(id,x,y);
            case End        -> new EndSystem(id,x,y);
        };
    }
}