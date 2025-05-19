package com.norwood.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileConfigManager implements ConfigManager {
    private final String configFile = "application.properties";
    private boolean cached = false;
    private Map<String, String> configMap = new HashMap<>();

    @Override
    public void cacheConfig() {
        try (InputStream stream = FileConfigManager.class
                .getClassLoader()
                .getResourceAsStream(configFile)) {
            if (stream == null) {
                throw new RuntimeException("No config file found at: " + configFile);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                configMap = reader.lines()
                    .collect(Collectors.toMap(
                        line -> line.split("=")[0],
                        line -> line.split("=")[1]
                    ));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + configFile, e);
        }
    }

    @Override
    public String get(String key) {
        if (!cached) {
            cacheConfig();
            cached = true;
        }
        return configMap.get(key);
    }
}

