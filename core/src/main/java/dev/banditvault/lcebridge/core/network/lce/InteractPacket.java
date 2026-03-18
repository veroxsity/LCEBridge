package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=7 -> entity interaction / attack.
 * Wire: [int source][int target][byte action]
 */
public class InteractPacket implements LcePacket {
    public static final int ID = 7;

    public int source;
    public int target;
    public int action;

    @Override
    public int getId() {
        return ID;
    }
}
