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
            case AddPlayerPacket p -> writeAddPlayer(p, w);
            case AddEntityPacket p -> writeAddEntity(p, w);
            case AddMobPacket p -> writeAddMob(p, w);
            case TeleportEntityPacket p -> writeTeleportEntity(p, w);
            case RotateHeadPacket p -> writeRotateHead(p, w);
            case SetEntityMotionPacket p -> writeSetEntityMotion(p, w);
            case RemoveEntitiesPacket p -> writeRemoveEntities(p, w);
            case SetEntityDataPacket p -> writeSetEntityData(p, w);
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

    private static void writeAddPlayer(AddPlayerPacket p, LceByteWriter w) {
        w.writeByte(AddPlayerPacket.ID);
        w.writeInt(p.entityId);
        w.writeUtf16(p.name);
        w.writeInt(p.x);
        w.writeInt(p.y);
        w.writeInt(p.z);
        w.writeByte(p.yaw);
        w.writeByte(p.pitch);
        w.writeByte(p.headYaw);
        w.writeShort(p.carriedItem);
        w.writeLong(p.offlineXuid);
        w.writeLong(p.onlineXuid);
        w.writeByte(p.playerIndex);
        w.writeInt(p.skinId);
        w.writeInt(p.capeId);
        w.writeInt(p.gamePrivileges);
        writeEntityMetadata(p.metadata, w);
    }

    private static void writeAddEntity(AddEntityPacket p, LceByteWriter w) {
        w.writeByte(AddEntityPacket.ID);
        w.writeShort(p.entityId);
        w.writeByte(p.type);
        w.writeInt(p.x);
        w.writeInt(p.y);
        w.writeInt(p.z);
        w.writeByte(p.yaw);
        w.writeByte(p.pitch);
        w.writeInt(p.data);
        if (p.data > -1) {
            w.writeShort(p.motionX);
            w.writeShort(p.motionY);
            w.writeShort(p.motionZ);
        }
    }

    private static void writeAddMob(AddMobPacket p, LceByteWriter w) {
        w.writeByte(AddMobPacket.ID);
        w.writeShort(p.entityId);
        w.writeByte(p.type);
        w.writeInt(p.x);
        w.writeInt(p.y);
        w.writeInt(p.z);
        w.writeByte(p.yaw);
        w.writeByte(p.pitch);
        w.writeByte(p.headYaw);
        w.writeShort(p.motionX);
        w.writeShort(p.motionY);
        w.writeShort(p.motionZ);
        writeEntityMetadata(p.metadata, w);
    }

    private static void writeTeleportEntity(TeleportEntityPacket p, LceByteWriter w) {
        w.writeByte(TeleportEntityPacket.ID);
        w.writeShort(p.entityId);
        w.writeInt(p.x);
        w.writeInt(p.y);
        w.writeInt(p.z);
        w.writeByte(p.yaw);
        w.writeByte(p.pitch);
    }

    private static void writeRotateHead(RotateHeadPacket p, LceByteWriter w) {
        w.writeByte(RotateHeadPacket.ID);
        w.writeInt(p.entityId);
        w.writeByte(p.yHeadRot);
    }

    private static void writeSetEntityMotion(SetEntityMotionPacket p, LceByteWriter w) {
        w.writeByte(SetEntityMotionPacket.ID);
        // 4J compact mode: if all velocities fit in signed bytes (*16), use 0x800 flag
        boolean compact = (p.xa >= -2048 && p.xa < 2048)
                        && (p.ya >= -2048 && p.ya < 2048)
                        && (p.za >= -2048 && p.za < 2048);
        if (compact) {
            w.writeShort(p.entityId | 0x800);
            w.writeByte(p.xa / 16);
            w.writeByte(p.ya / 16);
            w.writeByte(p.za / 16);
        } else {
            w.writeShort(p.entityId);
            w.writeShort(p.xa);
            w.writeShort(p.ya);
            w.writeShort(p.za);
        }
    }

    private static void writeRemoveEntities(RemoveEntitiesPacket p, LceByteWriter w) {
        w.writeByte(RemoveEntitiesPacket.ID);
        int count = Math.min(255, p.entityIds == null ? 0 : p.entityIds.size());
        w.writeByte(count);
        for (int i = 0; i < count; i++) {
            w.writeInt(p.entityIds.get(i));
        }
    }

    private static void writeSetEntityData(SetEntityDataPacket p, LceByteWriter w) {
        w.writeByte(SetEntityDataPacket.ID);
        w.writeInt(p.entityId);
        writeEntityMetadata(p.values, w);
    }

    private static void writeEntityMetadata(java.util.List<SetEntityDataPacket.DataValue> values, LceByteWriter w) {
        if (values != null) {
            for (SetEntityDataPacket.DataValue value : values) {
                if (value == null) {
                    continue;
                }
                int header = ((value.type() << 5) | (value.id() & 0x1F)) & 0xFF;
                w.writeByte(header);
                switch (value.type()) {
                    case SetEntityDataPacket.TYPE_BYTE -> w.writeByte(((Number) value.value()).intValue());
                    case SetEntityDataPacket.TYPE_SHORT -> w.writeShort(((Number) value.value()).intValue());
                    case SetEntityDataPacket.TYPE_INT -> w.writeInt(((Number) value.value()).intValue());
                    case SetEntityDataPacket.TYPE_FLOAT -> w.writeFloat(((Number) value.value()).floatValue());
                    case SetEntityDataPacket.TYPE_STRING -> w.writeUtf16((String) value.value());
                    case SetEntityDataPacket.TYPE_ITEMINSTANCE -> LceItemCodec.writeItem((LceItemStack) value.value(), w);
                    default -> throw new IllegalArgumentException("Unsupported LCE metadata type " + value.type());
                }
            }
        }
        w.writeByte(0x7F);
    }
}
