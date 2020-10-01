package me.leafs.timechanger.handler.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;

@RequiredArgsConstructor
public class ConfigHandler {
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Getter private final File configPath;

    public TimeConfiguration readConfig() {
        TimeConfiguration config = new TimeConfiguration();

        // make sure the config exists
        if (!configPath.exists()) {
            // write and return a new one
            populateConfig(config);
            return config;
        }

        // read the file and parse json
        try {
            InputStream in = new FileInputStream(configPath);
            config = GSON.fromJson(new InputStreamReader(in), TimeConfiguration.class);
        } catch (FileNotFoundException ignored) { // we literally check but ok
        }

        return config;
    }

    public void populateConfig(TimeConfiguration config) {
        String jsonString = GSON.toJson(config);

        try {
            Files.write(configPath.toPath(), jsonString.getBytes());
        } catch (IOException e) {
            System.out.println("Failed to write config file.");
        }
    }
}
