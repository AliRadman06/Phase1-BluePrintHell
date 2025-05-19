package com.blueprinthell.controller;

import com.blueprinthell.model.Packet;
import com.blueprinthell.model.ProcessingSystem;
import com.blueprinthell.view.PacketView;

import com.blueprinthell.view.ProcessingSystemView;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PacketController {
    private final Pane displayLayer;
    private final List<Packet> packets = new ArrayList<>();
    private final List<PacketView> views = new ArrayList<>();
    private final ScheduledExecutorService executor;
    private Packet packet;


    public PacketController(Pane displayLayer) {
        this.displayLayer = displayLayer;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void addPacket(Packet p, PacketView v) {
        packets.add(p);
        views.add(v);
        Platform.runLater(() -> displayLayer.getChildren().add(v.getNode()));
    }

    public void start() {
        executor.scheduleAtFixedRate(() -> {
            double dt = 1.0 / 60;

            List<Packet> toRemovePackets = new ArrayList<>();
            List<PacketView> toRemoveViews = new ArrayList<>();

            for (int i = 0; i < packets.size(); i++) {
                Packet p = packets.get(i);
                PacketView v = views.get(i);

                p.advance(p.getSpeed() * dt);

                if (p.isFinished()) {
                    // حذف از صحنه و صف
                    toRemovePackets.add(p);
                    toRemoveViews.add(v);

                    var dest = p.getDestinationPort();
                    if (dest != null && dest.getOwner() instanceof ProcessingSystem ps) {
                        ps.addToBuffer(p);

                        Platform.runLater(() -> {
                            displayLayer.getChildren().forEach(node -> {
                                if (node instanceof ProcessingSystemView view && view.getModel().equals(ps)) {
                                    view.renderBufferedPackets();
                                }
                            });
                        });
                    }

                    Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                }
            }

            packets.removeAll(toRemovePackets);
            views.removeAll(toRemoveViews);

            Platform.runLater(() -> {
                for (PacketView v : views) {
                    v.updatePosition();
                }
            });
        }, 0, 16, TimeUnit.MILLISECONDS);
    }


    public void stop() {
        executor.shutdown();
    }







}
