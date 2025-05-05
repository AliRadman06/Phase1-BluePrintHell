package com.blueprinthell.view;

import com.blueprinthell.model.Port;
import com.blueprinthell.model.NetworkDevice;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public abstract class AbstractDeviceView extends Group {
    protected final NetworkDevice model;

    public AbstractDeviceView(NetworkDevice model) {
        this.model = model;
        initializeGraphics();
        updatePosition();
    }

    /** رسم اجزای اصلی (بدنه، متن، …) در زیرکلاس */
    protected abstract void initializeGraphics();

    /** به‌روز کردن موقعیت View با مختصاتِ مدل */
    public void updatePosition() {
        setLayoutX(model.getX());
        setLayoutY(model.getY());
    }

    /**
     * هر View باید ارتفاع و عرض بدنه‌اش را برگرداند
     * (برای محاسبهٔ محل پورت‌ها)
     */
    protected abstract double getBodyWidth();
    protected abstract double getBodyHeight();

    /**
     * رسمِ پورت‌های IN در ضلع چپ و OUT در ضلع راست
     */
    protected void drawPorts() {
        double size   = 20;  // اندازه مربعِ پورت
        double offset = 1;   // فاصله بین پورت‌ها و از لبهٔ بدنه

        // --- پورت‌های IN روی ضلع چپ، وسط چین عمودی ---
        int inCount = model.getInPorts().size();
        // ارتفاع کلی ستون پورت‌ها: تعداد * size + (تعداد-1)*offset
        double totalInH = inCount*size + (inCount-1)*offset;
        // نقطه شروع: (بدنه-ارتفاع ستون)/2
        double startYIn = (getBodyHeight() - totalInH) / 2;

        for (int i = 0; i < inCount; i++) {
            Port p = model.getInPorts().get(i);
            double x = -size - offset;
            double y = startYIn + i*(size + offset);
            drawPortShape(p, x, y, size);
        }

        // --- پورت‌های OUT روی ضلع راست، وسط چین عمودی ---
        int outCount = model.getOutPorts().size();
        double totalOutH = outCount*size + (outCount-1)*offset;
        double startYOut = (getBodyHeight() - totalOutH) / 2;

        double bodyW = getBodyWidth();
        for (int i = 0; i < outCount; i++) {
            Port p = model.getOutPorts().get(i);
            double x = bodyW + offset;
            double y = startYOut + i*(size + offset);
            drawPortShape(p, x, y, size);
        }
    }


    /**
     * رسمِ یک مربع و یک مثلث برای هر پورت
     */
    private void drawPortShape(Port p, double x, double y, double size) {
        // مربع
        Rectangle square = new Rectangle(x, y, size, size);

        square.setArcHeight(2);
        square.setArcWidth(2);


        square.setFill(p.getDirection() == Port.Direction.IN ? Color.DODGERBLUE : Color.SALMON);

        square.setOnMouseMoved(evt ->
                System.out.println("Hovered PORT: "
                        + p.getDirection()
                        + " on System " + model.getId())
        );

        getChildren().add(square);


    }
}
