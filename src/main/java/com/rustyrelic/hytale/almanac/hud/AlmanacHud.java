package com.rustyrelic.hytale.almanac.hud;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.rustyrelic.hytale.almanac.components.AlmanacDisplayConfig;
import com.rustyrelic.hytale.almanac.components.HudCorner;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Rebuilt from scratch on every {@link #show()} (clear=true), not patched incrementally.
 * Rows are the three {@code Label}s statically declared in {@code Almanac.ui} — never
 * generated via {@code appendInline} — and repositioned with an explicit per-row
 * {@link Anchor}/{@link Value} pair computed in Java, the same verified mechanism used for
 * the group's own anchor. A disabled row is pushed off-screen and blanked rather than
 * removed from a flow layout, so every selector/type combination here is one already proven
 * working in this codebase (v1's static HUD, or the runtime style-mutation spike) — no
 * generated markup strings, no raw-string Background/Padding mutation.
 */
public class AlmanacHud extends CustomUIHud {

    public static final String KEY = "almanac";

    private static final int BASE_WIDTH = 250;
    private static final int BASE_PADDING = 10;
    private static final int BASE_COORDS_FONT = 18;
    private static final int BASE_BIOME_FONT = 16;
    private static final int BASE_TIME_FONT = 18;
    private static final String BIOME_COLOR = "#96a9be";

    /** Row box height as a multiple of its font size. Tighter than EyeSpy's 2x convention — condensed per feedback. */
    private static final double LINE_HEIGHT_RATIO = 1.2;

    /** Scale change per size level. Half of the original level/3.0 step (which was ~0.33 per level). */
    private static final double SIZE_STEP = 1.0 / 6.0;

    @Nonnull
    private HudCorner corner = HudCorner.TOP_LEFT;
    private int margin = 10;
    private int sizeLevel = 3;

    @Nullable
    private String coordsText;
    @Nullable
    private String biomeText;
    @Nullable
    private String timeText;

    public AlmanacHud(@Nonnull PlayerRef playerRef) {
        super(playerRef, KEY);
    }

    /** Applies persisted position/style prefs. Safe to call on an already-shown HUD to re-style it live. */
    public void applyConfig(@Nonnull AlmanacDisplayConfig config) {
        this.corner = config.getCorner();
        this.margin = config.getMargin();
        this.sizeLevel = config.getSizeLevel();
    }

    /** Each row is {@code null} when its toggle is off, so {@link #build} simply omits it. */
    @SuppressWarnings("null") // String.format(...) never returns null
    public void updateContent(@Nullable Vector3d pos, @Nullable String biomeName, @Nullable String timeText) {
        this.coordsText = pos == null ? null : String.format("X: %.0f  Y: %.0f  Z: %.0f", pos.x(), pos.y(), pos.z());
        this.biomeText = biomeName;
        this.timeText = timeText;
    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        ui.append("RelicsAlmanac/Hud/Almanac.ui");

        double scale = 1.0 + (sizeLevel - 3) * SIZE_STEP;
        int padding = scaled(BASE_PADDING, scale);
        int width = scaled(BASE_WIDTH, scale);
        int coordsFont = scaled(BASE_COORDS_FONT, scale);
        int biomeFont = scaled(BASE_BIOME_FONT, scale);
        int timeFont = scaled(BASE_TIME_FONT, scale);
        int rowWidth = width - padding * 2;

        int top = padding;
        top = row(ui, "CoordsText", coordsText, coordsFont, null, padding, rowWidth, top);
        top = row(ui, "BiomeText", biomeText, biomeFont, BIOME_COLOR, padding, rowWidth, top);
        top = row(ui, "TimeText", timeText, timeFont, null, padding, rowWidth, top);

        Anchor anchor = new Anchor();
        anchor.setWidth(Value.of(width));
        anchor.setHeight(Value.of(top + padding));
        applyCorner(anchor);
        ui.setObject("#AlmanacHud.Anchor", anchor);
    }

    /**
     * Positions one row via an explicit per-label {@link Anchor} (same verified mechanism as
     * the group's own anchor) and pushes its text/style via the selectors proven in the
     * original style-mutation spike. A {@code null} value pushes the row off-screen with
     * blank text instead of removing it, so nothing here depends on unverified markup
     * generation or flow-layout child removal.
     *
     * @return the Top offset the next visible row should start at.
     */
    private static int row(@Nonnull UICommandBuilder ui, @Nonnull String id, @Nullable String text, int fontSize, @Nullable String color, int padding, int rowWidth, int top) {
        int height = (int) Math.round(fontSize * LINE_HEIGHT_RATIO);
        Anchor anchor = new Anchor();
        anchor.setLeft(Value.of(padding));
        anchor.setWidth(Value.of(rowWidth));
        if (text == null) {
            anchor.setTop(Value.of(-9999));
            anchor.setHeight(Value.of(0));
            ui.setObject("#" + id + ".Anchor", anchor);
            ui.set("#" + id + ".Text", "");
            return top;
        }
        anchor.setTop(Value.of(top));
        anchor.setHeight(Value.of(height));
        ui.setObject("#" + id + ".Anchor", anchor);
        ui.set("#" + id + ".Style.FontSize", (float) fontSize);
        if (color != null) {
            ui.set("#" + id + ".Style.TextColor", color);
        }
        ui.set("#" + id + ".Text", text);
        return top + height;
    }

    private void applyCorner(@Nonnull Anchor anchor) {
        switch (corner) {
            case TOP_LEFT -> {
                anchor.setTop(Value.of(margin));
                anchor.setLeft(Value.of(margin));
            }
            case TOP_RIGHT -> {
                anchor.setTop(Value.of(margin));
                anchor.setRight(Value.of(margin));
            }
            case BOTTOM_LEFT -> {
                anchor.setBottom(Value.of(margin));
                anchor.setLeft(Value.of(margin));
            }
            case BOTTOM_RIGHT -> {
                anchor.setBottom(Value.of(margin));
                anchor.setRight(Value.of(margin));
            }
        }
    }

    private static int scaled(int base, double scale) {
        return (int) Math.round(base * scale);
    }

    /**
     * Adds, removes, or re-styles the HUD so its presence and appearance match the player's
     * persisted prefs. Called from the toggle/position/size commands and from the
     * player-ready listener, since a rejoin restores the saved component but doesn't
     * re-attach the HUD on its own.
     */
    public static void sync(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull AlmanacDisplayConfig config) {
        HudManager hudManager = player.getHudManager();
        CustomUIHud existing = hudManager.getCustomHud(KEY);

        if (config.shouldDisplay()) {
            if (existing instanceof AlmanacHud almanacHud) {
                almanacHud.applyConfig(config);
                almanacHud.show();
            } else {
                AlmanacHud hud = new AlmanacHud(playerRef);
                hud.applyConfig(config);
                hudManager.addCustomHud(playerRef, hud);
            }
        } else if (existing != null) {
            hudManager.removeCustomHud(playerRef, KEY);
        }
    }
}
