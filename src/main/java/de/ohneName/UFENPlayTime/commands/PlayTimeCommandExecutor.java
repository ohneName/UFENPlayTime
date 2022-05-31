package de.ohneName.UFENPlayTime.commands;

import de.ohneName.UFENPlayTime.PlayerUnknownException;
import de.ohneName.UFENPlayTime.UFENPlayTime;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class PlayTimeCommandExecutor implements CommandExecutor {

    protected UFENPlayTime plugin;

    public PlayTimeCommandExecutor(UFENPlayTime plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if(!(sender instanceof Player player)) {
            return this.handleConsole(sender, cmd, label, args);
        }
        else {
            return this.handlePlayer(player, cmd, label, args);
        }
    }

    protected boolean handleConsole(CommandSender sender, Command cmd, String label, String[] args) {

        Duration playDuration = null;
        boolean playerFound = true;

        // Output for yourself
        if(args.length == 0) {
            sender.sendMessage("You need to put a player name.");
        }
        // We want someone else's playtime!
        else {
            String playerName = args[0];
            String realName;

            // Try to find player online
            Player p = plugin.getServer().getPlayer(playerName);

            if(p != null) {
                try {
                    playDuration = plugin.getPlayerPlayTime(p);
                } catch (PlayerUnknownException e) {
                    playerFound = false;
                }
                realName = p.getName();
            }
            // Player could not be found
            else {
                // Method is deprecated, but that's ok for now. Using UUIDs in commands is bullshit
                @SuppressWarnings("deprecation")
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerName);

                try {
                    playDuration = plugin.getPlayerPlayTime(offlinePlayer);
                } catch (PlayerUnknownException e) {
                    playerFound = false;
                }
                realName = offlinePlayer.getName();
            }

            if(playerFound) {
                sender.sendMessage(ChatColor.GRAY + realName + "'s play time: " + ChatColor.GOLD + playDuration.toHours() + ":" + String.format("%02d", playDuration.toMinutesPart()) + " h");
            }
            else {
                sender.sendMessage(ChatColor.GRAY + "Player " + realName + " couldn't be found.");
            }
        }
        return true;
    }

    protected boolean handlePlayer(Player player, Command cmd, String label, String[] args) {

        Duration playDuration = null;
        boolean playerFound = true;

        // Output for yourself
        if(args.length == 0) {
            try {
                playDuration = plugin.getPlayerPlayTime(player);
            } catch (PlayerUnknownException e) {
                playerFound = false;
            }

            if (playerFound) {
                player.sendMessage(ChatColor.GRAY + "Your play time: " + ChatColor.GOLD + playDuration.toHours() + ":" + String.format("%02d", playDuration.toMinutesPart()) + " h");
            } else {
                player.sendMessage(ChatColor.GRAY + "Your play time couldn't be found.");
            }

        }
        // We want someone else's playtime!
        else {
            String playerName = args[0];
            String realName;

            // Try to find player online
            Player p = plugin.getServer().getPlayer(playerName);

            if(p != null) {
                try {
                    playDuration = plugin.getPlayerPlayTime(p);
                } catch (PlayerUnknownException e) {
                    playerFound = false;
                }
                realName = p.getName();
            }
            // Player could not be found
            else {
                // Method is deprecated, but that's ok for now. Using UUIDs in commands is bullshit
                @SuppressWarnings("deprecation")
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerName);

                try {
                    playDuration = plugin.getPlayerPlayTime(offlinePlayer);
                } catch (PlayerUnknownException e) {
                    playerFound = false;
                }
                realName = offlinePlayer.getName();
            }

            if(playerFound) {
                player.sendMessage(ChatColor.GRAY + realName + "'s play time: " + ChatColor.GOLD + playDuration.toHours() + ":" + String.format("%02d", playDuration.toMinutesPart()) + " h");
            }
            else {
                player.sendMessage(ChatColor.GRAY + "Player " + realName + " couldn't be found.");
            }
        }
        return true;
    }

}
