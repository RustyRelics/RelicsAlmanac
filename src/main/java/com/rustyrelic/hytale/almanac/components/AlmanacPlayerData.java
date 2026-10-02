package com.rustyrelic.hytale.almanac.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * Per-player save data for which Almanac HUD rows are enabled.
 * Confirmed (via the MySecondMod spike) to survive both reconnects and full server restarts.
 */
public class AlmanacPlayerData implements Component<EntityStore>, AlmanacDisplayConfig {

    @Nonnull
    @SuppressWarnings("null") // BuilderCodec.builder(...).build() never returns null
    public static final BuilderCodec<AlmanacPlayerData> CODEC =
            BuilderCodec.builder(AlmanacPlayerData.class, AlmanacPlayerData::new)
                    .append(new KeyedCodec<>("ShowCoords", Codec.BOOLEAN), AlmanacPlayerData::setShowCoords, AlmanacPlayerData::isShowCoords)
                    .documentation("Whether the coordinates row is shown")
                    .add()
                    .append(new KeyedCodec<>("ShowBiome", Codec.BOOLEAN), AlmanacPlayerData::setShowBiome, AlmanacPlayerData::isShowBiome)
                    .documentation("Whether the biome row is shown")
                    .add()
                    .append(new KeyedCodec<>("ShowTime", Codec.BOOLEAN), AlmanacPlayerData::setShowTime, AlmanacPlayerData::isShowTime)
                    .documentation("Whether the time-of-day row is shown")
                    .add()
                    .append(new KeyedCodec<>("AllEnabled", Codec.BOOLEAN), AlmanacPlayerData::setAllEnabled, AlmanacPlayerData::isAllEnabled)
                    .documentation("Master on/off switch for the whole display")
                    .add()
                    .append(new KeyedCodec<>("Corner", HudCorner.CODEC), AlmanacPlayerData::setCorner, AlmanacPlayerData::getCorner)
                    .documentation("Which screen corner the HUD anchors to")
                    .add()
                    .append(new KeyedCodec<>("Margin", Codec.INTEGER), AlmanacPlayerData::setMargin, AlmanacPlayerData::getMargin)
                    .documentation("Pixel margin from the anchored corner")
                    .add()
                    // Added after v0.2.0. Absent from older saves, which is fine: BuilderCodec only
                    // visits keys present in the stored document, so these keep their field default (0).
                    .append(new KeyedCodec<>("OffsetX", Codec.INTEGER), AlmanacPlayerData::setOffsetX, AlmanacPlayerData::getOffsetX)
                    .documentation("Horizontal nudge from the corner in pixels; screen-space, positive = right")
                    .add()
                    .append(new KeyedCodec<>("OffsetY", Codec.INTEGER), AlmanacPlayerData::setOffsetY, AlmanacPlayerData::getOffsetY)
                    .documentation("Vertical nudge from the corner in pixels; screen-space, positive = down")
                    .add()
                    .append(new KeyedCodec<>("SizeLevel", Codec.INTEGER), AlmanacPlayerData::setSizeLevel, AlmanacPlayerData::getSizeLevel)
                    .documentation("Overall HUD scale, 1-9 (3 = default)")
                    .add()
                    .build();

    private static ComponentType<EntityStore, AlmanacPlayerData> componentType;

    private boolean showCoords = false;
    private boolean showBiome = false;
    private boolean showTime = false;
    private boolean allEnabled = true;
    private HudCorner corner = HudCorner.TOP_LEFT;
    private int margin = 10;
    private int offsetX = 0;
    private int offsetY = 0;
    private int sizeLevel = 3;

    @Override
    public boolean isShowCoords() {
        return showCoords;
    }

    public void setShowCoords(boolean showCoords) {
        this.showCoords = showCoords;
    }

    @Override
    public boolean isShowBiome() {
        return showBiome;
    }

    public void setShowBiome(boolean showBiome) {
        this.showBiome = showBiome;
    }

    @Override
    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    @Override
    public boolean isAllEnabled() {
        return allEnabled;
    }

    public void setAllEnabled(boolean allEnabled) {
        this.allEnabled = allEnabled;
    }

    @Override
    public HudCorner getCorner() {
        return corner;
    }

    public void setCorner(HudCorner corner) {
        this.corner = corner;
    }

    @Override
    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    /** Zeroes both nudge offsets, leaving the corner and margin alone. */
    public void resetOffsets() {
        this.offsetX = 0;
        this.offsetY = 0;
    }

    @Override
    public int getSizeLevel() {
        return sizeLevel;
    }

    public void setSizeLevel(int sizeLevel) {
        this.sizeLevel = sizeLevel;
    }

    @Override
    public Component<EntityStore> clone() {
        AlmanacPlayerData copy = new AlmanacPlayerData();
        copy.showCoords = this.showCoords;
        copy.showBiome = this.showBiome;
        copy.showTime = this.showTime;
        copy.allEnabled = this.allEnabled;
        copy.corner = this.corner;
        copy.margin = this.margin;
        copy.offsetX = this.offsetX;
        copy.offsetY = this.offsetY;
        copy.sizeLevel = this.sizeLevel;
        return copy;
    }

    public static void init(@Nonnull PluginBase plugin) {
        if (componentType != null) {
            throw new IllegalStateException("AlmanacPlayerData component has already been initialized!");
        }
        componentType = plugin.getEntityStoreRegistry().registerComponent(AlmanacPlayerData.class, "Almanac", CODEC);
    }

    @Nonnull
    @SuppressWarnings("null") // ensureAndGetComponent(...) never returns null; componentType is set by init() before setup() registers anything that could call this
    public static AlmanacPlayerData getSaveData(@Nonnull ComponentAccessor<EntityStore> accessor, @Nonnull Ref<EntityStore> ref) {
        return accessor.ensureAndGetComponent(ref, componentType);
    }
}
