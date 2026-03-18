package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=102 -> container click.
 * Wire: [byte containerId][short slot][byte button][short uid][byte clickType][item]
 */
public class ContainerClickPacket implements LcePacket {
    public static final int ID = 102;

    public int containerId;
    public int slotNum;
    public int buttonNum;
    public int uid;
    public int clickType;
    public LceItemStack item;

    @Override
    public int getId() {
        return ID;
    }
}
