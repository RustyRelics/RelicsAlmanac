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
import com.rustyrelic.hytale.almanac.components.HudCorner;
import com.rustyrelic.hytale.almanac.hud.AlmanacHud;

import javax.annotation.Nonnull;
import java.util.Locale;

@SuppressWarnings("this-escape") // withRequiredArg as a field initializer is the engine's own required pattern (see e.g. WorldMapViewRadiusSetCommand); this class is a concrete leaf, never subclassed
public class AlmanacPositionCommand extends AbstractPlayerCommand {

    @Nonnull
    private final RequiredArg<HudCorner> cornerArg = withRequiredArg("corner", "Which screen corner to anchor to", ArgTypes.forEnum("corner", HudCorner.class));

    public AlmanacPositionCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
        requireNoPermission();
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        HudCorner corner = cornerArg.get(context);
        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        data.setCorner(corner);

        AlmanacHud.sync(player, playerRef, data);

        context.sendMessage(Message.raw("Almanac HUD anchored to " + corner.name().toLowerCase(Locale.ROOT).replace('_', ' ')));
    }
}
