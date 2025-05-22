// PacketController.java (بررسی و عبور پکت از سیستم‌ها)
package com.blueprinthell.controller;

import com.blueprinthell.model.*;
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
        System.out.println("🔵 پکت اضافه شد به کنترلر: " + packet);
        Platform.runLater(() -> displayLayer.getChildren().add(view.getNode()));
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
                    Port dest = p.getDestinationPort();

                    if (dest != null && dest.getOwner() instanceof ProcessingSystem ps) {
                        System.out.println("📍 پکت به مقصد رسید: " + p);

                        List<Wire> allWires = getAllWires();
                        boolean forwarded = false;

                        for (Port out : ps.getOutPorts()) {
                            if (!out.getShape().toString().equals(p.getShape().toString())) continue;

                            Wire next = allWires.stream()
                                    .filter(w -> w.getOutputPort().equals(out))
                                    .findFirst().orElse(null);

                            if (next != null) {
                                System.out.println("➡️ سیم خروجی مناسب پیدا شد، پکت ادامه می‌دهد");

                                Packet forwardedPacket = new Packet(p.getShape(), p.getSize(), next.flatten(40), 100);
                                forwardedPacket.setDestinationPort(next.getInputPort());

                                addPacket(forwardedPacket, new PacketView(forwardedPacket));

                                Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                                toRemovePackets.add(p);
                                toRemoveViews.add(v);
                                forwarded = true;
                                break;
                            }
                        }

                        if (!forwarded) {
                            System.out.println("❌ هیچ پورت خروجی آزاد پیدا نشد");
                            ps.addToBuffer(p);
                            System.out.println("🟡 پکت وارد بافر پردازش شد: " + ps.getId());
                            Platform.runLater(() -> {
                                for (Node node : displayLayer.getChildren()) {
                                    if (node instanceof ProcessingSystemView view && view.getModel().equals(ps)) {
                                        view.renderBufferedPackets();
                                    }
                                }
                            });
                        }
                    }

                    // فقط پاک نکن اگر پکت forward نشده بود
                    if (!packets.contains(p)) {
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
        }, 0, 16, TimeUnit.MILLISECONDS);
    }


    public void stop() {
        executor.shutdown();
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

    private Wire findWireByInputPort(Port port) {
        Pane layer = (wiringLayer != null) ? wiringLayer : displayLayer;
        for (Node node : layer.getChildren()) {
            if (node.getUserData() instanceof Wire wire) {
                if (wire.getInputPort().equals(port)) return wire;
            }
        }
        return null;
    }

    private Wire findWireConnectedTo(Port port) {
        Pane layer = (wiringLayer != null) ? wiringLayer : displayLayer;
        for (Node node : layer.getChildren()) {
            if (node.getUserData() instanceof Wire wire) {
                if (wire.getOutputPort().equals(port)) return wire;
            }
        }
        return null;
    }
}
