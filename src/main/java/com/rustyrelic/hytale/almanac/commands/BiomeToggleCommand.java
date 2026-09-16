package com.rustyrelic.hytale.almanac.commands;

import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;

import javax.annotation.Nonnull;

public class BiomeToggleCommand extends AlmanacToggleCommand {

    public BiomeToggleCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
    }

    @Nonnull
    @Override
    protected String label() {
        return "Biome";
    }

    @Override
    protected boolean toggle(@Nonnull AlmanacPlayerData data) {
        boolean enabled = !data.isShowBiome();
        data.setShowBiome(enabled);
        return enabled;
    }
}
