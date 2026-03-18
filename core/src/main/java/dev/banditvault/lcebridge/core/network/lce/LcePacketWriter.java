package dev.banditvault.lcebridge.core.network.lce;

import dev.banditvault.lcebridge.core.util.LceByteWriter;
import io.netty.buffer.ByteBuf;

/**
 * Encodes a typed LcePacket into raw bytes (no length prefix -
 * LcePacketEncoder adds that wrapper).
 */
public class LcePacketWriter {

    public static void write(LcePacket pkt, ByteBuf out) {
        LceByteWriter w = new LceByteWriter(out);
        switch (pkt) {
            case KeepAlivePacket p -> writeKeepAlive(p, w);
            case DisconnectPacket p -> writeDisconnect(p, w);
            case SetTimePacket p -> writeSetTime(p, w);
            case SetSpawnPositionPacket p -> writeSetSpawnPosition(p, w);
            case PlayerAbilitiesPacket p -> writePlayerAbilities(p, w);
            case SetCarriedItemPacket p -> writeSetCarriedItem(p, w);
            case GameEventPacket p -> writeGameEvent(p, w);
            case EntityEventPacket p -> writeEntityEvent(p, w);
            case SetHealthPacket p -> writeSetHealth(p, w);
            case ChatPacket p -> writeChat(p, w);
            case ContainerOpenPacket p -> writeContainerOpen(p, w);
            case ContainerClosePacket p -> writeContainerClose(p, w);
            case ContainerSetSlotPacket p -> writeContainerSetSlot(p, w);
            case ContainerSetContentPacket p -> writeContainerSetContent(p, w);
            case ContainerSetDataPacket p -> writeContainerSetData(p, w);
            case ContainerAckPacket p -> writeContainerAck(p, w);
            case ChunkVisibilityPacket p -> writeChunkVisibility(p, w);
            case ChunkVisibilityAreaPacket p -> writeChunkVisibilityArea(p, w);
            case BlockRegionUpdatePacket p -> writeBlockRegionUpdate(p, w);
            case TileUpdatePacket p -> writeTileUpdate(p, w);
            default -> throw new IllegalArgumentException("No encoder for LCE packet id=" + pkt.getId());
        }
    }

    private static void writeKeepAlive(KeepAlivePacket p, LceByteWriter w) {
        w.writeByte(KeepAlivePacket.ID);
        w.writeInt(p.keepAliveId);
    }

    private static void writeDisconnect(DisconnectPacket p, LceByteWriter w) {
        w.writeByte(DisconnectPacket.ID);
        w.writeInt(p.reason);
    }

    private static void writeSetTime(SetTimePacket p, LceByteWriter w) {
        w.writeByte(SetTimePacket.ID);
        w.writeLong(p.gameTime);
        w.writeLong(p.dayTime);
    }

    private static void writeSetSpawnPosition(SetSpawnPositionPacket p, LceByteWriter w) {
        w.writeByte(SetSpawnPositionPacket.ID);
        w.writeInt(p.x);
        w.writeInt(p.y);
        w.writeInt(p.z);
    }

    private static void writePlayerAbilities(PlayerAbilitiesPacket p, LceByteWriter w) {
        w.writeByte(PlayerAbilitiesPacket.ID);
        w.writeByte(p.flags);
        w.writeFloat(p.flySpeed);
        w.writeFloat(p.walkSpeed);
    }

    private static void writeSetCarriedItem(SetCarriedItemPacket p, LceByteWriter w) {
        w.writeByte(SetCarriedItemPacket.ID);
        w.writeShort(p.slot);
    }

    private static void writeGameEvent(GameEventPacket p, LceByteWriter w) {
        w.writeByte(GameEventPacket.ID);
        w.writeByte(p.reason);
        w.writeByte(p.param);
    }

    private static void writeEntityEvent(EntityEventPacket p, LceByteWriter w) {
        w.writeByte(EntityEventPacket.ID);
        w.writeInt(p.entityId);
        w.writeByte(p.eventId);
    }

    private static void writeSetHealth(SetHealthPacket p, LceByteWriter w) {
        w.writeByte(SetHealthPacket.ID);
        w.writeFloat(p.health);
        w.writeShort(p.food);
        w.writeFloat(p.saturation);
        w.writeByte(p.damageSource);
    }

    private static void writeChat(ChatPacket p, LceByteWriter w) {
        w.writeByte(ChatPacket.ID);
        w.writeShort(p.messageType);
        int sc = p.stringArgs == null ? 0 : p.stringArgs.size();
        int ic = p.intArgs == null ? 0 : p.intArgs.size();
        short packed = (short) (((sc & 0xF) << 4) | (ic & 0xF));
        w.writeShort(packed);
        if (p.stringArgs != null) {
            for (String s : p.stringArgs) {
                w.writeUtf16(s);
            }
        }
        if (p.intArgs != null) {
            for (int i : p.intArgs) {
                w.writeInt(i);
            }
        }
    }

    private static void writeContainerOpen(ContainerOpenPacket p, LceByteWriter w) {
        w.writeByte(ContainerOpenPacket.ID);
        w.writeByte(p.containerId & 0xFF);
        w.writeByte(p.type & 0xFF);
        w.writeByte(p.size & 0xFF);
        w.writeByte(p.customName ? 1 : 0);
        if (p.type == ContainerOpenPacket.HORSE) {
            w.writeInt(p.entityId);
        }
        if (p.customName) {
            w.writeUtf16(p.title);
        }
    }

    private static void writeContainerClose(ContainerClosePacket p, LceByteWriter w) {
        w.writeByte(ContainerClosePacket.ID);
        w.writeByte(p.containerId);
    }

    private static void writeContainerSetSlot(ContainerSetSlotPacket p, LceByteWriter w) {
        w.writeByte(ContainerSetSlotPacket.ID);
        w.writeByte(p.containerId);
        w.writeShort(p.slot);
        LceItemCodec.writeItem(p.item, w);
    }

    private static void writeContainerSetContent(ContainerSetContentPacket p, LceByteWriter w) {
        w.writeByte(ContainerSetContentPacket.ID);
        w.writeByte(p.containerId);
        int count = p.items == null ? 0 : p.items.size();
        w.writeShort(count);
        if (p.items != null) {
            for (LceItemStack item : p.items) {
                LceItemCodec.writeItem(item, w);
            }
        }
    }

    private static void writeContainerSetData(ContainerSetDataPacket p, LceByteWriter w) {
        w.writeByte(ContainerSetDataPacket.ID);
        w.writeByte(p.containerId);
        w.writeShort(p.id);
        w.writeShort(p.value);
    }

    private static void writeContainerAck(ContainerAckPacket p, LceByteWriter w) {
        w.writeByte(ContainerAckPacket.ID);
        w.writeByte(p.containerId);
        w.writeShort(p.uid);
        w.writeByte(p.accepted ? 1 : 0);
    }

    private static void writeChunkVisibility(ChunkVisibilityPacket p, LceByteWriter w) {
        w.writeByte(ChunkVisibilityPacket.ID);
        w.writeInt(p.chunkX);
        w.writeInt(p.chunkZ);
        w.writeByte(p.visible ? 1 : 0);
    }

    private static void writeChunkVisibilityArea(ChunkVisibilityAreaPacket p, LceByteWriter w) {
        w.writeByte(ChunkVisibilityAreaPacket.ID);
        w.writeInt(p.minCX);
        w.writeInt(p.maxCX);
        w.writeInt(p.minCZ);
        w.writeInt(p.maxCZ);
    }

    private static void writeBlockRegionUpdate(BlockRegionUpdatePacket p, LceByteWriter w) {
        w.writeByte(BlockRegionUpdatePacket.ID);

        byte chunkFlags = 0;
        if (p.isFullChunk) {
            chunkFlags |= 0x01;
        }
        if (p.ys == 0) {
            chunkFlags |= 0x02;
        }
        w.writeByte(chunkFlags);

        w.writeInt(p.x);
        w.writeShort(p.y);
        w.writeInt(p.z);
        w.writeByte((byte) (p.xs - 1));
        w.writeByte((byte) (p.ys - 1));
        w.writeByte((byte) (p.zs - 1));

        int sizeAndLevel = p.compressedData.length;
        sizeAndLevel |= (p.levelIdx << 30);
        w.writeInt(sizeAndLevel);

        w.buffer().writeBytes(p.compressedData);
    }

    private static void writeTileUpdate(TileUpdatePacket p, LceByteWriter w) {
        w.writeByte(TileUpdatePacket.ID);
        int xyzData = ((p.x & 0x3FF) << 22) | ((p.y & 0xFF) << 14) | ((p.z & 0x3FF) << 4) | (p.data & 0xF);
        w.writeInt(xyzData);
        w.writeShort(p.block);
        w.writeByte(p.levelIdx & 0xFF);
    }
}
