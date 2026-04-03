package com.jukeboxflataudio;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class JukeboxFlatAudioMod implements ClientModInitializer {

    public static final String MOD_ID = "jukeboxflataudio";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean flatJukeboxAudio = true;

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("jukebox-flat-audio.properties");

    @Override
    public void onInitializeClient() {
        loadConfig();
        LOGGER.info("Jukebox Flat Audio loaded. Flat jukebox audio: {}", flatJukeboxAudio);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof SoundOptionsScreen) {
                ButtonWidget button = ButtonWidget.builder(
                    getToggleText(),
                    btn -> {
                        flatJukeboxAudio = !flatJukeboxAudio;
                        saveConfig();
                        btn.setMessage(getToggleText());
                    }
                ).dimensions(scaledWidth / 2 - 100, scaledHeight - 48, 200, 20).build();

                Screens.getButtons(screen).add(button);
            }
        });
    }

    private static Text getToggleText() {
        return Text.literal("Flat Jukebox Audio: " + (flatJukeboxAudio ? "ON" : "OFF"));
    }

    public static void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            Properties props = new Properties();
            try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
                props.load(reader);
                flatJukeboxAudio = Boolean.parseBoolean(props.getProperty("flatJukeboxAudio", "true"));
            } catch (IOException e) {
                LOGGER.error("Failed to load config", e);
            }
        }
    }

    public static void saveConfig() {
        Properties props = new Properties();
        props.setProperty("flatJukeboxAudio", String.valueOf(flatJukeboxAudio));
        try (var writer = Files.newBufferedWriter(CONFIG_PATH)) {
            props.store(writer, "Jukebox Flat Audio Config");
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}
