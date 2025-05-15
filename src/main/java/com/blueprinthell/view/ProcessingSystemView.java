package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ProcessingSystemView extends AbstractDeviceView {
    private Rectangle body;
    private Rectangle innerBody;
    private Circle lamp;

    public ProcessingSystemView(NetworkDevice model) {
        super(model);
    }

    @Override
    protected void initializeGraphics() {
        body = new Rectangle(0, 0, 200, 200);
        innerBody = new Rectangle(5, 50, 190, 140);
        lamp = new Circle(180, 25, 15);

        lamp.setFill(Color.rgb(40, 40, 40));
        lamp.setStroke(Color.rgb(40, 40, 40).darker().darker().darker());

        body.setArcWidth(10);
        body.setArcHeight(10);


        innerBody.setArcWidth(10);
        innerBody.setArcHeight(10);


        body.setFill(Color.rgb(80, 80, 80));
        body.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
        body.setStrokeWidth(2);

        innerBody.setFill(Color.rgb(40, 40 ,40));
        innerBody.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
        getChildren().addAll(body, innerBody, lamp);

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
