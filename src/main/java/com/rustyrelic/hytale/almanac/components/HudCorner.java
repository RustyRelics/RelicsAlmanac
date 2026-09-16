package com.rustyrelic.hytale.almanac.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

public enum HudCorner {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT;

    public static final Codec<HudCorner> CODEC = new EnumCodec<>(HudCorner.class);
}
