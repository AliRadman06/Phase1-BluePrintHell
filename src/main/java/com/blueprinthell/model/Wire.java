package com.blueprinthell.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

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

    public List<Point2D> flatten(int steps) {
        // نقاط آغاز و انجام سیم (p0 و p3)
        Point2D p0 = getStart();
        Point2D p3 = getEnd();

        // کنترل‌پوینت‌های منحنی، مطابق WireView
        double ctrlX = (p0.getX() + p3.getX()) / 2;
        Point2D p1 = new Point2D(ctrlX, p0.getY());
        Point2D p2 = new Point2D(ctrlX, p3.getY());

        // لیست خروجی با ظرفیت steps+1
        List<Point2D> pts = new ArrayList<>(steps + 1);

        // نمونه‌برداری پارامتریک بین t=0 تا t=1
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double u = 1 - t;

            // ضرایب بزیهٔ مکعبی
            double b0 = u * u * u;
            double b1 = 3 * u * u * t;
            double b2 = 3 * u * t * t;
            double b3 = t * t * t;

            // ترکیب خطی نقاط با ضرایب بزیه
            double x = b0 * p0.getX()
                    + b1 * p1.getX()
                    + b2 * p2.getX()
                    + b3 * p3.getX();
            double y = b0 * p0.getY()
                    + b1 * p1.getY()
                    + b2 * p2.getY()
                    + b3 * p3.getY();

            // افزودن نقطه به لیست
            pts.add(new Point2D(x, y));
        }

        return pts;
    }

}
