package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=14 — PlayerAction (C→S, block digging / item actions).
 * Wire: [byte action][int x][byte y][int z][byte face]
 * Actions: 0=startDig, 1=abortDig, 2=stopDig, 3=dropAll, 4=dropItem, 5=releaseUseItem
 */
public class PlayerActionPacket implements LcePacket {
    public static final int ID = 14;
    public int action;
    public int x;
    public int y;
    public int z;
    public int face;
    @Override public int getId() { return ID; }
}
