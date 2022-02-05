package me.bcheng.autodc.mixin;

import me.bcheng.autodc.MinecraftClientRenderEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "render(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;tick()V"))
    private void renderInvokeTick(CallbackInfo info) {
        MinecraftClientRenderEvent.EVENT.invoker().handle();
    }
}
