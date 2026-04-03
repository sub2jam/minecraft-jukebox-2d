package com.jukeboxflataudio.mixin;

import com.jukeboxflataudio.JukeboxFlatAudioMod;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public class JukeboxBlockEntityMixin {

    @Inject(method = "getAttenuationType", at = @At("HEAD"), cancellable = true)
    private void jukeboxFlatAudio_flattenJukebox(CallbackInfoReturnable<SoundInstance.AttenuationType> ci) {
        AbstractSoundInstance self = (AbstractSoundInstance)(Object)this;
        if (!JukeboxFlatAudioMod.flatJukeboxAudio) return;
        if (self.getCategory() != SoundCategory.RECORDS) return;
        ci.setReturnValue(SoundInstance.AttenuationType.NONE);
    }
}
