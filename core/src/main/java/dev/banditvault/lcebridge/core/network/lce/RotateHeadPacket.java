package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=35 -> head rotation update.
 * Wire: int entityId, byte yHeadRot
 */
public class RotateHeadPacket implements LcePacket {
    public static final int ID = 35;

    public int entityId;
    public int yHeadRot;

    @Override
    public int getId() {
        return ID;
    }
}
