package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=108 -> container button/menu action.
 * Wire: [byte containerId][byte buttonId]
 */
public class ContainerButtonClickPacket implements LcePacket {
    public static final int ID = 108;

    public int containerId;
    public int buttonId;

    @Override
    public int getId() {
        return ID;
    }
}
