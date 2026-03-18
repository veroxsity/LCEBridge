package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=101 -> close container.
 * Wire: [byte containerId]
 */
public class ContainerClosePacket implements LcePacket {
    public static final int ID = 101;

    public int containerId;

    @Override
    public int getId() {
        return ID;
    }
}
