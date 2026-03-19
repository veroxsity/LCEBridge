package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=28 -> entity velocity/knockback.
 *
 * Wire format from source (SetEntityMotionPacket::write):
 *   short (entityId | 0x800 if compact)
 *   if compact: 3x byte (xa/16, ya/16, za/16)
 *   else:       3x short (xa, ya, za)
 *
 * Velocity units: value * 8000.0 = blocks/tick
 */
public class SetEntityMotionPacket implements LcePacket {
    public static final int ID = 28;

    public int entityId;
    public int xa, ya, za;

    @Override
    public int getId() {
        return ID;
    }
}
