package dev.kwlew.kwelcome;

import dev.kwlew.kwelcome.command.kWelcomeCommand;
import dev.kwlew.kwelcome.listeners.ListenerManager;
import dev.kwlew.kwelcome.managers.ConfigManager;
import dev.kwlew.kwelcome.managers.MessageManager;
import dev.kwlew.kwelcome.managers.PlayerStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class kWelcome extends JavaPlugin {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private PlayerStorage playerStorage;

    private long start;

    @Override
    public void onEnable() {
        startTime();
        saveDefaultConfig();

        initManagers();

        ListenerManager listenerManager = new ListenerManager(this);
        listenerManager.registerAll();

        Objects.requireNonNull(getCommand("kwelcome")).setExecutor(new kWelcomeCommand(this));

        logStartupTime(getTime());
    }

    @Override
    public void onDisable() {
        getLogger().info("\u001B[36mDisabling kWelcome...\u001B[0m");

        saveConfig();
    }

    private void initManagers() {
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.playerStorage = new PlayerStorage(this);
    }

    private void startTime() {
        start = System.currentTimeMillis();
    }

    private void logStartupTime(long time) {
        getLogger().info("\u001B[36mkWelcome enabled! \u001B[90m(Took \u001B[32m"
                + time + "ms\u001B[90m)\u001B[0m");
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public  MessageManager getMessageManager() {
        return messageManager;
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }
}
