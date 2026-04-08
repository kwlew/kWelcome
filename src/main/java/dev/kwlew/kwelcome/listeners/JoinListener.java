package dev.kwlew.kwelcome.listeners;

import dev.kwlew.kwelcome.kWelcome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener implements Listener {

    private final kWelcome plugin;

    public JoinListener(kWelcome plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.joinMessage(null);

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        long lastJoin = plugin.getPlayerStorage().getLastLogin(playerId);

        boolean firstJoin = plugin.getPlayerStorage().handleLogin(playerId);

        boolean joinMessageEnabled = plugin.getConfigManager().isJoinMessageEnabled();
        boolean isFirstEnabled = plugin.getConfigManager().ifFirstJoinMessageIsEnabled();

        if (firstJoin && isFirstEnabled) {
            plugin.getMessageManager().broadcast("player.first-join", plugin.getMessageManager().placeholder("player", player.getName()));
        }
        else if (joinMessageEnabled) {
            plugin.getMessageManager().broadcast("player.join", plugin.getMessageManager().placeholder("player", player.getName()), plugin.getMessageManager().placeholder("last_login", formatTime(lastJoin)));
        }
    }

    private String formatTime(long millis) {
        return java.time.format.DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
                .format(java.time.Instant.ofEpochMilli(millis));
    }
}
