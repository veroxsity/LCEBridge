package dev.banditvault.lcebridge.core.network.lce;

/**
 * ID 205 — ClientCommand (C→S).
 * action: 0 = LOGIN_COMPLETE, 1 = PERFORM_RESPAWN
 */
public class ClientCommandPacket implements LcePacket {
    public static final int ID = 205;
    public int action;

    @Override
    public int getId() {
        return ID;
    }
}
