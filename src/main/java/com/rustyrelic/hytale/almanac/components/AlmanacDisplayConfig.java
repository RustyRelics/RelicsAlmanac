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

    /**
     * Horizontal nudge from the corner position, in pixels. <b>Screen-space sign convention,
     * identical for all four corners:</b> positive moves the HUD right, negative moves it left.
     * It is deliberately <em>not</em> "away from the anchored edge" — see
     * {@code AlmanacHud#applyCorner} for how that gets translated per corner.
     */
    int getOffsetX();

    /**
     * Vertical nudge from the corner position, in pixels. Screen-space sign convention,
     * identical for all four corners: positive moves the HUD <b>down</b>, negative moves it up.
     */
    int getOffsetY();

    /** 1-9, where 3 is the hand-tuned default all other levels scale from. */
    int getSizeLevel();
}
