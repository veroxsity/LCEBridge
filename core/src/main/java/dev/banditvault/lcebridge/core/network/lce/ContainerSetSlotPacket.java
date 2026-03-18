package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=103 -> set one slot.
 * Wire: [byte containerId][short slot][item]
 */
public class ContainerSetSlotPacket implements LcePacket {
    public static final int ID = 103;

    public int containerId;
    public int slot;
    public LceItemStack item;

    @Override
    public int getId() {
        return ID;
    }
}
