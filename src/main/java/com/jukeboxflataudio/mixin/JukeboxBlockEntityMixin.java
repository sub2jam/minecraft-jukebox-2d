package com.jukeboxflataudio.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public class JukeboxBlockEntityMixin {

    @Shadow protected double x;
    @Shadow protected double y;
    @Shadow protected double z;

    @Unique
    private double jukeboxFlat$getDist() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return 0;
        double dx = this.x - client.player.getX();
        double dy = this.y - client.player.getY();
        double dz = this.z - client.player.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Inject(method = "getX", at = @At("RETURN"), cancellable = true)
    private void noJukeboxPanX(CallbackInfoReturnable<Double> ci) {
        AbstractSoundInstance self = (AbstractSoundInstance)(Object)this;
        if (self.getCategory() != SoundCategory.RECORDS) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        double dist = jukeboxFlat$getDist();
        if (dist == 0) return;
        float yaw = client.player.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        ci.setReturnValue(client.player.getX() - MathHelper.sin(yaw) * dist);
    }

    @Inject(method = "getZ", at = @At("RETURN"), cancellable = true)
    private void noJukeboxPanZ(CallbackInfoReturnable<Double> ci) {
        AbstractSoundInstance self = (AbstractSoundInstance)(Object)this;
        if (self.getCategory() != SoundCategory.RECORDS) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        double dist = jukeboxFlat$getDist();
        if (dist == 0) return;
        float yaw = client.player.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        ci.setReturnValue(client.player.getZ() + MathHelper.cos(yaw) * dist);
    }
}
