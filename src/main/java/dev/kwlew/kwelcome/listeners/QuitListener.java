package dev.kwlew.kwelcome.listeners;

import dev.kwlew.kwelcome.kWelcome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final kWelcome plugin;

    public QuitListener(kWelcome plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(null);

        Player player = event.getPlayer();

        String playerName = player.getName();

        boolean quitMessageEnabled = plugin.getConfigManager().isQuitMessageEnabled();
        if (quitMessageEnabled) {
            plugin.getMessageManager().broadcast("player.quit", plugin.getMessageManager().placeholder("player", playerName));
        }
    }
}
