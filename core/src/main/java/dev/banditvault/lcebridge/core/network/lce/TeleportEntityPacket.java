package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=34 -> absolute entity position update.
 *
 * Win64 builds use large-world coordinates, so x/y/z are 32x fixed-point ints.
 */
public class TeleportEntityPacket implements LcePacket {
    public static final int ID = 34;

    public int entityId;
    public int x;
    public int y;
    public int z;
    public int yaw;
    public int pitch;

    @Override
    public int getId() {
        return ID;
    }
}
