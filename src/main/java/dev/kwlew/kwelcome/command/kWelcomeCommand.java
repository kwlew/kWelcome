package dev.kwlew.kwelcome.command;

import dev.kwlew.kwelcome.kWelcome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /kwelcome reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("kwelcome.reload")) {
                sender.sendMessage(Component.text("[kWelcome] No permission to use this command!", NamedTextColor.RED));
                return true;
            }

            plugin.getConfigManager().reloadConfig();
            plugin.getMessageManager().reload();
            plugin.getPlayerStorage().reload();

            sender.sendMessage(Component.text("[kWelcome] Reloaded!", NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("[kWelcome] Usage: /kwelcome reload", NamedTextColor.RED));
        return true;
    }
}