package dev.banditvault.lcebridge.core.network.lce;

import dev.banditvault.lcebridge.core.util.LceByteReader;
import dev.banditvault.lcebridge.core.util.LceByteWriter;

/**
 * Reads and writes the legacy item wire format shared by inventory packets.
 */
public final class LceItemCodec {
    private LceItemCodec() {
    }

    public static LceItemStack readItem(LceByteReader reader) {
        int id = reader.readShort();
        if (id < 0) {
            return null;
        }

        int count = reader.readUnsignedByte();
        int damage = reader.readShort() & 0xFFFF;
        int nbtSize = reader.readShort();
        if (nbtSize > 0) {
            reader.skipBytes(nbtSize);
        }
        return new LceItemStack(id, count, damage);
    }

    public static void writeItem(LceItemStack item, LceByteWriter writer) {
        if (item == null) {
            writer.writeShort(-1);
            return;
        }

        writer.writeShort(item.id);
        writer.writeByte(item.count);
        writer.writeShort(item.damage);
        writer.writeShort(-1);
    }
}
