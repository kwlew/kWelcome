package dev.kwlew.kwelcome.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MessageManager {

    private final JavaPlugin plugin;
    private final MessageRenderer.Renderer renderer = MessageRenderer.compatibleRenderer();
    private Component prefixComponent;

    private FileConfiguration config;
    private File file;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), "messages.yml");
        }

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);

        String prefix = config.getString("prefix", "");
        prefixComponent = renderer.render(prefix);
    }

    public void reload() {
        load();
    }

    // ======================
    // BASIC MESSAGE
    // ======================
    public Component get(String path) {
        return get(path, new Placeholder[0]);
    }

    // ======================
    // ACTION BAR
    // ======================
    public void sendActionBar(Player player, String path) {
        player.sendActionBar(get(path));
    }

    public void sendActionBar(Player player, String path, Placeholder... placeholders) {
        player.sendActionBar(get(path, placeholders));
    }

    // ======================
    // WITH PLACEHOLDERS
    // ======================
    public Component get(String path, Placeholder... placeholders) {
        String msg = config.getString(path);
        if (msg == null) msg = "<red>Missing message: " + path;

        return prefixComponent.append(
                renderer.render(msg, placeholders)
        );
    }

    public String getRaw(String path, String fallback) {
        return config.getString(path, fallback);
    }

    // ======================
    // SEND METHODS
    // ======================
    public void send(Player player, String path) {
        player.sendMessage(get(path));
    }

    public void send(Player player, String path, Placeholder... placeholders) {
        player.sendMessage(get(path, placeholders));
    }

    public void send(CommandSender sender, String path) {
        sender.sendMessage(get(path));
    }

    public void send(CommandSender sender, String path, Placeholder... placeholders) {
        sender.sendMessage(get(path, placeholders));
    }

    // ======================
    // BROADCAST
    // ======================
    public void broadcast(String path) {
        org.bukkit.Bukkit.broadcast(get(path));
    }

    public void broadcast(String path, Placeholder... placeholders) {
        org.bukkit.Bukkit.broadcast(get(path, placeholders));
    }

    public Placeholder placeholder(String key, String value) {
        return new Placeholder(key, value);
    }

    public record Placeholder(String key, String value) {}
}
