package com.rustyrelic.hytale.almanac.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;
import com.rustyrelic.hytale.almanac.hud.AlmanacHud;

import javax.annotation.Nonnull;

/**
 * Shared toggle-a-row behaviour for the coords/biome/time subcommands.
 * Uses {@link AbstractPlayerCommand}, not {@link com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand} —
 * every row here is a self-toggle, never targeting another player, so there's no target
 * argument and no nullable-sender case to guard against. A console sender is already
 * rejected by {@code AbstractPlayerCommand} itself before {@link #execute} ever runs.
 */
abstract class AlmanacToggleCommand extends AbstractPlayerCommand {

    protected AlmanacToggleCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
        // No permission gate by default resolves to false (op-only) — these are self-toggle
        // cosmetic commands with no reason to require anything, op or otherwise.
        requireNoPermission();
    }

    @Nonnull
    protected abstract String label();

    protected abstract boolean toggle(@Nonnull AlmanacPlayerData data);

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        boolean enabled = toggle(data);
        if (enabled) {
            // Turning any row on implies the display should be showing.
            data.setAllEnabled(true);
        }

        AlmanacHud.sync(player, playerRef, data);

        context.sendMessage(Message.raw(label() + " display is now " + (enabled ? "on" : "off")));
    }
}
