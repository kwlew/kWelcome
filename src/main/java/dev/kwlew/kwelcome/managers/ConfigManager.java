package dev.kwlew.kwelcome.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ConfigManager {

    private static final String DEFAULT_LAST_LOGIN_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_LAST_LOGIN_ZONE = "system";

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private DateTimeFormatter lastLoginFormatter;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        rebuildFormatters();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        rebuildFormatters();
    }

    public boolean isJoinMessageEnabled() {
        return config.getBoolean("enable-join-message");
    }

    public boolean isQuitMessageEnabled() {
        return config.getBoolean("enable-quit-message");
    }

    public boolean isFirstJoinMessageEnabled() {
        return config.getBoolean("enable-firstjoin-message");
    }

    public DateTimeFormatter getLastLoginFormatter() {
        return lastLoginFormatter;
    }

    private void rebuildFormatters() {
        String configuredPattern = config.getString("last-login.format", DEFAULT_LAST_LOGIN_FORMAT);
        DateTimeFormatter formatter = buildFormatter(configuredPattern);
        ZoneId zoneId = resolveZone(config.getString("last-login.zone", DEFAULT_LAST_LOGIN_ZONE));
        this.lastLoginFormatter = formatter.withZone(zoneId);
    }

    private DateTimeFormatter buildFormatter(String configuredPattern) {
        try {
            return DateTimeFormatter.ofPattern(configuredPattern);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid last-login.format in config.yml. Using default: " + DEFAULT_LAST_LOGIN_FORMAT);
            return DateTimeFormatter.ofPattern(DEFAULT_LAST_LOGIN_FORMAT);
        }
    }

    private ZoneId resolveZone(String configuredZone) {
        if (configuredZone == null || configuredZone.equalsIgnoreCase(DEFAULT_LAST_LOGIN_ZONE)) {
            return ZoneId.systemDefault();
        }

        try {
            return ZoneId.of(configuredZone);
        } catch (DateTimeException e) {
            plugin.getLogger().warning("Invalid last-login.zone in config.yml. Using system timezone.");
            return ZoneId.systemDefault();
        }
    }
}
