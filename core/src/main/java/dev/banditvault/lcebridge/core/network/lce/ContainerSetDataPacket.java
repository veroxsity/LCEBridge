package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=105 -> container progress/property update.
 * Wire: [byte containerId][short id][short value]
 */
public class ContainerSetDataPacket implements LcePacket {
    public static final int ID = 105;

    public int containerId;
    public int id;
    public int value;

    @Override
    public int getId() {
        return ID;
    }
}
