package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=23 -> generic non-living entity spawn.
 *
 * Win64 builds use large-world coordinates, so x/y/z are 32x fixed-point ints.
 */
public class AddEntityPacket implements LcePacket {
    public static final int ID = 23;

    public static final int BOAT = 1;
    public static final int ITEM = 2;
    public static final int MINECART = 10;
    public static final int PRIMED_TNT = 50;
    public static final int ENDER_CRYSTAL = 51;
    public static final int ARROW = 60;
    public static final int SNOWBALL = 61;
    public static final int EGG = 62;
    public static final int FIREBALL = 63;
    public static final int SMALL_FIREBALL = 64;
    public static final int THROWN_ENDERPEARL = 65;
    public static final int WITHER_SKULL = 66;
    public static final int FALLING = 70;
    public static final int ITEM_FRAME = 71;
    public static final int EYEOFENDERSIGNAL = 72;
    public static final int THROWN_POTION = 73;
    public static final int FALLING_EGG = 74;
    public static final int THROWN_EXPBOTTLE = 75;
    public static final int FIREWORKS = 76;
    public static final int LEASH_KNOT = 77;
    public static final int FISH_HOOK = 90;
    public static final int DRAGON_FIRE_BALL = 200;

    public int entityId;
    public int type;
    public int x;
    public int y;
    public int z;
    public int yaw;
    public int pitch;
    public int data = -1;
    public int motionX;
    public int motionY;
    public int motionZ;

    @Override
    public int getId() {
        return ID;
    }
}
