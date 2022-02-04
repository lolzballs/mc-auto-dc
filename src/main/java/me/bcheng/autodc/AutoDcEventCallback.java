package me.bcheng.autodc;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface AutoDcEventCallback {
    Event<AutoDcEventCallback> EVENT = EventFactory.createArrayBacked(AutoDcEventCallback.class,
            (listeners) -> (reason) -> {
                for (var listener : listeners) {
                    listener.handle(reason);
                }
            });

    void handle(AutoDcEventSource reason);
}
