package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=15 -> place block / use item.
 * Wire: [int x][ubyte y][int z][ubyte face][item][ubyte clickX][ubyte clickY][ubyte clickZ]
 */
public class UseItemPacket implements LcePacket {
    public static final int ID = 15;

    public int x;
    public int y;
    public int z;
    public int face;
    public LceItemStack item;
    public float clickX;
    public float clickY;
    public float clickZ;

    @Override
    public int getId() {
        return ID;
    }
}
