package de.ohneName.UFENPlayTime.commands;

import de.ohneName.UFENPlayTime.UFENPlayTime;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTimeCommandExecutor implements CommandExecutor {

    protected UFENPlayTime plugin;

    public PlayTimeCommandExecutor(UFENPlayTime plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return this.handleConsole(sender, cmd, label, args);
        }
        else {
            Player player = (Player) sender;
            return this.handlePlayer(player, cmd, label, args);
        }
    }

    protected boolean handleConsole(CommandSender sender, Command cmd, String label, String[] args) {

        int[] times;

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
                times = plugin.getPlayerTimes(p);
                realName = p.getName();
            }
            // Player could not be found
            else {
                // Method is deprecated, but that's ok for now. Using UUIDs in commands is bullshit
                @SuppressWarnings("deprecation")
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerName);

                times = plugin.getPlayerTimes(offlinePlayer);
                realName = offlinePlayer.getName();
            }

            if(times != null) {
                sender.sendMessage(ChatColor.GRAY + realName + "'s play time: " + ChatColor.GOLD + times[0] + ":" + String.format("%02d", times[1]) + " h");
            }
            else {
                sender.sendMessage(ChatColor.GRAY + "Player " + realName + " couldn't be found.");
            }
        }
        return true;
    }

    protected boolean handlePlayer(Player player, Command cmd, String label, String[] args) {

        int[] times;

        // Output for yourself
        if(args.length == 0) {
            times = plugin.getPlayerTimes(player);
            player.sendMessage(ChatColor.GRAY + "Your play time: " + ChatColor.GOLD + times[0] + ":" + String.format("%02d", times[1]) + " h");
        }
        // We want someone else's playtime!
        else {
            String playerName = args[0];
            String realName;

            // Try to find player online
            Player p = plugin.getServer().getPlayer(playerName);

            if(p != null) {
                times = plugin.getPlayerTimes(p);
                realName = p.getName();
            }
            // Player could not be found
            else {
                // Method is deprecated, but that's ok for now. Using UUIDs in commands is bullshit
                @SuppressWarnings("deprecation")
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerName);

                times = plugin.getPlayerTimes(offlinePlayer.getPlayer());
                realName = offlinePlayer.getName();
            }

            if(times != null) {
                player.sendMessage(ChatColor.GRAY + realName + "'s play time: " + ChatColor.GOLD + times[0] + ":" + String.format("%02d", times[1]) + " h");
            }
            else {
                player.sendMessage(ChatColor.GRAY + "Player " + realName + " couldn't be found.");
            }
        }
        return true;
    }

}
