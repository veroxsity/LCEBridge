package dev.banditvault.lcebridge.core.network.lce;

import java.util.ArrayList;
import java.util.List;

/**
 * LCE id=20 -> player entity spawn.
 *
 * Wire format (from AddPlayerPacket::write in source):
 *   int entityId
 *   utf16 name
 *   int x, y, z       (32x fixed-point, LARGE_WORLDS)
 *   byte yRot, xRot, yHeadRot
 *   short carriedItem
 *   long offlineXuid   (PlayerUID = 8 bytes on Win64)
 *   long onlineXuid
 *   byte playerIndex
 *   int skinId
 *   int capeId
 *   int gamePrivileges
 *   SynchedEntityData metadata (packed, terminated by 0x7F)
 */
public class AddPlayerPacket implements LcePacket {
    public static final int ID = 20;

    public int entityId;
    public String name = "";
    public int x, y, z;
    public int yaw, pitch, headYaw;
    public int carriedItem;
    public long offlineXuid;
    public long onlineXuid;
    public int playerIndex;
    public int skinId;
    public int capeId;
    public int gamePrivileges;
    public List<SetEntityDataPacket.DataValue> metadata = new ArrayList<>();

    @Override
    public int getId() {
        return ID;
    }
}
