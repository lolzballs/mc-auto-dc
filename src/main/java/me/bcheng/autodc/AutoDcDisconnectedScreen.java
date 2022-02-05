package me.bcheng.autodc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class AutoDcDisconnectedScreen extends Screen {
    private final AutoDcEventSource reason;
    private final Screen parent;
    private MultilineText reasonFormatted = MultilineText.EMPTY;
    private int reasonHeight;

    public AutoDcDisconnectedScreen(Screen parent, AutoDcEventSource reason) {
        super(new LiteralText("AFK Protection"));
        this.parent = parent;
        this.reason = reason;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        this.reasonFormatted = MultilineText.create(this.textRenderer, this.getTextForDcReason(this.reason), this.width - 50);
        this.reasonHeight = this.reasonFormatted.count() * this.textRenderer.fontHeight;

        var button = new ButtonWidget(this.width / 2 - 100,
                Math.min(this.height / 2 + this.reasonHeight / 2 + this.textRenderer.fontHeight, this.height - 30),
                200, 20,
                new LiteralText("Go back"),
                unused -> this.client.setScreen(this.parent));
        this.addDrawableChild(button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.height / 2 - this.reasonHeight / 2 - this.textRenderer.fontHeight * 2, 0xAAAAAA);
        this.reasonFormatted.drawCenterWithShadow(matrices, this.width / 2, this.height / 2 - this.reasonHeight / 2);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private Text getTextForDcReason(AutoDcEventSource reason) {
        return switch (reason) {
            case DAMAGE -> new LiteralText("You were damaged.");
            case MOVEMENT -> new LiteralText("You were moved.");
        };
    }
}

