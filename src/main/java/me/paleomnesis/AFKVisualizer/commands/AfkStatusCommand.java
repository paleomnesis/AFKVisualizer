package me.paleomnesis.AFKVisualizer.commands;

import me.paleomnesis.AFKVisualizer.afk.AfkManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AfkStatusCommand implements CommandExecutor {

    private final Plugin plugin;
    private final AfkManager afkManager;

    public AfkStatusCommand(AfkManager afkManager) {
        this.afkManager = afkManager;
        this.plugin = Bukkit.getPluginManager().getPlugin("AFKVisualizer");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        long thresholdMillis = 1000L * config.getInt("afk-threshold-seconds", 120);
        List<Player> afkPlayers = afkManager.getAfkPlayers(thresholdMillis);

        String header = getMessage(config, "messages.afk-list-header", "&eCurrently AFK players:");
        String entryFormat = getMessage(config, "messages.afk-list-entry", "&b- {player}");
        String none = getMessage(config, "messages.no-afk-players", "&7There are no AFK players right now.");

        if (afkPlayers.isEmpty()) {
            sender.sendMessage(none);
            return true;
        }

        sender.sendMessage(header);
        for (Player p : afkPlayers) {
            sender.sendMessage(entryFormat.replace("{player}", p.getName()));
        }

        return true;
    }

    private String getMessage(FileConfiguration config, String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path, def));
    }
}
