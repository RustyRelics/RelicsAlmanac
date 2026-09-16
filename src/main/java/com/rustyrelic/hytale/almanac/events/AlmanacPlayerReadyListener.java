package com.rustyrelic.hytale.almanac.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.rustyrelic.hytale.almanac.components.AlmanacPlayerData;
import com.rustyrelic.hytale.almanac.hud.AlmanacHud;

/**
 * Persistence survives a rejoin on its own, but the HUD itself doesn't — it has to be
 * re-attached explicitly, or a player who left with it on comes back to nothing until
 * their next toggle command happens to rebuild it.
 */
public class AlmanacPlayerReadyListener {

    public static void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        if (!ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }

        AlmanacPlayerData data = AlmanacPlayerData.getSaveData(store, ref);
        AlmanacHud.sync(event.getPlayer(), playerRef, data);
    }
}
