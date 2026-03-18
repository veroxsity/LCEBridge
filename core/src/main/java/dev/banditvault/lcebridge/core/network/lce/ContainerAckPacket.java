package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=106 -> container click acknowledgement.
 * Wire: [byte containerId][short uid][byte accepted]
 */
public class ContainerAckPacket implements LcePacket {
    public static final int ID = 106;

    public int containerId;
    public int uid;
    public boolean accepted;

    @Override
    public int getId() {
        return ID;
    }
}
