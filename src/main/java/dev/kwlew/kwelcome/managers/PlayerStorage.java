package dev.kwlew.kwelcome.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.UUID;

public class PlayerStorage {

    private static final long UNKNOWN_LOGIN = -1L;

    public record LoginState(boolean firstJoin, long previousLogin) {}

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;
    private boolean dirty;

    public PlayerStorage(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
            throw new IllegalStateException("Failed to create the kWelcome data folder");
        }

        this.file = new File(plugin.getDataFolder(), "players.yml");

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Failed to create players.yml");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to create players.yml", e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        ensurePlayersSection();
    }

    private String path(UUID uuid) {
        return "players." + uuid;
    }

    /* =========================
       Core Logic
       ========================= */

    /**
     * Handles a player login.
     * Returns whether the player is joining for the first time and their previous login timestamp.
     */
    public LoginState handleLogin(UUID uuid, boolean serverFirstJoin) {
        String playerPath = path(uuid);

        long previousLogin = config.getLong(playerPath + ".last-login", UNKNOWN_LOGIN);
        boolean firstJoin = determineFirstJoin(previousLogin, serverFirstJoin);

        config.set(playerPath + ".first-join", firstJoin);
        config.set(playerPath + ".last-login", System.currentTimeMillis());
        dirty = true;

        return new LoginState(firstJoin, previousLogin);
    }

    static boolean determineFirstJoin(long previousLogin, boolean serverFirstJoin) {
        return previousLogin == UNKNOWN_LOGIN && serverFirstJoin;
    }

    public boolean isFirstJoin(UUID uuid) {
        return config.getBoolean(path(uuid) + ".first-join", true);
    }

    public long getLastLogin(UUID uuid) {
        return config.getLong(path(uuid) + ".last-login", UNKNOWN_LOGIN);
    }

    /* =========================
       File Handling
       ========================= */

    public boolean save() {
        if (!dirty) {
            return true;
        }

        try {
            config.save(file);
            dirty = false;
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save players.yml", e);
            return false;
        }
    }

    public void reload() {
        if (!save()) {
            plugin.getLogger().warning("Player data was not reloaded because pending changes could not be saved.");
            return;
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        this.dirty = false;
        ensurePlayersSection();
    }

    private void ensurePlayersSection() {
        if (!config.contains("players")) {
            config.createSection("players");
            dirty = true;
            save();
        }
    }
}
