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
            r.setFill(Color.YELLOW);
            node = r;
        } else {
            Polygon tri = new Polygon(
                0, 0,
                    20, 0,
                    10, 20
            );
            node = tri;
        }
        updatePosition();
    }

    public void updatePosition() {
        Point2D pos = model.getCurrentPosition();
        node.setTranslateX(pos.getX());
        node.setTranslateY(pos.getY());
    }

    public Node getNode() {
        return node;
    }
}
