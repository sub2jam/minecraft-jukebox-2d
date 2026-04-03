package com.jukeboxflataudio.mixin;

import com.jukeboxflataudio.JukeboxFlatAudioMod;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundManager.class)
public class JukeboxBlockEntityMixin {

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void jukeboxFlatAudio_interceptPlay(SoundInstance sound, CallbackInfoReturnable<?> ci) {
        if (!JukeboxFlatAudioMod.flatJukeboxAudio) return;
        if (sound.getCategory() != SoundCategory.RECORDS) return;
        if (sound.getAttenuationType() == SoundInstance.AttenuationType.NONE) return;

        ci.cancel();

        SoundInstance flat = new PositionedSoundInstance(
                sound.getId(),
                SoundCategory.RECORDS,
                sound.getVolume(),
                sound.getPitch(),
                Random.create(),
                false,
                0,
                SoundInstance.AttenuationType.NONE,
                0, 0, 0,
                true
        );

        ((SoundManager) (Object) this).play(flat);
    }
}
