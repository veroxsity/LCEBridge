package dev.banditvault.lcebridge.core.network.lce;

import java.util.ArrayList;
import java.util.List;

/**
 * LCE id=29 -> remove one or more entities.
 */
public class RemoveEntitiesPacket implements LcePacket {
    public static final int ID = 29;

    public List<Integer> entityIds = new ArrayList<>();

    @Override
    public int getId() {
        return ID;
    }
}
