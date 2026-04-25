package dev.kwlew.kwelcome.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public boolean isJoinMessageEnabled() {
        return config.getBoolean("enable-join-message");
    }

    public boolean isQuitMessageEnabled() {
        return config.getBoolean("enable-quit-message");
    }

    public boolean ifFirstJoinMessageIsEnabled() {
        return config.getBoolean("enable-firstjoin-message");
    }
}

