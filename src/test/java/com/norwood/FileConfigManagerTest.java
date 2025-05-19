package com.norwood;

import junit.framework.TestCase;

import com.norwood.core.FileConfigManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class FileConfigManagerTest extends TestCase {
    public void testConfigLoadsFromClasspath() {
        FileConfigManager manager = new FileConfigManager();
        String value = manager.get("beanRegistryClass");
        assertEquals("com.norwood.userland.UserBeanRegistry", value);
    }

    public void testConfigLoadsFromJar() throws Exception {
        // Create a temporary jar containing the properties file
        Path tempJar = Files.createTempFile("config", ".jar");
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempJar.toFile()))) {
            jos.putNextEntry(new JarEntry("application.properties"));
            Path src = Paths.get("src/main/resources/application.properties");
            Files.copy(src, jos);
            jos.closeEntry();
        }

        // Determine location of compiled classes
        URL classes = FileConfigManager.class.getProtectionDomain().getCodeSource().getLocation();
        URL[] cp = new URL[] { classes, tempJar.toUri().toURL() };
        try (URLClassLoader loader = new URLClassLoader(cp)) {
            Class<?> clazz = Class.forName("com.norwood.core.FileConfigManager", true, loader);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            String value = (String) clazz.getMethod("get", String.class).invoke(instance, "beanRegistryClass");
            assertEquals("com.norwood.userland.UserBeanRegistry", value);
        }

        Files.deleteIfExists(tempJar);
    }
}
