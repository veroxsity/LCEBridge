package dev.banditvault.lcebridge.core.network.lce;

/**
 * Minimal Legacy Console Edition item stack representation.
 * NBT is intentionally omitted for now; bridge-generated items are tagless.
 */
public class LceItemStack {
    public final int id;
    public final int count;
    public final int damage;

    public LceItemStack(int id, int count, int damage) {
        this.id = id;
        this.count = count;
        this.damage = damage;
    }
}
