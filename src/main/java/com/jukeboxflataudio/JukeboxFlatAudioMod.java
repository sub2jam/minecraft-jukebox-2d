package com.jukeboxflataudio;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class JukeboxFlatAudioMod implements ClientModInitializer {

    public static final String MOD_ID = "jukeboxflataudio";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // The setting: true = jukebox music plays flat (no 3D), false = normal 3D audio
    public static boolean flatJukeboxAudio = true;

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("jukebox-flat-audio.properties");

    @Override
    public void onInitializeClient() {
        loadConfig();
        LOGGER.info("Jukebox Flat Audio loaded. Flat jukebox audio: {}", flatJukeboxAudio);
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
