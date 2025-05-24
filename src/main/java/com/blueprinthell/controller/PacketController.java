package com.blueprinthell.controller;

import com.blueprinthell.logic.GameStats;
import com.blueprinthell.logic.PacketCollisionDetector;
import com.blueprinthell.model.*;
import com.blueprinthell.util.StageProvider;
import com.blueprinthell.view.GameOverView;
import com.blueprinthell.view.PacketView;
import com.blueprinthell.view.ProcessingSystemView;
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
            List<PacketView> toRemoveViews = new ArrayList<>();

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

                        if (p.getShape() == Packet.ShapeType.SQUARE) {
                            GameStats.addCoins(1);
                        } else if (p.getShape() == Packet.ShapeType.TRIANGLE) {
                            GameStats.addCoins(2);
                        }

                        List<Wire> allWires = getAllWires();
                        List<Port> outPorts = ps.getOutPorts();

                        Wire chosenWire = null;
                        double speed = 100;

                        for (Port out : outPorts) {
                            Optional<Wire> maybe = allWires.stream()
                                    .filter(w -> w.getOutputPort().equals(out))
                                    .filter(this::isWireFree)
                                    .findFirst();

                            if (maybe.isPresent()) {
                                chosenWire = maybe.get();
                                speed = out.getShape().toString().equals(p.getShape().toString()) ? 100 : 50;
                                break;
                            }
                        }

                        if (chosenWire != null) {
                            p.resetProgress();
                            p.setPath(chosenWire.flatten(40));
                            p.setSpeed(speed);
                            p.setDestinationPort(chosenWire.getInputPort());

                            removePacket(p);
                            addPacket(p, new PacketView(p));
                            Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                        } else {
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

                    } else {
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
                Platform.runLater(() -> GameOverView.showOverlay(StageProvider.getStage().getScene()));
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

                    // فقط سیم‌های خروجی از این سیستم
                    List<Wire> freeWires = allWires.stream()
                            .filter(w -> w.getOutputPort().getOwner().equals(ps))
                            .filter(this::isWireFree)
                            .toList();

                    // اولویت با سیم هم‌نوع
                    Optional<Wire> sameType = freeWires.stream()
                            .filter(w -> w.getOutputPort().getShape().toString().equals(p.getShape().toString()))
                            .findFirst();

                    Wire chosen = sameType.orElseGet(() ->
                            freeWires.isEmpty() ? null : freeWires.get(0)
                    );

                    if (chosen == null) break;

                    double speed = chosen.getOutputPort().getShape().toString().equals(p.getShape().toString()) ? 100 : 50;

                    p.resetProgress();
                    p.setPath(chosen.flatten(40));
                    p.setSpeed(speed);
                    p.setDestinationPort(chosen.getInputPort());
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

    private boolean isWireFree(Wire wire) {
        return packets.stream()
                .noneMatch(p -> p.getDestinationPort() != null &&
                        p.getDestinationPort().equals(wire.getInputPort()) &&
                        !p.isFinished());
    }



}
