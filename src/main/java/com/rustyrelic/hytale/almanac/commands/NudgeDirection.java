package com.rustyrelic.hytale.almanac.commands;

/**
 * Direction argument for {@code /almanac nudge}. Parsed case-insensitively by name
 * ({@code left}, {@code right}, {@code up}, {@code down}). Not persisted, so unlike
 * {@code HudCorner} there is no codec and the constants are free to change.
 * <p>
 * The unit vector is in <b>screen-space</b> (+X right, +Y down), matching the sign convention of
 * {@code AlmanacDisplayConfig#getOffsetX()} / {@code getOffsetY()}, so every direction moves the
 * HUD the way it reads on screen regardless of which corner the HUD is anchored to.
 */
public enum NudgeDirection {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1);

    private final int dx;
    private final int dy;

    NudgeDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }
}
