package com.blueprinthell.controller;

import com.blueprinthell.logic.GameStats;
import com.blueprinthell.logic.PacketCollisionDetector;
import com.blueprinthell.model.*;
import com.blueprinthell.util.SoundManager;
import com.blueprinthell.util.StageProvider;
import com.blueprinthell.view.*;
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
    private boolean isPaused = false;
    private List<Packet> allPackets = new ArrayList<>();
    private int oAtarCount = 0;
    private int oAiryamanCount = 0;
    private int oAnahitaCount = 0;
    private List<Packet> alivePackets = new ArrayList<>();
    private List<Packet> lostPackets = new ArrayList<>();
    private EndSystem endSystem;
    private boolean isWinChecked = false;
    private int totalToBeSpawned = 0;
    private int finishedPackets = 0;




    public PacketController(Pane displayLayer) {
        this.displayLayer = displayLayer;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void setWiringLayer(Pane wiringLayer) {
        this.wiringLayer = wiringLayer;
    }

    public void addPacket(Packet packet, PacketView view) {
        collisionDetector.setPacketController(this);
        packets.add(packet);
        if (!allPackets.contains(packet)) {
            allPackets.add(packet);

        }
        views.add(view);
        Platform.runLater(() -> displayLayer.getChildren().add(view.getNode()));
    }

    public void start() {

        executor.scheduleAtFixedRate(() -> {
            double dt = 1.0 / 60;
            if (isPaused) return;

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
                    if (dest != null) {
                        Object owner = dest.getOwner();

                        if (owner instanceof ProcessingSystem ps) {
                            if (p.getShape() == Packet.ShapeType.SQUARE) GameStats.addCoins(1);
                            else if (p.getShape() == Packet.ShapeType.TRIANGLE) GameStats.addCoins(2);

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
                                        if (node instanceof ProcessingSystemView view &&
                                                view.getModel().equals(ps)) {
                                            view.renderBufferedPackets();
                                        }
                                    }
                                });
                            }

                        } else if (owner instanceof EndSystem es) {
                            if (p.getShape() == Packet.ShapeType.SQUARE) GameStats.addCoins(1);
                            else if (p.getShape() == Packet.ShapeType.TRIANGLE) GameStats.addCoins(2);

                            es.addToBuffer(p);
                            finishedPackets++;
                            toRemovePackets.add(p);
                            toRemoveViews.add(v);
                            Platform.runLater(() -> {
                                displayLayer.getChildren().remove(v.getNode());
                                for (Node node : displayLayer.getChildren()) {
                                    if (node instanceof EndSystemView view &&
                                            view.getModel().equals(es)) {
                                        view.renderBufferedPackets();
                                    }
                                }
                            });
                        } else {
                            toRemovePackets.add(p);
                            toRemoveViews.add(v);
                            Platform.runLater(() -> displayLayer.getChildren().remove(v.getNode()));
                        }
                    }
                }
            }

            packets.removeAll(toRemovePackets);
            views.removeAll(toRemoveViews);

            Platform.runLater(() -> views.forEach(PacketView::updatePosition));
            collisionDetector.update(packets, views);

            if (GameStats.isGameOver()) {
                stop();
                Platform.runLater(() -> {
                    GameOverView.showOverlay(StageProvider.getStage().getScene());
                    SoundManager.getInstance().playEffect("game-over", "/audio/Game_Over.mp3");
                });

            }

            if (!isWinChecked && totalToBeSpawned > 0 && finishedPackets == totalToBeSpawned) {
                isWinChecked = true;
                System.out.println("win");
                Platform.runLater(() -> WinView.showOverlay(StageProvider.getStage().getScene()));
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

                    List<Wire> freeWires = allWires.stream()
                            .filter(w -> w.getOutputPort().getOwner().equals(ps))
                            .filter(this::isWireFree)
                            .toList();

                    Optional<Wire> sameType = freeWires.stream()
                            .filter(w -> w.getOutputPort().getShape().toString().equals(p.getShape().toString()))
                            .findFirst();

                    Wire chosen = sameType.orElseGet(() -> freeWires.isEmpty() ? null : freeWires.get(0));
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
                            if (node instanceof ProcessingSystemView view &&
                                    view.getModel().equals(ps)) {
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
        return packets.stream().noneMatch(p ->
                p.getDestinationPort() != null &&
                        p.getDestinationPort().equals(wire.getInputPort()) &&
                        !p.isFinished()
        );
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void activateOAtarSkill() {
        collisionDetector.disableImpactForSeconds(10);
    }

    public void activateOAiryamanSkill() {
        collisionDetector.disableCollisionForSeconds(5);
    }

    public void activeOAnahitaSkill() {
        for (Packet packet : allPackets) {
            packet.setNoise(0);
        }
    }

    public void addOAtar()     { oAtarCount++; }
    public void addOAiryaman() { oAiryamanCount++; }
    public void addOAnahita()  { oAnahitaCount++; }

    public int getOAtarCount()     { return oAtarCount; }
    public int getOAiryamanCount() { return oAiryamanCount; }
    public int getOAnahitaCount()  { return oAnahitaCount; }

    public void setOAtarCount(int oAtarCount) {
        this.oAtarCount = oAtarCount;
    }

    public void setOAiryamanCount(int oAiryamanCount) {
        this.oAiryamanCount = oAiryamanCount;
    }

    public void setOAnahitaCount(int oAnahitaCount) {
        this.oAnahitaCount = oAnahitaCount;
    }



    public boolean isOAtarBought()   { return oAtarCount > 0; }
    public boolean isOAiryamanBought() { return oAiryamanCount > 0; }
    public boolean isOAnahitaBought() { return oAnahitaCount > 0; }

    public List<String> boughtItems() {
        List<String> boughtItems = new ArrayList<>();
        if (isOAiryamanBought()) boughtItems.add("O Airyaman");
        if (isOAnahitaBought())  boughtItems.add("O Anahita");
        if (isOAtarBought())     boughtItems.add("O Atar");
        return boughtItems;
    }

    public void addPacketToLossList(Packet p) {
        lostPackets.add(p);
    }

    public void setEndSystem(EndSystem endSystem) {
        this.endSystem = endSystem;
    }

    public void incrementFinishedPackets() {
        finishedPackets++;
    }

    public void setTotalToBeSpawned(int t) {
        this.totalToBeSpawned = t;
    }



}
