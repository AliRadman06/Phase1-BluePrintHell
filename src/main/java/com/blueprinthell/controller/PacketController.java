package com.blueprinthell.controller;

import com.blueprinthell.logic.GameStats;
import com.blueprinthell.logic.PacketCollisionDetector;
import com.blueprinthell.model.*;
import com.blueprinthell.util.StageProvider;
import com.blueprinthell.view.GameOverView;
import com.blueprinthell.view.HUDView;
import com.blueprinthell.view.PacketView;
import com.blueprinthell.view.ProcessingSystemView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PacketController {
    private final Pane displayLayer;
    private Pane wiringLayer;
    private final List<Packet> packets = new ArrayList<>();
    private final List<PacketView> views = new ArrayList<>();
    private final ScheduledExecutorService executor;
    private final PacketCollisionDetector collisionDetector = new PacketCollisionDetector();
    private HUDView hudView;




    public PacketController(Pane displayLayer) {
        this.displayLayer = displayLayer;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void setWiringLayer(Pane wiringLayer) {
        this.wiringLayer = wiringLayer;
    }

    public void addPacket(Packet packet, PacketView view) {
        packets.add(packet);
        views.add(view);
        Platform.runLater(() -> displayLayer.getChildren().add(view.getNode()));
    }

    public void start() {
        executor.scheduleAtFixedRate(() -> {
            double dt = 1.0 / 60;

            checkAndForwardBufferedPackets();

            List<Packet> toRemovePackets = new ArrayList<>();
            List<PacketView> toRemoveViews   = new ArrayList<>();

            for (int i = 0; i < packets.size(); i++) {
                Packet p = packets.get(i);
                PacketView v = views.get(i);

                p.advance(p.getSpeed() * dt);

                if (!p.isAlive()) {
                    toRemovePackets.add(p);
                    toRemoveViews.add(v);
                    Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                    continue;
                }

                if (p.isFinished()) {
                    Port dest = p.getDestinationPort();

                    if (dest != null && dest.getOwner() instanceof ProcessingSystem ps) {

                        List<Wire> allWires = getAllWires();
                        boolean forwarded = false;

                        for (Port out : ps.getOutPorts()) {
                            if (!out.getShape().toString().equals(p.getShape().toString()))
                                continue;

                            Wire next = allWires.stream()
                                    .filter(w -> w.getOutputPort().equals(out))
                                    .findFirst().orElse(null);

                            if (next != null) {
                                p.resetProgress();
                                p.setPath(next.flatten(40));
                                p.setSpeed(50);
                                p.setDestinationPort(next.getInputPort());
                                removePacket(p);
                                addPacket(p, new PacketView(p));

                                Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                                forwarded = true;
                                break;
                            }
                        }

                        if (!forwarded) {
                            ps.addToBuffer(p);

                            toRemovePackets.add(p);
                            toRemoveViews.add(v);
                            Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));

                            Platform.runLater(() -> {
                                for (Node node : displayLayer.getChildren()) {
                                    if (node instanceof ProcessingSystemView view
                                            && view.getModel().equals(ps)) {
                                        view.renderBufferedPackets();
                                    }
                                }
                            });
                        }
                    }
                    else {
                        toRemovePackets.add(p);
                        toRemoveViews.add(v);
                        Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                    }
                }
            }

            packets.removeAll(toRemovePackets);
            views.removeAll(toRemoveViews);

            Platform.runLater(() -> {
                for (PacketView v : views) {
                    v.updatePosition();
                }
            });
            collisionDetector.update(packets, views);
            if (GameStats.isGameOver()) {
                stop();
                Platform.runLater(() -> {
                    GameOverView.showOverlay(StageProvider.getStage().getScene());
                });
            }

        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdown();
    }

    private void checkAndForwardBufferedPackets() {
        List<Wire> allWires = getAllWires();

        for (Wire wire : allWires) {
            if (wire.getOutputPort().getOwner() instanceof ProcessingSystem ps) {
                Iterator<Packet> it = ps.getPacketBuffer().iterator();

                while (it.hasNext()) {
                    Packet p = it.next();
                    if (!wire.getOutputPort().getShape().toString()
                            .equals(p.getShape().toString())) {
                        continue;
                    }

                    p.resetProgress();
                    p.setPath(wire.flatten(40));
                    p.setSpeed(100);
                    p.setDestinationPort(wire.getInputPort());
                    addPacket(p, new PacketView(p));

                    it.remove();

                    Platform.runLater(() -> {
                        for (Node node : displayLayer.getChildren()) {
                            if (node instanceof ProcessingSystemView view
                                    && view.getModel().equals(ps)) {
                                view.renderBufferedPackets();
                            }
                        }
                    });
                }
            }
        }
    }

    private List<Wire> getAllWires() {
        List<Wire> wires = new ArrayList<>();
        Pane layer = (wiringLayer != null) ? wiringLayer : displayLayer;
        for (Node node : layer.getChildren()) {
            if (node.getUserData() instanceof Wire wire) {
                wires.add(wire);
            }
        }
        return wires;
    }

    public void removePacket(Packet p) {
        int index = packets.indexOf(p);
        if (index != -1) {
            packets.remove(index);
            PacketView view = views.remove(index);
            Platform.runLater(() -> displayLayer.getChildren().remove(view.getNode()));
        }
    }

    public void setHUDView(HUDView hudView) {
        this.hudView = hudView;
    }


}
