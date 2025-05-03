package com.blueprinthell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * پایه‌ی مشترک همهٔ سیستم‌ها
 */
public abstract class NetworkDevice {
    private final String id;
    private final double x, y;
    protected final List<Port> inPorts  = new ArrayList<>();
    protected final List<Port> outPorts = new ArrayList<>();

    public NetworkDevice(String id, double x, double y) {
        this.id = id;
        this.x  = x;
        this.y  = y;
    }

    public String getId()            { return id; }
    public double getX()             { return x; }
    public double getY()             { return y; }
    public List<Port> getInPorts()   { return inPorts; }
    public List<Port> getOutPorts()  { return outPorts; }

    /** مشخص می‌کند این دستگاه از چه نوعی است */
    public abstract SystemType getType();

    /** منطقِ اولیه (مثلاً تولید Packet اولیه) */
    public abstract void initialize();

    /** آپدیت مدل در هر فریم با deltaTime به ثانیه */
    public abstract void update(double deltaTime);
}
