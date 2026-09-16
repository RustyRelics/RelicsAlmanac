package com.rustyrelic.hytale.almanac.components;

/**
 * Read-only view of a player's Almanac display settings.
 * Code that only needs to read the toggles depends on this, not on
 * {@link AlmanacPlayerData} itself, so it can never mutate it.
 */
public interface AlmanacDisplayConfig {

    boolean isShowCoords();

    boolean isShowBiome();

    boolean isShowTime();

    /** Master on/off switch — see {@link #shouldDisplay()}. */
    boolean isAllEnabled();

    /** Whether the HUD should currently be visible: the master switch, and at least one row enabled. */
    default boolean shouldDisplay() {
        return isAllEnabled() && (isShowCoords() || isShowBiome() || isShowTime());
    }

    HudCorner getCorner();

    int getMargin();

    /** 1-9, where 3 is the hand-tuned default all other levels scale from. */
    int getSizeLevel();
}
