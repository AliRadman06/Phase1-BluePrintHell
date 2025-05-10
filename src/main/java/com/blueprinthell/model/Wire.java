package com.blueprinthell.model;

import javafx.geometry.Point2D;

public class Wire {
    private final Port outputPort;
    private Port inputPort;

    private double tempEndX, tempEndY;

    public Wire(Port outputPort) {
        this.outputPort = outputPort;
        this.inputPort = null;
    }

    public void setInputPort(Port inputPort) {
        this.inputPort = inputPort;
    }

    public void setTempEnd(Point2D p) {
        this.tempEndX = p.getX();
        this.tempEndY = p.getY();
    }

//    public Point2D getStart() {
//        return outputPort.
//    }
}
