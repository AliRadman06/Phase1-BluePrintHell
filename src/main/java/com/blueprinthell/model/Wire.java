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

    //نهایی کردن پورت مقصد
    public void setInputPort(Port inputPort) {
        this.inputPort = inputPort;
    }

    //ذخیره مختصات موس هنگام درگ
    public void setTempEnd(Point2D p) {
        this.tempEndX = p.getX();
        this.tempEndY = p.getY();
    }

    //مرکز پورت خروجی و نقطه ی شروع
    public Point2D getStart() {
        return outputPort.getCenter();
    }


    public Point2D getEnd() {
        if(inputPort != null) {
            return inputPort.getCenter();
        }
        else {
            return new Point2D(tempEndX, tempEndY);
        }
    }

    public double calculateLength() {
        return getStart().distance(getEnd());
    }

    public boolean isWithinBudget(double remainingWire) {
        return calculateLength() <= remainingWire;
    }

    public Port getInputPort() {
        return inputPort;
    }

    public Port getOutputPort() {
        return outputPort;
    }

}
