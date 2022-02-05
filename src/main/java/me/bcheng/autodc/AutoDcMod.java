package me.bcheng.autodc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDcMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("autodc");

    private final Text hudText = new LiteralText("AFK Protection On");
    private ClickableWidget afkButton;
    private boolean afk;

    @Override
    public void onInitializeClient() {
        this.afkButton = createAfkButton();

        AutoDcEventCallback.EVENT.register(this::onDcEvent);
        ScreenEvents.AFTER_INIT.register(this::afterScreenInit);
        HudRenderCallback.EVENT.register(this::renderHudOverlay);
    }

    private void renderHudOverlay(MatrixStack matrixStack, float tickDelta) {
        var client = MinecraftClient.getInstance();

        // Don't render if debug menu (F3) is open, or if AFK protection is off
        if (!this.afk || client.options.debugEnabled)
            return;

        var textRenderer = client.textRenderer;
        textRenderer.draw(matrixStack, this.hudText, 0, 0, 0xFFFFFFFF);
    }

    private void onDcEvent(AutoDcEventSource reason) {
        if (this.afk) {
            LOGGER.info("DcEvent reason {}", reason);
            // TODO: disconnect player here
        }
    }

    private void afterScreenInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        if (screen instanceof GameMenuScreen) {
            Screens.getButtons(screen).add(this.afkButton);
        }
    }

    private ClickableWidget createAfkButton() {
        return CyclingButtonWidget.onOffBuilder(new LiteralText("On"), new LiteralText("Off"))
                .initially(false)
                .build(0, 0, 150, 20,
                        new LiteralText("AFK Protection"),
                        (widget, value) -> this.afk = value);
    }
}
