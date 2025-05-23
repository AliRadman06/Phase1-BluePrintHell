package com.blueprinthell.view;

import com.blueprinthell.model.Packet;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class PacketView {
    private Packet model;
    private Node node;

    public PacketView(Packet model) {
        this.model = model;

        if (model.getShape() == Packet.ShapeType.SQUARE) {
            Rectangle r = new Rectangle(20, 20);
            r.setX(-10); r.setY(-10);
            r.setFill(Color.YELLOW);
            r.setStroke(Color.rgb(140, 112, 5));
            r.setStrokeWidth(2);
            r.setId("packet-" + model.getId());
            node = r;
        } else {
            Polygon tri = new Polygon(
                    -10, 10,
                    10, 10,
                    0, -10
            );
            tri.setFill(Color.ORANGE);
            tri.setStroke(Color.rgb(152, 82, 5));
            tri.setStrokeWidth(2);
            tri.setId("packet-" + model.getId());
            node = tri;
        }

        updatePosition();
    }

    public void updatePosition() {
        Point2D pos = model.getCurrentPosition();
        node.setTranslateX(pos.getX());
        node.setTranslateY(pos.getY() + model.getDeviation());
    }

    public Node getNode() {
        return node;
    }

    public double getX() {
        return node.getLayoutX() + node.getTranslateX();
    }

    public double getY() {
        return node.getLayoutY() + node.getTranslateY();
    }

}
