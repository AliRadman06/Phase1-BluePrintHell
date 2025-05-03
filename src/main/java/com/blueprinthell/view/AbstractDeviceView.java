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
        double size   = 20; // اندازهٔ مربعِ پورت
        double offset = 5;  // فاصله از لبهٔ بدنه و بین پورت‌ها

        // 1) پورت‌های ورودی (IN) روی ضلع چپ
        int i = 0;
        for (Port p : model.getInPorts()) {
            double x = -size - offset;
            double y = offset + i * (size + offset);
            drawPortShape(p, x, y, size);
            i++;
        }

        // 2) پورت‌های خروجی (OUT) روی ضلع راست
        i = 0;
        double bodyW = getBodyWidth();
        for (Port p : model.getOutPorts()) {
            double x = bodyW + offset;
            double y = offset + i * (size + offset);
            drawPortShape(p, x, y, size);
            i++;
        }
    }

    /**
     * رسمِ یک مربع و یک مثلث برای هر پورت
     */
    private void drawPortShape(Port p, double x, double y, double size) {
        // مربع
        Rectangle square = new Rectangle(x, y, size, size);
        square.setFill(
                p.getDirection() == Port.Direction.IN
                        ? Color.DODGERBLUE
                        : Color.SALMON
        );
        getChildren().add(square);

        // مثلث متساوی‌الاضلاع
        Polygon tri = new Polygon(
                0.0, 0.0,
                size, 0.0,
                size/2, size * Math.sqrt(3)/2
        );
        tri.setFill(square.getFill());

        if (p.getDirection() == Port.Direction.OUT) {
            // چرخش ۱۸۰ درجه و جابجایی زیر مربع
            tri.setRotate(180);
            tri.setLayoutX(x + size);
            tri.setLayoutY(y + size);
        } else {
            // IN: بالای مربع
            tri.setLayoutX(x);
            tri.setLayoutY(y - size * Math.sqrt(3)/2);
        }
        getChildren().add(tri);
    }
}
