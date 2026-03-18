package dev.banditvault.lcebridge.core.network.lce;

/**
 * ID 9 — Respawn request (C→S fallback).
 * Some clients may emit this instead of ClientCommand action=1.
 */
public class RespawnRequestPacket implements LcePacket {
    public static final int ID = 9;

    @Override
    public int getId() {
        return ID;
    }
}
