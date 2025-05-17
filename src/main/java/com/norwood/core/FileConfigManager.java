package com.norwood.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileConfigManager implements ConfigManager {
    private final String configFile = "src/main/resources/application.properties";
    private boolean cached = false;
    private Map<String, String> configMap = new HashMap<>();

    @Override
    public void cacheConfig() {
        configMap = readConfigLines()
            .collect(Collectors.toMap(
                line -> line.split("=")[0],
                line -> line.split("=")[1]
            ));
    }

    private Stream<String> readConfigLines() {
        try {
            return Files.lines(Path.of(configFile));
        } catch (IOException e) {
            throw new RuntimeException("No config file found at: " + configFile);
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

