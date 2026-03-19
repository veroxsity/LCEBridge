package dev.banditvault.lcebridge.core.network.lce;

import java.util.ArrayList;
import java.util.List;

/**
 * LCE id=40 -> entity metadata update.
 */
public class SetEntityDataPacket implements LcePacket {
    public static final int ID = 40;

    public static final int TYPE_BYTE = 0;
    public static final int TYPE_SHORT = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_ITEMINSTANCE = 5;

    public record DataValue(int id, int type, Object value) {
    }

    public int entityId;
    public List<DataValue> values = new ArrayList<>();

    @Override
    public int getId() {
        return ID;
    }
}
