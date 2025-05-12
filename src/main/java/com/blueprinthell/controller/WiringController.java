package com.blueprinthell.controller;

import com.blueprinthell.model.Wire;
import com.blueprinthell.view.GameViewL1;
import com.blueprinthell.view.WireView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class WiringController {
    private final Pane wiringLayer;
    private final List<Wire> wires = new ArrayList<>();
    private double remainingWires;
    private Wire currentWire;
    private WireView currentWireView;

    public WiringController(GameViewL1 view) {
        this.wiringLayer = view.getWiringLayer();
        this.remainingWires = view.TOTAL_WIRE;
        initEventHandlers(view);
    }

    private void initEventHandlers(GameViewL1 view) {

    }

}
