package com.rustyrelic.hytale.almanac.commands;

import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;

import javax.annotation.Nonnull;

public class TimeToggleCommand extends AlmanacToggleCommand {

    public TimeToggleCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
    }

    @Nonnull
    @Override
    protected String onMessageKey() {
        return "almanac.time.on";
    }

    @Nonnull
    @Override
    protected String offMessageKey() {
        return "almanac.time.off";
    }

    @Override
    protected boolean toggle(@Nonnull AlmanacPlayerData data) {
        boolean enabled = !data.isShowTime();
        data.setShowTime(enabled);
        return enabled;
    }
}
