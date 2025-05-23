package com.blueprinthell.model;

import java.util.ArrayList;
import java.util.List;


public abstract class
NetworkDevice {
    private final String id;
    private  double x, y;
    protected final List<Port> inPorts  = new ArrayList<>();
    protected final List<Port> outPorts = new ArrayList<>();

    public NetworkDevice(String id, double x, double y) {
        this.id = id;
        this.x  = x;
        this.y  = y;
    }

    public String getId()            { return id; }
    public void setX(double  x) { this.x = x; }
    public void setY(double  y) { this.y = y; }
    public double getX()             { return x; }
    public double getY()             { return y; }
    public List<Port> getInPorts()   { return inPorts; }
    public List<Port> getOutPorts()  { return outPorts; }

    public abstract SystemType getType();

    public abstract void initialize();

    public abstract void update(double deltaTime);
}
