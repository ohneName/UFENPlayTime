package de.ohneName.UFENPlayTime.commands;

import de.ohneName.UFENPlayTime.UFENPlayTime;
import org.bukkit.ChatColor;
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
        sender.sendMessage("Consoles don't have a play time.");
        return false;
    }

    protected boolean handlePlayer(Player player, Command cmd, String label, String[] args) {
        // TODO: ozzy: fix this :P
        int[] times;

        if(args[0]) { // FOR: /playtime [playername]
            // TODO: does the requested player exists?
            // Does this even work?
            Player otherPlayer = new Player(args[0]);
            // I don't know
            times = UFENPlayTime.splitToComponentTimes(otherPlayer.getStatistic(Statistic.PLAY_ONE_TICK) / 20);
            // Javanoob at work
        } else { // own playtime
            times = UFENPlayTime.splitToComponentTimes(player.getStatistic(Statistic.PLAY_ONE_TICK) / 20);
        }

        player.sendMessage(ChatColor.GRAY + "Play time: " + ChatColor.GOLD + times[0] + ":" + String.format("%02d", times[1]) + " h");
        return true;
    }

}
