package com.rustyrelic.hytale.almanac.commands;

import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;

import javax.annotation.Nonnull;

public class CoordsToggleCommand extends AlmanacToggleCommand {

    public CoordsToggleCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
    }

    @Nonnull
    @Override
    protected String label() {
        return "Coordinates";
    }

    @Override
    protected boolean toggle(@Nonnull AlmanacPlayerData data) {
        boolean enabled = !data.isShowCoords();
        data.setShowCoords(enabled);
        return enabled;
    }
}
