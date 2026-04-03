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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts every sound played through SoundManager.
 * If the sound is in the RECORD category and flatJukeboxAudio is on,
 * we cancel the original (3D positioned) call and re-play it as a
 * non-attenuated (flat / 2D) sound instance.
 */
@Mixin(SoundManager.class)
public class JukeboxBlockEntityMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void jukeboxFlatAudio_interceptPlay(SoundInstance sound, CallbackInfo ci) {
        // Only act when the toggle is on
        if (!JukeboxFlatAudioMod.flatJukeboxAudio) return;

        // Jukebox / disc sounds always use the RECORD category
        if (sound.getCategory() != SoundCategory.RECORDS) return;

        // Guard: if it's already non-attenuated we already replaced it — skip to avoid a loop
        if (sound.getAttenuationType() == SoundInstance.AttenuationType.NONE) return;

        // Cancel the 3D call
        ci.cancel();

        // Re-issue as a flat (non-positional) sound at the same volume/pitch
        SoundInstance flat = new PositionedSoundInstance(
                sound.getId(),
                SoundCategory.RECORDS,
                sound.getVolume(),
                sound.getPitch(),
                Random.create(),
                false,                               // not looping
                0,                                   // no delay
                SoundInstance.AttenuationType.NONE,  // KEY: no distance fade
                0, 0, 0,                             // position doesn't matter for NONE
                true                                 // relative to listener — stays 2D
        );

        ((SoundManager) (Object) this).play(flat);
    }
}
