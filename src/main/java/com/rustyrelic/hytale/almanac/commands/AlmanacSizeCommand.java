package com.rustyrelic.hytale.almanac.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;
import com.rustyrelic.hytale.almanac.hud.AlmanacHud;

import javax.annotation.Nonnull;

@SuppressWarnings("this-escape") // withRequiredArg as a field initializer is the engine's own required pattern (see e.g. WorldMapViewRadiusSetCommand); this class is a concrete leaf, never subclassed
public class AlmanacSizeCommand extends AbstractPlayerCommand {

    private static final int MIN = 1;
    private static final int MAX = 9;

    @Nonnull
    private final RequiredArg<Integer> sizeArg = withRequiredArg("level", "Overall HUD scale, 1-9 (3 is the default)", ArgTypes.INTEGER);

    public AlmanacSizeCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
        requireNoPermission();
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        int level = sizeArg.get(context);
        if (level < MIN || level > MAX) {
            context.sendMessage(Message.raw("Size must be between " + MIN + " and " + MAX + "."));
            return;
        }

        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        data.setSizeLevel(level);

        AlmanacHud.sync(player, playerRef, data);

        context.sendMessage(Message.raw("Almanac HUD size set to " + level));
    }
}
