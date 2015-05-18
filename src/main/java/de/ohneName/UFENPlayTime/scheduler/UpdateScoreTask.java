package de.ohneName.UFENPlayTime.scheduler;

import de.ohneName.UFENPlayTime.UFENPlayTime;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateScoreTask extends BukkitRunnable {
    protected final UFENPlayTime plugin;

    public UpdateScoreTask(UFENPlayTime plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getScoreboardHandler().updateAllOnlinePlayers();
    }
}
