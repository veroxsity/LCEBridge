package dev.banditvault.lcebridge.core.network.lce;

import java.util.ArrayList;
import java.util.List;

/**
 * LCE id=104 -> full container contents snapshot.
 * Wire: [byte containerId][short count][count * item]
 */
public class ContainerSetContentPacket implements LcePacket {
    public static final int ID = 104;

    public int containerId;
    public List<LceItemStack> items = new ArrayList<>();

    @Override
    public int getId() {
        return ID;
    }
}
