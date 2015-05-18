UFEN - Play Time
---

This plugin allows the easy displaying of a players play time on the current map. It uses Minecraft's built in statistics and doesn't depent on databases or such. It is ready to run out of the box.

---

### Commands

* __`/playtime`__  
  Displays a players play time in hours and minutes. The command can be disabled in the configuration.
  
---

### Scoreboard Objective

This plugin creates a new objective called `onlineTime` (by default) and updates it every 10 seconds for each user. The value of this scoreboard is the play time in _full_ (completed) hours, e.g. `0` for 59 minutes, but `1` for 110 minutes.

If the objective already exists, the plugin uses the configured one. Objective name and display name are configurable.