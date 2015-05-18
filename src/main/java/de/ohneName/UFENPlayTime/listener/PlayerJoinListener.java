package de.ohneName.UFENPlayTime.listener;

import de.ohneName.UFENPlayTime.UFENPlayTime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    protected UFENPlayTime plugin;

    public PlayerJoinListener(UFENPlayTime plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.plugin.getScoreboardHandler().updatePlayer(event.getPlayer());
    }
}
