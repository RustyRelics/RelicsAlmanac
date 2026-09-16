package com.rustyrelic.hytale.almanac.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;
import com.rustyrelic.hytale.almanac.hud.AlmanacHud;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.time.format.DateTimeFormatter;

public class AlmanacTickSystem extends DelayedEntitySystem<EntityStore> {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public AlmanacTickSystem() {
        super(0.5f);
    }

    @Override
    @SuppressWarnings("null") // getComponentType()/getResourceType() never return null
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Player player = archetypeChunk.getComponent(index, Player.getComponentType());
        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (player == null || transform == null) {
            return;
        }

        HudManager hudManager = player.getHudManager();
        CustomUIHud hud = hudManager.getCustomHud(AlmanacHud.KEY);
        if (!(hud instanceof AlmanacHud almanacHud)) {
            return;
        }

        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(commandBuffer, archetypeChunk.getReferenceTo(index));

        Vector3d pos = data.isShowCoords() ? transform.getPosition() : null;

        String biomeName = null;
        if (data.isShowBiome()) {
            biomeName = resolveBiomeName(player);
        }

        String timeText = null;
        if (data.isShowTime()) {
            WorldTimeResource timeResource = store.getResource(WorldTimeResource.getResourceType());
            if (timeResource != null) {
                timeText = timeResource.getGameDateTime().format(TIME_FORMAT);
            }
        }

        almanacHud.applyConfig(data);
        almanacHud.updateContent(pos, biomeName, timeText);
        almanacHud.show();
    }

    @Nonnull
    private static String resolveBiomeName(@Nonnull Player player) {
        String biomeName = player.getWorldMapTracker().getCurrentBiomeName();
        if (biomeName == null) {
            return "Unknown";
        }
        return biomeName.replace('_', ' ');
    }

    @Nonnull
    @Override
    @SuppressWarnings("null") // getComponentType() never returns null
    public Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType(), TransformComponent.getComponentType());
    }
}
