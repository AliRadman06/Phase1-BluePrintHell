package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StartSystemView extends AbstractDeviceView {
    private Rectangle body;

    public StartSystemView(NetworkDevice model) {
        super(model);
    }

    @Override
    protected void initializeGraphics() {
        body = new Rectangle(0, 0, 200, 200);
        body.setArcWidth(10);
        body.setArcHeight(10);
        body.setFill(Color.LIGHTGREEN);
        body.setStroke(Color.DARKGREEN);
        getChildren().add(body);

        body.setOnMouseMoved(e ->
                System.out.println("Hovered SYSTEM: " + model.getId())
        );

        drawPorts(); // رسمِ پورت‌ها پس از بدنه
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
