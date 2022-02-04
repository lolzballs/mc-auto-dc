package me.bcheng.autodc.mixin;

import me.bcheng.autodc.AutoDcEventCallback;
import me.bcheng.autodc.AutoDcEventSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
    private void onEntityMove(MovementType movementType, Vec3d vec, CallbackInfo info) {
        if (movementType != MovementType.SELF) {
            AutoDcEventCallback.EVENT.invoker().handle(AutoDcEventSource.MOVEMENT);
        }
    }

    @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    private void onEntityDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        var player = (ClientPlayerEntity) ((Object) this);
        if (!player.isInvulnerableTo(source)) {
            AutoDcEventCallback.EVENT.invoker().handle(AutoDcEventSource.DAMAGE);
        }
    }
}
