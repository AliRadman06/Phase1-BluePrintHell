package com.blueprinthell.model;

import java.util.ArrayList;

public abstract class NetworkDevice {
    private final String id;
    private final double x, y;
//    protected List<Packet> packets = new ArrayList<>();  گتر هم بعدا براش بزارم

    public NetworkDevice(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public abstract SystemType getSystemType();
    public abstract void initialize();  // ساختن پکت اولیه
    public abstract void update(double deltaTime);  // اپدیت کردن هر فریم بر اساس دلتا تایم



}
