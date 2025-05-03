package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EndSystemView extends AbstractDeviceView {
    private Rectangle body;

    public EndSystemView(NetworkDevice model) {
        super(model);
    }

    @Override
    protected void initializeGraphics() {
        body = new Rectangle(0, 0, 200, 200);
        body.setFill(Color.PINK);
        body.setStroke(Color.RED);
        getChildren().add(body);

        body.setOnMouseMoved(e ->
                System.out.println("Hovered SYSTEM: " + model.getId())
        );

        drawPorts();
    }

    @Override
    protected double getBodyWidth() {
        return body.getWidth();
    }

    @Override
    protected double getBodyHeight() {
        return body.getHeight();
    }
}
