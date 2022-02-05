package me.bcheng.autodc;

import me.bcheng.autodc.mixin.MinecraftClientMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDcMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("autodc");

    private final Text hudText = new LiteralText("AFK Protection On");
    private ClickableWidget afkButton;
    private boolean afk = false;
    private AutoDcEventSource disconnectReason = null;

    @Override
    public void onInitializeClient() {
        this.afkButton = createAfkButton();

        AutoDcEventCallback.EVENT.register(this::onDcEvent);
        MinecraftClientRenderEvent.EVENT.register(this::disconnect);
        ScreenEvents.AFTER_INIT.register(this::afterScreenInit);
        HudRenderCallback.EVENT.register(this::renderHudOverlay);
    }

    private void disconnect() {
        if (this.disconnectReason == null)
            return;

        var client = MinecraftClient.getInstance();
        if (client.world == null)
            return;

        Screen returnScreen = new TitleScreen();
        boolean sp = client.isInSingleplayer();
        boolean connectedToRealms = client.isConnectedToRealms();
        client.world.disconnect();
        if (sp) {
            client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        } else {
            client.disconnect();
        }
        if (connectedToRealms) {
            returnScreen = new RealmsMainScreen(returnScreen);
        } else if (!sp) {
            returnScreen = new MultiplayerScreen(returnScreen);
        }

        client.setScreen(new AutoDcDisconnectedScreen(returnScreen, this.disconnectReason));

        this.disconnectReason = null;
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
            this.disconnectReason = reason;
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
