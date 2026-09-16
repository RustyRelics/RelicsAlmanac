package com.rustyrelic.hytale.almanac;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.rustyrelic.hytale.almanac.commands.AlmanacCommand;
import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;
import com.rustyrelic.hytale.almanac.events.AlmanacPlayerReadyListener;
import com.rustyrelic.hytale.almanac.systems.AlmanacTickSystem;

import javax.annotation.Nonnull;

public class AlmanacPlugin extends JavaPlugin {

    public AlmanacPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        AlmanacPlayerData.init(this);
        this.getEntityStoreRegistry().registerSystem(new AlmanacTickSystem());
        this.getCommandRegistry().registerCommand(new AlmanacCommand("almanac", "Coordinates, clock and biome HUD"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, AlmanacPlayerReadyListener::onPlayerReady);
    }
}
