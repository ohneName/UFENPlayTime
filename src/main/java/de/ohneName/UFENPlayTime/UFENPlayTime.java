package de.ohneName.UFENPlayTime;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.ohneName.UFENPlayTime.commands.PlayTimeCommandExecutor;
import de.ohneName.UFENPlayTime.listener.PlayerJoinListener;
import de.ohneName.UFENPlayTime.scheduler.UpdateScoreTask;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;


public class UFENPlayTime extends JavaPlugin {

    protected ScoreboardHandler scoreboardHandler;
    private Gson gson;

    @Override
    public void onEnable() {

        this.getLogger().info("loading ...");
        this.saveDefaultConfig();

        if(this.getConfig().getBoolean("commandEnabled")) {
            Objects.requireNonNull(this.getCommand("playtime")).setExecutor(new PlayTimeCommandExecutor(this));
            this.getLogger().info("Commands are enabled.");
        }
        else {
            this.getLogger().info("Commands are NOT enabled.");
        }

        // Load JSON library
        gson = new Gson();

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

    public Duration getPlayerPlayTime(OfflinePlayer offlinePlayer) throws PlayerUnknownException {

        int minutesPlayed = getStatisticPlayOneMinute(offlinePlayer);

        return Duration.ofMinutes(minutesPlayed);

    }


    public int getStatisticPlayOneMinute(OfflinePlayer offlinePlayer) throws PlayerUnknownException {
        if (offlinePlayer.getPlayer() != null) {
            int ticksPlayed =  offlinePlayer.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE);
            return Math.toIntExact(ticksPlayed / 20 / 60);
        } else {
            if (offlinePlayer.hasPlayedBefore()) {

                UUID uuid = offlinePlayer.getUniqueId();
                World world = this.getServer().getWorlds().get(0);
                File statsFile = new File(new File(world.getWorldFolder(), "stats"), uuid + ".json");

                try {
                    FileReader fileReader = new FileReader(statsFile);
                    JsonReader reader = new JsonReader(fileReader);
                    JsonObject json = gson.fromJson(reader, JsonObject.class);

                    long ticksPlayed = json.getAsJsonObject("stats")
                            .getAsJsonObject("minecraft:custom")
                            .getAsJsonObject("minecraft:play_time").getAsLong();

                    // 20 Ticks per second, 60 seconds per Minute
                    return Math.toIntExact(ticksPlayed / 20 / 60);


                } catch (FileNotFoundException e) {
                    throw new PlayerUnknownException();
                }
            }
        }

        throw new PlayerUnknownException();
    }
}
