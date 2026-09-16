package com.rustyrelic.hytale.almanac.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

import javax.annotation.Nonnull;

/**
 * {@code /almanac} with no arguments prints usage for free — that's
 * {@link AbstractCommandCollection}'s built-in {@code executeAsync} behaviour.
 */
@SuppressWarnings("this-escape") // addAliases/addSubCommand from the constructor is the engine's own required pattern (see e.g. WorldMapCommand); this class is a concrete leaf, never subclassed
public class AlmanacCommand extends AbstractCommandCollection {

    public AlmanacCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
        requireNoPermission();
        addAliases("ra");
        addSubCommand(new AllToggleCommand("all", "Toggle the whole display on or off"));
        addSubCommand(new CoordsToggleCommand("coords", "Toggle the coordinates row"));
        addSubCommand(new BiomeToggleCommand("biome", "Toggle the biome row"));
        addSubCommand(new TimeToggleCommand("time", "Toggle the time-of-day row"));
        addSubCommand(new AlmanacPositionCommand("position", "Anchor the HUD to a screen corner"));
        addSubCommand(new AlmanacSizeCommand("size", "Set the overall HUD scale"));
    }
}
