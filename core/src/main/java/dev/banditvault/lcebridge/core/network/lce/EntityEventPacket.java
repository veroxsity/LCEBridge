package dev.banditvault.lcebridge.core.network.lce;

/**
 * ID 38 — EntityEvent (S→C).
 * eventId: 2 = hurt, 3 = death.
 */
public class EntityEventPacket implements LcePacket {
    public static final int ID = 38;
    public int entityId;
    public int eventId;

    @Override
    public int getId() {
        return ID;
    }
}
