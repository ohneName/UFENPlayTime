package de.ohneName.UFENPlayTime;


import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {

    protected UFENPlayTime plugin;
    protected Scoreboard scoreboard;
    protected String objectiveName;
    protected String objectiveDisplayName;

    protected Objective objective;

    public ScoreboardHandler(UFENPlayTime plugin) {
        this.plugin = plugin;
        this.scoreboard = this.plugin.getServer().getScoreboardManager().getMainScoreboard();


        this.objectiveName = this.plugin.getConfig().getString("scoreboardObjective.name");
        this.objectiveDisplayName = this.plugin.getConfig().getString("scoreboardObjective.displayName");

        this.registerObjective();
    }

    protected void registerObjective() {
        if(scoreboard.getObjective(objectiveName) != null) {
            this.objective = scoreboard.getObjective(objectiveName);
        }
        else {
            this.objective = scoreboard.registerNewObjective(objectiveName, "dummy");
            this.objective.setDisplayName(this.objectiveDisplayName);
        }
    }

    public void updatePlayer(Player player) {
        int hoursPlayed = UFENPlayTime.splitToComponentTimes(player.getStatistic(Statistic.PLAY_ONE_TICK) / 20)[0];
        this.objective.getScore(player.getName()).setScore(hoursPlayed);
    }

    public void updateAllOnlinePlayers() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::updatePlayer);
    }

    public Objective getObjective() {
        return objective;
    }
}
