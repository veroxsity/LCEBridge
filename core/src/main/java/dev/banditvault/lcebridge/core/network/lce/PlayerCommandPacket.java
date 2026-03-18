package dev.banditvault.lcebridge.core.network.lce;

/**
 * LCE id=19 — PlayerCommand (C→S).
 * Wire: [int entityId][byte action][int data]
 * Actions: 1=startSneak, 2=stopSneak, 3=stopSleep, 4=startSprint, 5=stopSprint,
 *          6=startIdleAnim, 7=stopIdleAnim, 8=ridingJump, 9=openInventory
 */
public class PlayerCommandPacket implements LcePacket {
    public static final int ID = 19;
    public int entityId;
    public int action;
    public int data;
    @Override public int getId() { return ID; }
}
