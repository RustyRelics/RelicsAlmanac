package com.rustyrelic.hytale.almanac.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.rustyrelic.hytale.almanac.commands.PositionTargetArgumentType.Target;
import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;
import com.rustyrelic.hytale.almanac.components.HudCorner;
import com.rustyrelic.hytale.almanac.hud.AlmanacHud;

import javax.annotation.Nonnull;

/**
 * {@code /almanac position <corner|reset>}.
 * <ul>
 *   <li>{@code <corner>} anchors to that corner <b>and zeroes the nudge offsets</b>, so a player who
 *       nudged the HUD off-screen can recover by picking any corner without knowing {@code reset}.</li>
 *   <li>{@code reset} zeroes the offsets and leaves the corner alone.</li>
 * </ul>
 * Neither depends on the current offset state, so both always work.
 */
@SuppressWarnings("this-escape") // withRequiredArg as a field initializer is the engine's own required pattern (see e.g. WorldMapViewRadiusSetCommand); this class is a concrete leaf, never subclassed
public class AlmanacPositionCommand extends AbstractPlayerCommand {

    @Nonnull
    private final RequiredArg<Target> targetArg = withRequiredArg("corner", "almanac.position.arg.corner.desc", PositionTargetArgumentType.INSTANCE);

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

        Target target = targetArg.get(context);
        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        data.resetOffsets();

        HudCorner corner = target.corner();
        if (corner == null) {
            AlmanacHud.sync(player, playerRef, data);
            context.sendMessage(Message.translation("almanac.position.reset"));
            return;
        }

        data.setCorner(corner);
        AlmanacHud.sync(player, playerRef, data);
        context.sendMessage(Message.translation(anchoredMessageKey(corner)));
    }

    /**
     * One fully-baked sentence per corner rather than interpolating a corner name into a
     * shared template — same "don't glue translated fragments together" reasoning as the
     * toggle commands' on/off keys.
     */
    @Nonnull
    private static String anchoredMessageKey(@Nonnull HudCorner corner) {
        return switch (corner) {
            case TOP_LEFT -> "almanac.position.anchored.topLeft";
            case TOP_RIGHT -> "almanac.position.anchored.topRight";
            case BOTTOM_LEFT -> "almanac.position.anchored.bottomLeft";
            case BOTTOM_RIGHT -> "almanac.position.anchored.bottomRight";
        };
    }
}
