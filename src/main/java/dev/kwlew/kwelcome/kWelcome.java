package dev.kwlew.kwelcome;

import dev.kwlew.kwelcome.command.kWelcomeCommand;
import dev.kwlew.kwelcome.listeners.ListenerManager;
import dev.kwlew.kwelcome.managers.ConfigManager;
import dev.kwlew.kwelcome.managers.MessageManager;
import dev.kwlew.kwelcome.managers.PlayerStorage;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class kWelcome extends JavaPlugin {
    private static final int BSTATS_PLUGIN_ID = 31271;

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

        if (getCommand("kwelcome") == null) {
            throw new IllegalStateException("Command 'kwelcome' is missing from plugin.yml");
        }
        getCommand("kwelcome").setExecutor(new kWelcomeCommand(this));

        new Metrics(this, BSTATS_PLUGIN_ID);

        logStartupTime(getTime());
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling kWelcome...");

        saveConfig();
        if (playerStorage != null) {
            playerStorage.save();
        }
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
        getLogger().info("kWelcome enabled in " + time + "ms.");
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }
}
