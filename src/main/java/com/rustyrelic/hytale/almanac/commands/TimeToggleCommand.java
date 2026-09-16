package com.rustyrelic.hytale.almanac.commands;

import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;

import javax.annotation.Nonnull;

public class TimeToggleCommand extends AlmanacToggleCommand {

    public TimeToggleCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
    }

    @Nonnull
    @Override
    protected String label() {
        return "Time of day";
    }

    @Override
    protected boolean toggle(@Nonnull AlmanacPlayerData data) {
        boolean enabled = !data.isShowTime();
        data.setShowTime(enabled);
        return enabled;
    }
}
