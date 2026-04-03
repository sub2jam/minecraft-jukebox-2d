package com.jukeboxflataudio.mixin;

import com.jukeboxflataudio.JukeboxFlatAudioMod;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin extends Screen {

    protected SoundOptionsScreenMixin() {
        super(Text.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void jukeboxFlatAudio_addToggle(CallbackInfo ci) {
        int buttonWidth = 200;
        int buttonX = this.width / 2 - buttonWidth / 2;
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
        return Text.literal("Flat Jukebox Audio: " + (JukeboxFlatAudioMod.flatJukeboxAudio ? "ON" : "OFF"));
    }
}
