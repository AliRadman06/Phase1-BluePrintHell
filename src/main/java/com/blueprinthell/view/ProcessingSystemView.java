package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ProcessingSystemView extends AbstractDeviceView {
    private Rectangle body;

    public ProcessingSystemView(NetworkDevice model) {
        super(model);
    }

    @Override
    protected void initializeGraphics() {
        body = new Rectangle(0, 0, 200, 200);
        body.setArcWidth(5);
        body.setArcHeight(5);
        body.setFill(Color.LIGHTBLUE);
        body.setStroke(Color.DARKBLUE);

        Text label = new Text("PROC");
        label.setFont(Font.font(24));
        label.setFill(Color.DARKBLUE);
        label.setX((body.getWidth() - label.getLayoutBounds().getWidth()) / 2);
        label.setY((body.getHeight() + label.getLayoutBounds().getHeight()) / 2);

        body.setOnMouseMoved(e ->
                System.out.println("Hovered SYSTEM: " + model.getId())
        );

        getChildren().addAll(body, label);
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
