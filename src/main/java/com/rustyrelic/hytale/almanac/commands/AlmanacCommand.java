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
        addSubCommand(new AllToggleCommand("all", "almanac.all.desc"));
        addSubCommand(new CoordsToggleCommand("coords", "almanac.coords.desc"));
        addSubCommand(new BiomeToggleCommand("biome", "almanac.biome.desc"));
        addSubCommand(new TimeToggleCommand("time", "almanac.time.desc"));
        addSubCommand(new AlmanacPositionCommand("position", "almanac.position.desc"));
        addSubCommand(new AlmanacSizeCommand("size", "almanac.size.desc"));
    }
}
