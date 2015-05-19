package de.ohneName.UFENPlayTime;

import de.ohneName.UFENPlayTime.commands.PlayTimeCommandExecutor;
import de.ohneName.UFENPlayTime.listener.PlayerJoinListener;
import de.ohneName.UFENPlayTime.scheduler.UpdateScoreTask;
import org.bukkit.plugin.java.JavaPlugin;


public final class UFENPlayTime extends JavaPlugin {

    protected ScoreboardHandler scoreboardHandler;

    @Override
    public void onEnable() {

        this.getLogger().info("loading ...");
        this.saveDefaultConfig();

        if(this.getConfig().getBoolean("commandEnabled")) {
            this.getCommand("playtime").setExecutor(new PlayTimeCommandExecutor(this));
            this.getLogger().info("Commands are enabled.");
        }
        else {
            this.getLogger().info("Commands are NOT enabled.");
        }

        // Register ScoreBoard handler
        this.getLogger().info("Registering scoreboard objective.");
        this.scoreboardHandler = new ScoreboardHandler(this);

        // Register async update task every 10 seconds (200 ticks)
        new UpdateScoreTask(this).runTaskTimerAsynchronously(this, 200, 200);

        // Register onJoin listener
        new PlayerJoinListener(this);

        this.getLogger().info("Plugin loaded.");

    }

    @Override
    public void onDisable() {
        this.getLogger().info("Shutting down.");
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    /*
     * http://stackoverflow.com/questions/6118922/convert-seconds-value-to-hours-minutes-seconds-android-java
     */
    public static int[] splitToComponentTimes(int biggy) {
        int hours = (int) biggy / 3600;
        int remainder = (int) biggy - hours * 3600;
        int mins = remainder / 60;

        return new int[]{hours, mins};
    }
}
