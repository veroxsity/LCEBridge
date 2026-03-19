package dev.banditvault.lcebridge.core.network.lce;

import java.util.ArrayList;
import java.util.List;

/**
 * LCE id=24 -> living entity spawn.
 *
 * Win64 builds use large-world coordinates, so x/y/z are 32x fixed-point ints.
 */
public class AddMobPacket implements LcePacket {
    public static final int ID = 24;

    public int entityId;
    public int type;
    public int x;
    public int y;
    public int z;
    public int yaw;
    public int pitch;
    public int headYaw;
    public int motionX;
    public int motionY;
    public int motionZ;
    public List<SetEntityDataPacket.DataValue> metadata = new ArrayList<>();

    @Override
    public int getId() {
        return ID;
    }
}
