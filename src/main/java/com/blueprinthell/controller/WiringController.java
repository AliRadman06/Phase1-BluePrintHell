package com.blueprinthell.controller;

import com.blueprinthell.model.Packet;
import com.blueprinthell.model.Port;
import com.blueprinthell.model.Wire;
import com.blueprinthell.view.AbstractDeviceView;
import com.blueprinthell.view.GameViewL1;
import com.blueprinthell.view.PacketView;
import com.blueprinthell.view.WireView;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import com.blueprinthell.controller.PacketController;

import java.util.ArrayList;
import java.util.List;

public class WiringController {
    private final Pane wiringLayer;
    private final List<Wire> wires = new ArrayList<>();
    private double remainingWires;
    private Wire currentWire;
    private WireView currentWireView;
    private final PacketController packetController;


    public WiringController(GameViewL1 view) {
        this.wiringLayer = view.getWiringLayer();
        this.packetController = view.getPacketController(); // ← همین
        this.remainingWires = view.TOTAL_WIRE;
        initEventHandlers(view);
    }

    private void initEventHandlers(GameViewL1 view) {
        for( Node child : view.getGamePane().getChildren() ) {
            if( child instanceof AbstractDeviceView deviceView) {
                for( Node portShape : deviceView.getAllPortNodes() ){
                    portShape.setOnMousePressed(this::onPortMousePressed);
                }
            }
        }
        view.getScene().setOnMouseDragged(this::onMouseDragged);
        view.getScene().setOnMouseReleased(this::onMouseReleased);
    }

    private void onPortMousePressed(MouseEvent event) {
        Port p = (Port)((Node)event.getSource()).getUserData();

        if (p.getDirection() == Port.Direction.OUT && canStartFrom(p)) {
            System.out.println("Start wiring on port: " + p.getOwner().getId());
            currentWire = new Wire(p);
            currentWireView = new WireView(currentWire);
            wiringLayer.getChildren().add(currentWireView.getCurve());
        }
        event.consume();
    }


    private void onMouseDragged(MouseEvent event) {
        if( currentWireView != null ) {
            Point2D mousePt = new Point2D(event.getX(), event.getY());
            currentWire.setTempEnd(mousePt);
            System.out.println("Mouse Dragged at: " + mousePt);
            currentWireView.updateShape();
            System.out.println("Curve Elements after update: " + currentWireView.getCurve().getElements().size());

        }
    }

    private void onMouseReleased(MouseEvent event) {
        if (currentWireView == null) return;

        Node target = event.getPickResult().getIntersectedNode();
        if (target != null && target.getUserData() instanceof Port p) {
            if (p.getDirection() == Port.Direction.IN) {
                Port outPort = currentWire.getOutputPort();
                if (outPort.getOwner().equals(p.getOwner())) {
                    wiringLayer.getChildren().remove(currentWireView.getCurve());
                }
                else if (outPort.getShape() != p.getShape()) {
                    wiringLayer.getChildren().remove(currentWireView.getCurve());
                }
                else if (canEndAt(p)) {
                    currentWire.setInputPort(p);
                    currentWireView.updateShape();
                    if (addConnection(currentWire)) {
                        currentWireView.bindToBudget(remainingWires);
                    } else {
                        wiringLayer.getChildren().remove(currentWireView.getCurve());
                    }
                } else {
                    wiringLayer.getChildren().remove(currentWireView.getCurve());
                }
            } else {
                wiringLayer.getChildren().remove(currentWireView.getCurve());
            }
        } else {
            wiringLayer.getChildren().remove(currentWireView.getCurve());
        }

        currentWireView = null;
        currentWire     = null;
    }

    private boolean addConnection(Wire w) {
        double len = w.calculateLength();
        if (len <= remainingWires) {
            wires.add(w);
            remainingWires -= len;
            return true;
        }
        return false;
    }

    private boolean canStartFrom(Port out) {
        return wires.stream()
                .map(Wire::getOutputPort)
                .noneMatch(p -> p.equals(out));
    }

    private boolean canEndAt(Port in) {
        return wires.stream()
                .map(Wire::getInputPort)
                .noneMatch(p -> p != null && p.equals(in));
    }

}
