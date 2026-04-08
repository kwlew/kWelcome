package dev.kwlew.kwelcome.listeners;

import dev.kwlew.kwelcome.kWelcome;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    private final JavaPlugin plugin;

    public ListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new JoinListener((kWelcome) plugin), plugin);
        pm.registerEvents(new QuitListener((kWelcome) plugin), plugin);
    }
}