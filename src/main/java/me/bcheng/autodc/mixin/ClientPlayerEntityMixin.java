package me.bcheng.autodc.mixin;

import me.bcheng.autodc.AutoDcEventCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
	private void onEntityMove(MovementType movementType, Vec3d vec, CallbackInfo info) {
		if (movementType != MovementType.SELF) {
			AutoDcEventCallback.EVENT.invoker().handle("onEntityMove");
		}
	}
}
