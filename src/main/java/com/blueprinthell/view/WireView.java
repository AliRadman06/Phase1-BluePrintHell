package com.blueprinthell.view;

import com.blueprinthell.model.Wire;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class WireView {
    private final Wire model;
    private final Path curve;

    public WireView(Wire model) {
        this.model = model;
        this.curve = new Path();
        this.curve.setStrokeWidth(2);
        this.curve.setStroke(Color.GREEN);
        updateShape();

    }

    public Path getCurve() {
        return curve;
    }

    public void updateShape() {
        curve.getElements().clear();
        Point2D p0 = model.getStart();
        Point2D p3 = model.getEnd();
        double ctrlX = (p0.getX() + p3.getX())/2;
        Point2D p1 = new Point2D(ctrlX, p0.getY());
        Point2D p2 = new Point2D(ctrlX, p3.getY());
        curve.getElements().add(new MoveTo(p0.getX(), p0.getY()));
        curve.getElements().add(new CubicCurveTo(
                p1.getX(), p1.getY(),
                p2.getX(), p2.getY(),
                p3.getX(), p3.getY()
        ));

//        System.out.println("updateShape");
    }

    public void bindToBudget(double remainingWire) {
        boolean valid = model.isWithinBudget(remainingWire);
        curve.setStroke(valid ? Color.GREEN : Color.RED);
    }
}
