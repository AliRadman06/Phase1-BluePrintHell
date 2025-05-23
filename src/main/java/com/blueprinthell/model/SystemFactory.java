package com.blueprinthell.model;

public class SystemFactory {
    public static NetworkDevice createSystem(SystemType type,
                                             String id,
                                             double x, double y) {
        return switch (type) {
            case START      -> new StartSystem(id, x, y);
            case PROCESSING -> new ProcessingSystem(id, x, y);
            case END        -> new EndSystem(id, x, y);
        };
    }
}
