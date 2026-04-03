package com.jukeboxflataudio.mixin;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public class JukeboxBlockEntityMixin {

    @Inject(method = "isRelative", at = @At("RETURN"), cancellable = true)
    private void flattenJukeboxPanning(CallbackInfoReturnable<Boolean> ci) {
        AbstractSoundInstance self = (AbstractSoundInstance)(Object)this;
        if (self.getCategory() == SoundCategory.RECORDS) {
            ci.setReturnValue(true);
        }
    }
}
