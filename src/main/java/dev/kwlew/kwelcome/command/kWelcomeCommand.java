package dev.kwlew.kwelcome.command;

import dev.kwlew.kwelcome.kWelcome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

public class kWelcomeCommand implements CommandExecutor {

    private final kWelcome plugin;

    public kWelcomeCommand(kWelcome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            plugin.getMessageManager().send(sender, "command.usage");
            return true;
        }

        if (!sender.hasPermission("kwelcome.reload")) {
            plugin.getMessageManager().send(sender, "command.no-permission");
            return true;
        }

        plugin.getConfigManager().reloadConfig();
        plugin.getMessageManager().reload();
        plugin.getPlayerStorage().reload();

        plugin.getMessageManager().send(sender, "command.reload-success");
        return true;
    }
}
