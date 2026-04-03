package com.jukeboxflataudio;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JukeboxFlatAudioMod implements ClientModInitializer {
    public static final String MOD_ID = "jukeboxflataudio";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Jukebox Flat Audio loaded.");
    }
}
