package de.ohneName.UFENPlayTime;

import com.sun.istack.internal.Nullable;
import de.ohneName.UFENPlayTime.commands.PlayTimeCommandExecutor;
import de.ohneName.UFENPlayTime.listener.PlayerJoinListener;
import de.ohneName.UFENPlayTime.scheduler.UpdateScoreTask;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;


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

    public int[] getPlayerTimes(OfflinePlayer offlinePlayer) {
        // Needs to have played before, otherwise there's no statistics for that player
        if(offlinePlayer.getPlayer() != null) {
            return UFENPlayTime.splitToComponentTimes(offlinePlayer.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) / 20);
        }
        else {
            // We assume the player has statistics and read the json file directly
            if(offlinePlayer.hasPlayedBefore()) {
                UUID uuid = offlinePlayer.getUniqueId();
                World world = this.getServer().getWorlds().get(0);
                File statsFile = new File(new File(world.getWorldFolder(), "stats"), uuid.toString() + ".json");

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject;
                try {
                    FileReader fileReader = new FileReader(statsFile);
                    jsonObject = (JSONObject) jsonParser.parse(fileReader);
                    fileReader.close();
                } catch (IOException | ParseException e) {
                    return null;
                }

                long minutesPlayed = (long) jsonObject.get("stat.playOneMinute");

                return UFENPlayTime.splitToComponentTimes(minutesPlayed / 20);
            }
            else {
                return null;
            }
        }
    }

    /*
     * http://stackoverflow.com/questions/6118922/convert-seconds-value-to-hours-minutes-seconds-android-java
     */
    public static int[] splitToComponentTimes(long biggy) {
        int hours = (int) biggy / 3600;
        int remainder = (int) biggy - hours * 3600;
        int mins = remainder / 60;

        return new int[]{hours, mins};
    }
}
