package com.blueprinthell.logic;

import com.blueprinthell.model.Packet;
import com.blueprinthell.view.PacketView;
import javafx.scene.shape.Shape;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PacketCollisionDetector {
    private final Set<PacketCollisionKey> active = new HashSet<>();
    private boolean impactDisabled = false;
    private long impactDisableUntil = 0;
    private boolean collisionDisabled = false;
    private long collisionDisableUntil = 0;



    public void update(List<Packet> packets, List<PacketView> views) {

        if (impactDisabled && System.currentTimeMillis() > impactDisableUntil) {
            impactDisabled = false;
        }

        if (collisionDisabled && System.currentTimeMillis() > collisionDisableUntil) {
            collisionDisabled = false;
        }

        if (collisionDisabled) {
            return;
        }


        Set<PacketCollisionKey> current = new HashSet<>();

        int count = packets.size();
        for (int i = 0; i < count; i++) {
            Packet a = packets.get(i);
            PacketView va = views.get(i);
            Shape sa = (Shape) va.getNode();

            for (int j = i + 1; j < count; j++) {
                Packet b = packets.get(j);
                if (a == b) continue;

                PacketView vb = views.get(j);
                Shape sb = (Shape) vb.getNode();

                if (!areClose(va, vb, a.getRadiusOfDetection())) continue;
                if (!areIntersecting(sa, sb)) continue;

                PacketCollisionKey key = new PacketCollisionKey(a, b);
                current.add(key);

                if (!active.contains(key)) {
                    applyImpact(a, b, va, vb, packets, views);
                }
            }
        }
        active.retainAll(current);
        active.addAll(current);
    }

    public void reset() {
        active.clear();
    }

    private boolean areClose(PacketView a, PacketView b, double threshold) {
        double ax = a.getNode().getLayoutX() + a.getNode().getTranslateX();
        double ay = a.getNode().getLayoutY() + a.getNode().getTranslateY();
        double bx = b.getNode().getLayoutX() + b.getNode().getTranslateX();
        double by = b.getNode().getLayoutY() + b.getNode().getTranslateY();
        return Math.hypot(ax - bx, ay - by) < threshold;
    }

    private boolean areIntersecting(Shape a, Shape b) {
        return !Shape.intersect(a, b).getBoundsInLocal().isEmpty();
    }

    private void applyImpact(Packet a, Packet b, PacketView va, PacketView vb,  List<Packet> allPackets, List<PacketView> allViews) {
        final double DAMAGE = 1.0;
        final double DEV = 5.0;

        a.increaseNoise(DAMAGE);
        System.out.println(a.getId() + "  :  " + a.getNoise());
        b.increaseNoise(DAMAGE);
        System.out.println(b.getId() + "  :  " + b.getNoise());

        double ay = va.getNode().getLayoutY() + va.getNode().getTranslateY();
        double by = vb.getNode().getLayoutY() + vb.getNode().getTranslateY();

        if (!impactDisabled) {
            if (ay < by) {
                a.addDeviation(+DEV);
                b.addDeviation(-DEV);
            } else {
                a.addDeviation(-DEV);
                b.addDeviation(+DEV);
            }
        }

        if (a.getNoise() >= a.getSize()) {
            a.kill();
        }
        if (b.getNoise() >= b.getSize()) {
            b.kill();
        }

        double impactX = (va.getX() + vb.getX()) / 2;
        double impactY = (va.getY() + vb.getY()) / 2;

        applyWaveImpact(
                impactX, impactY,
                allPackets, allViews,
                a, b
        );
    }

    public void applyWaveImpact(double impactX, double impactY,
                                List<Packet> packets, List<PacketView> views,
                                Packet a, Packet b
    ) {
        final double WAVE_RADIUS = 500.0;
        final double WAVE_POWER = 1000.0;

        for (int i = 0; i < packets.size(); i++) {
            Packet p = packets.get(i);

            if (!p.isAlive() || p == a || p == b) continue;

            PacketView pv = views.get(i);
            double px = pv.getNode().getLayoutX() + pv.getNode().getTranslateX();
            double py = pv.getNode().getLayoutY() + pv.getNode().getTranslateY();

            double dx = px - impactX;
            double dy = py - impactY;
            double dist = Math.hypot(dx, dy);

            if (dist == 0 || dist > WAVE_RADIUS) continue;

            double power = WAVE_POWER / dist;
            double nx = dx / dist;
            double ny = dy / dist;

            p.addDeviation(power);
            p.increaseNoise(power / 7);
            System.out.println(p.getId()+ "  :  " + p.getNoise());
            if (p.getNoise() >= p.getSize()) {
                p.kill();
            }


            pv.getNode().setTranslateX(pv.getNode().getTranslateX() + nx * power);
            pv.getNode().setTranslateY(pv.getNode().getTranslateY() + ny * power);

        }
    }

    public void disableImpactForSeconds(int seconds) {
        this.impactDisabled = true;
        this.impactDisableUntil = System.currentTimeMillis() + seconds * 1000L;
    }

    public boolean isImpactDisabled() {
        return impactDisabled;
    }

    public void disableCollisionForSeconds(int seconds) {
        collisionDisabled = true;
        collisionDisableUntil = System.currentTimeMillis() + seconds * 1000L;
    }


}
