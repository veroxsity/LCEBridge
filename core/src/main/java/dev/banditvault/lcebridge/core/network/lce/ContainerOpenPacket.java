package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=100 -> open server-controlled container UI.
 */
public class ContainerOpenPacket implements LcePacket {
    public static final int ID = 100;

    public static final int CONTAINER = 0;
    public static final int WORKBENCH = 1;
    public static final int FURNACE = 2;
    public static final int TRAP = 3;
    public static final int ENCHANTMENT = 4;
    public static final int BREWING_STAND = 5;
    public static final int TRADER_NPC = 6;
    public static final int BEACON = 7;
    public static final int REPAIR_TABLE = 8;
    public static final int HOPPER = 9;
    public static final int DROPPER = 10;
    public static final int HORSE = 11;
    public static final int FIREWORKS = 12;
    public static final int BONUS_CHEST = 13;
    public static final int LARGE_CHEST = 14;
    public static final int ENDER_CHEST = 15;
    public static final int MINECART_CHEST = 16;
    public static final int MINECART_HOPPER = 17;

    public int containerId;
    public int type;
    public int size;
    public boolean customName;
    public String title = "";
    public int entityId;

    @Override
    public int getId() {
        return ID;
    }
}
