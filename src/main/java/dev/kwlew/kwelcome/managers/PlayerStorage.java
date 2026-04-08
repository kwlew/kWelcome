package dev.kwlew.kwelcome.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PlayerStorage {

    private final File file;
    private FileConfiguration config;

    public PlayerStorage(JavaPlugin plugin) {
        // Ensure folder exists
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.file = new File(plugin.getDataFolder(), "players.yml");

        // Create file if missing
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create players.yml", e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);

        // Ensure root section exists
        if (!config.contains("players")) {
            config.createSection("players");
            save();
        }
    }

    private String path(UUID uuid) {
        return "players." + uuid;
    }

    /* =========================
       Core Logic
       ========================= */

    /**
     * Handles a player login.
     * Returns TRUE if this is the first time the player joined.
     */
    public boolean handleLogin(UUID uuid) {
        String path = path(uuid);

        long previousLogin = config.getLong(path + ".last-login", -1);

        boolean firstJoin = (previousLogin == -1);

        // Store values
        config.set(path + ".first-join", firstJoin);
        config.set(path + ".last-login", System.currentTimeMillis());

        save();

        return firstJoin;
    }

    public boolean isFirstJoin(UUID uuid) {
        return config.getBoolean(path(uuid) + ".first-join", true);
    }

    public long getLastLogin(UUID uuid) {
        return config.getLong(path(uuid) + ".last-login", -1);
    }

    /* =========================
       File Handling
       ========================= */

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save players.yml", e);
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }
}