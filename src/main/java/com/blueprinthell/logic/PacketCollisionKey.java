package com.blueprinthell.logic;

import com.blueprinthell.model.Packet;

import java.util.Objects;

public class PacketCollisionKey {
    private final int idA, idB;

    public PacketCollisionKey(Packet a, Packet b) {
        if (a.getId() < b.getId()) {
            this.idA = a.getId();
            this.idB = b.getId();
        } else {
            this.idA = b.getId();
            this.idB = a.getId();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PacketCollisionKey that)) return false;
        return idA == that.idA && idB == that.idB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idA, idB);
    }

    @Override
    public String toString() {
        return "Collision[" + idA + "," + idB + "]";
    }
}
