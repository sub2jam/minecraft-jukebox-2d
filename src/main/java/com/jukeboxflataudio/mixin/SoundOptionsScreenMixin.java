package com.jukeboxflataudio.mixin;

import com.jukeboxflataudio.JukeboxFlatAudioMod;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds a "Flat Jukebox Audio: ON/OFF" toggle button to the Sound Options screen,
 * sitting just below the existing volume sliders.
 */
@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin extends Screen {

    protected SoundOptionsScreenMixin() {
        super(Text.empty());
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void jukeboxFlatAudio_addToggle(CallbackInfo ci) {
        SoundOptionsScreen self = (SoundOptionsScreen) (Object) this;

        // Place the button at the bottom of the option list area, centred
        int buttonWidth = 200;
        int buttonX = this.width / 2 - buttonWidth / 2;
        // Position it near the bottom of the screen, above the Done button
        int buttonY = this.height - 48;

        this.addDrawableChild(
            ButtonWidget.builder(
                getToggleText(),
                button -> {
                    JukeboxFlatAudioMod.flatJukeboxAudio = !JukeboxFlatAudioMod.flatJukeboxAudio;
                    JukeboxFlatAudioMod.saveConfig();
                    button.setMessage(getToggleText());
                }
            )
            .dimensions(buttonX, buttonY, buttonWidth, 20)
            .build()
        );
    }

    private static Text getToggleText() {
        String state = JukeboxFlatAudioMod.flatJukeboxAudio ? "ON" : "OFF";
        return Text.literal("Flat Jukebox Audio: " + state);
    }
}
