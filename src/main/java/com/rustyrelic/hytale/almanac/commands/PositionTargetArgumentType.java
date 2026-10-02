package com.rustyrelic.hytale.almanac.commands;

import com.hypixel.hytale.common.util.StringUtil;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.CommandUtil;
import com.hypixel.hytale.server.core.command.system.ParseResult;
import com.hypixel.hytale.server.core.command.system.arguments.types.SingleArgumentType;
import com.hypixel.hytale.server.core.command.system.suggestion.SuggestionResult;
import com.rustyrelic.hytale.almanac.components.HudCorner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The single argument of {@code /almanac position}: either a {@link HudCorner} name or the word
 * {@code reset}.
 * <p>
 * Why not an enum argument plus a {@code reset} subcommand: {@code AbstractCommand.getUsageShort}
 * prints <em>only</em> the subcommands/variants of a command that has any, so adding a
 * {@code reset} subcommand to {@code position} would make {@code /help} stop showing the
 * {@code <corner>} argument at all. Folding both words into one argument type keeps the usage line
 * and tab-completion complete.
 */
public final class PositionTargetArgumentType extends SingleArgumentType<PositionTargetArgumentType.Target> {

    private static final String RESET_WORD = "reset";

    // Declared before INSTANCE on purpose: static initializers run in textual order, and INSTANCE's
    // constructor reads NAMES.
    private static final List<String> NAMES = names();

    public static final PositionTargetArgumentType INSTANCE = new PositionTargetArgumentType();

    /** A parsed argument: a corner to anchor to, or {@link #RESET} (no corner, zero the nudge offsets). */
    public record Target(@Nullable HudCorner corner) {
        public static final Target RESET = new Target(null);

        public boolean isReset() {
            return corner == null;
        }
    }

    private PositionTargetArgumentType() {
        super("corner", String.join(", ", NAMES), NAMES.toArray(String[]::new));
    }

    @Nonnull
    private static List<String> names() {
        List<String> names = new ArrayList<>();
        for (HudCorner corner : HudCorner.values()) {
            names.add(corner.name().toLowerCase(Locale.ROOT));
        }
        names.add(RESET_WORD);
        return List.copyOf(names);
    }

    @Nullable
    @Override
    public Target parse(@Nonnull String input, @Nonnull ParseResult parseResult) {
        String lower = input.toLowerCase(Locale.ROOT);
        if (lower.equals(RESET_WORD)) {
            return Target.RESET;
        }
        for (HudCorner corner : HudCorner.values()) {
            if (corner.name().toLowerCase(Locale.ROOT).equals(lower)) {
                return new Target(corner);
            }
        }

        // Same failure shape as the engine's own EnumArgumentType.
        parseResult.fail(
                Message.empty()
                        .insert(Message.translation("server.commands.errors.noSuchEnum")
                                .param("type", "HudCorner")
                                .param("name", input))
                        .insert(Message.raw(" "))
                        .insert(Message.translation("server.general.failed.didYouMean")
                                .param("choices", StringUtil.sortByFuzzyDistance(lower, NAMES, CommandUtil.RECOMMEND_COUNT).toString()))
        );
        return null;
    }

    @Override
    public void suggest(@Nonnull CommandSender sender, @Nonnull String textAlreadyEntered, int numParametersTyped, @Nonnull SuggestionResult result) {
        String lowerInput = textAlreadyEntered.toLowerCase(Locale.ROOT);
        for (String name : NAMES) {
            if (name.startsWith(lowerInput)) {
                result.suggest(name);
            }
        }
    }

    @Override
    public int getSuggestionValueCount() {
        return NAMES.size();
    }
}
