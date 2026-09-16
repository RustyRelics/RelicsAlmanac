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
 * Master on/off switch. A plain toggle when at least one row is already enabled — off hides
 * the display without touching the individual row toggles, on brings back whatever was
 * showing before. Run with nothing enabled at all (fresh install, or everything toggled off
 * individually), it means "give me everything" instead: all three rows turn on so there's
 * something to look at, rather than silently flipping a flag nothing currently depends on.
 * <p>
 * Kept separate from {@link AlmanacToggleCommand} rather than reusing its generic
 * "label + display is now on/off" message — this command's two branches need genuinely
 * different feedback, not a label substituted into a fixed template.
 */
@SuppressWarnings("this-escape") // requireNoPermission() from the constructor is the engine's own required pattern; this class is a concrete leaf, never subclassed
public class AllToggleCommand extends AbstractPlayerCommand {

    public AllToggleCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
        requireNoPermission();
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        boolean anyRowEnabled = data.isShowCoords() || data.isShowBiome() || data.isShowTime();

        if (!anyRowEnabled) {
            data.setShowCoords(true);
            data.setShowBiome(true);
            data.setShowTime(true);
            data.setAllEnabled(true);
            AlmanacHud.sync(player, playerRef, data);
            context.sendMessage(Message.raw("Almanac display is now on — coordinates, biome and time all enabled"));
            return;
        }

        boolean enabled = !data.isAllEnabled();
        data.setAllEnabled(enabled);
        AlmanacHud.sync(player, playerRef, data);
        context.sendMessage(Message.raw("Almanac display is now " + (enabled ? "on" : "off")));
    }
}
