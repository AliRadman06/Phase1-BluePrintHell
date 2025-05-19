package com.blueprinthell.controller;

import com.blueprinthell.model.Packet;
import com.blueprinthell.view.PacketView;

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
            // حرکت مدل
            for (Packet p : packets) {
                p.advance(p.getSpeed() * dt);
            }
            // به‌روزرسانی ویو در ترد FX
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
