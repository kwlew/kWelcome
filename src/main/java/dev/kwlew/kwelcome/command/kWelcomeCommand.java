package dev.kwlew.kwelcome.command;

import dev.kwlew.kwelcome.kWelcome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class kWelcomeCommand implements TabExecutor {

    private final kWelcome plugin;

    public kWelcomeCommand(kWelcome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1
                && sender.hasPermission("kwelcome.reload")
                && "reload".startsWith(args[0].toLowerCase(Locale.ROOT))) {
            return List.of("reload");
        }

        return Collections.emptyList();
    }
}
