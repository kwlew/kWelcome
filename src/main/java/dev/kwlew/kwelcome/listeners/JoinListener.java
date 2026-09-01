package dev.kwlew.kwelcome.listeners;

import dev.kwlew.kwelcome.kWelcome;
import dev.kwlew.kwelcome.managers.PlayerStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;
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

        PlayerStorage.LoginState loginState = plugin.getPlayerStorage().handleLogin(
                playerId,
                !player.hasPlayedBefore()
        );

        boolean joinMessageEnabled = plugin.getConfigManager().isJoinMessageEnabled();
        boolean isFirstEnabled = plugin.getConfigManager().isFirstJoinMessageEnabled();

        if (loginState.firstJoin() && isFirstEnabled) {
            plugin.getMessageManager().broadcast("player.first-join", plugin.getMessageManager().placeholder("player", player.getName()));
        }
        else if (joinMessageEnabled) {
            plugin.getMessageManager().broadcast(
                    "player.join",
                    plugin.getMessageManager().placeholder("player", player.getName()),
                    plugin.getMessageManager().placeholder("last_login", formatTime(loginState.previousLogin()))
            );
        }
    }

    private String formatTime(long millis) {
        if (millis < 0) {
            return plugin.getMessageManager().getRaw("misc.unknown-last-login", "unknown");
        }
        return plugin.getConfigManager().getLastLoginFormatter().format(Instant.ofEpochMilli(millis));
    }
}
