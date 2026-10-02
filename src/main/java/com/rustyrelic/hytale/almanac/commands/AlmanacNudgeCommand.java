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

/**
 * {@code /almanac nudge <direction> [px]} — moves the HUD a few pixels on screen, relative to
 * wherever it currently is. Meant to be typed repeatedly while eyeballing the result, so the pixel
 * count is optional (default {@value #DEFAULT_STEP}).
 * <p>
 * The optional count is a <em>usage variant</em>, not an {@code OptionalArg}: optional args in this
 * command system are {@code --flag=value} style, which is the opposite of comfortable for a command
 * typed over and over. Variants are picked by required-argument count, the same pattern the
 * builtin {@code /move <direction> <distance>} uses.
 * <p>
 * A single nudge is capped at {@value #MAX_STEP}px. The server cannot know the client's screen
 * size, so it cannot clamp the accumulated offset; capping each step keeps any one command from
 * flinging the HUD somewhere unrecoverable. {@code /almanac position <corner>} and
 * {@code /almanac position reset} both zero the offsets and work from any state.
 */
@SuppressWarnings("this-escape") // addUsageVariant/withRequiredArg from the constructor/field initializers is the engine's own required pattern (see e.g. MoveCommand); this class is a concrete leaf, never subclassed
public class AlmanacNudgeCommand extends AbstractPlayerCommand {

    static final int DEFAULT_STEP = 10;
    static final int MIN_STEP = 1;
    static final int MAX_STEP = 50;

    @Nonnull
    private final RequiredArg<NudgeDirection> directionArg = withRequiredArg("direction", "almanac.nudge.arg.direction.desc", ArgTypes.forEnum("direction", NudgeDirection.class));

    public AlmanacNudgeCommand(@Nonnull String name, @Nonnull String description) {
        super(name, description);
        requireNoPermission();
        addUsageVariant(new WithStep(description));
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        nudge(context, store, ref, playerRef, directionArg.get(context), DEFAULT_STEP);
    }

    private static void nudge(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull NudgeDirection direction, int step) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        if (step < MIN_STEP || step > MAX_STEP) {
            context.sendMessage(Message.translation("almanac.nudge.outOfRange").param("min", MIN_STEP).param("max", MAX_STEP));
            return;
        }

        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        data.setOffsetX(data.getOffsetX() + direction.dx() * step);
        data.setOffsetY(data.getOffsetY() + direction.dy() * step);

        AlmanacHud.sync(player, playerRef, data);

        context.sendMessage(Message.translation(movedMessageKey(direction)).param("px", step));
    }

    /** One fully-baked sentence per direction — same "don't glue translated fragments" reasoning as the toggle keys. */
    @Nonnull
    private static String movedMessageKey(@Nonnull NudgeDirection direction) {
        return switch (direction) {
            case LEFT -> "almanac.nudge.moved.left";
            case RIGHT -> "almanac.nudge.moved.right";
            case UP -> "almanac.nudge.moved.up";
            case DOWN -> "almanac.nudge.moved.down";
        };
    }

    /** {@code /almanac nudge <direction> <px>}. Needs its own {@code requireNoPermission()}: a nameless variant inherits the parent's generated (op-only) node otherwise. */
    @SuppressWarnings("this-escape") // withRequiredArg as a field initializer is the engine's own required pattern; private leaf, never subclassed
    private static final class WithStep extends AbstractPlayerCommand {

        @Nonnull
        private final RequiredArg<NudgeDirection> directionArg = withRequiredArg("direction", "almanac.nudge.arg.direction.desc", ArgTypes.forEnum("direction", NudgeDirection.class));

        @Nonnull
        private final RequiredArg<Integer> stepArg = withRequiredArg("px", "almanac.nudge.arg.px.desc", ArgTypes.INTEGER);

        WithStep(@Nonnull String description) {
            super(description);
            requireNoPermission();
        }

        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            nudge(context, store, ref, playerRef, directionArg.get(context), stepArg.get(context));
        }
    }
}
