package com.crnobog.nirnwarden;

import com.crnobog.nirnwarden.commands.NirnWardenCommand;
import com.crnobog.nirnwarden.listeners.GriefingListener;
import io.papermc.lib.PaperLib;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * NirnWarden - Mob Anti-Grief Plugin for Paper 1.21.11.
 * Prevents various mob griefing behaviors with configurable options.
 */
public class NirnWarden extends JavaPlugin {

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);

    // Save default config if it doesn't exist
    saveDefaultConfig();

    // Register event listeners
    getServer().getPluginManager().registerEvents(new GriefingListener(this), this);

    // Register commands
    NirnWardenCommand commandExecutor = new NirnWardenCommand(this);
    PluginCommand command = getCommand("nw");
    if (command != null) {
      command.setExecutor(commandExecutor);
      command.setTabCompleter(commandExecutor);
    }

    getLogger().info("[NirnWarden] пробуђен! Твоја земља сада стоји под заштитом древне моћи - ниједно чудовиште неће скрнавити твој свет!");
  }

  @Override
  public void onDisable() {
    getLogger().info("[NirnWarden] спава.");
  }

  /**
   * Check if a specific griefing type is blocked.
   *
   * @param key The config key for the griefing type
   * @return true if griefing should be blocked, false otherwise
   */
  public boolean isGriefingBlocked(String key) {
    return getConfig().getBoolean(key, true);
  }

  /**
   * Set a griefing option and save the config.
   *
   * @param key   The config key for the griefing type
   * @param value true to block griefing, false to allow it
   */
  public void setGriefingOption(String key, boolean value) {
    getConfig().set(key, value);
    saveConfig();
  }
}
