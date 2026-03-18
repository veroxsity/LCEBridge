package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=53 -> single block update.
 */
public class TileUpdatePacket implements LcePacket {
    public static final int ID = 53;

    public int x;
    public int y;
    public int z;
    public int block;
    public int data;
    public int levelIdx;

    @Override
    public int getId() {
        return ID;
    }
}
