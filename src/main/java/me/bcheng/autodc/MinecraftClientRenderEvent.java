package me.bcheng.autodc;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface MinecraftClientRenderEvent {
    Event<MinecraftClientRenderEvent> EVENT = EventFactory.createArrayBacked(MinecraftClientRenderEvent.class,
            (listeners) -> () -> {
                for (var listener : listeners) {
                    listener.handle();
                }
            });

    void handle();
}
