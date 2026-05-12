package dev.kwlew.kwelcome.listeners;

import dev.kwlew.kwelcome.kWelcome;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    private final kWelcome plugin;

    public ListenerManager(kWelcome plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new JoinListener(plugin), plugin);
        pm.registerEvents(new QuitListener(plugin), plugin);
    }
}
