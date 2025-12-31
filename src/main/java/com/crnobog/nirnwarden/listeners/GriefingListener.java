package com.crnobog.nirnwarden.listeners;

import com.crnobog.nirnwarden.NirnWarden;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Listener for all mob griefing events.
 * Cancels events based on configuration settings.
 */
public class GriefingListener implements Listener {

  private final NirnWarden plugin;

  public GriefingListener(NirnWarden plugin) {
    this.plugin = plugin;
  }

  /**
   * Handles explosion events for creepers, ender crystals, ghast fireballs,
   * wither, and wither skulls.
   */
  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityExplode(EntityExplodeEvent event) {
    Entity entity = event.getEntity();

    // Creeper explosions
    if (entity instanceof Creeper && plugin.isGriefingBlocked("creeper_griefing")) {
      event.blockList().clear();
      return;
    }

    // Ender Crystal explosions
    if (entity instanceof EnderCrystal && plugin.isGriefingBlocked("ender_crystal_griefing")) {
      event.blockList().clear();
      return;
    }

    // Ghast Fireball explosions
    if (entity instanceof Fireball && plugin.isGriefingBlocked("fireball_griefing")) {
      event.blockList().clear();
      return;
    }

    // Wither spawn/attack explosions
    if (entity instanceof Wither && plugin.isGriefingBlocked("wither_griefing")) {
      event.blockList().clear();
      return;
    }

    // Wither Skull projectile explosions
    if (entity instanceof WitherSkull && plugin.isGriefingBlocked("wither_skull_griefing")) {
      event.blockList().clear();
    }
  }

  /**
   * Handles block change events for enderman, ender dragon, sheep, rabbit,
   * silverfish, and villagers.
   */
  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityChangeBlock(EntityChangeBlockEvent event) {
    Entity entity = event.getEntity();

    // Enderman picking up/placing blocks
    if (entity instanceof Enderman && plugin.isGriefingBlocked("enderman_griefing")) {
      event.setCancelled(true);
      return;
    }

    // Ender Dragon destroying blocks
    if (entity instanceof EnderDragon && plugin.isGriefingBlocked("ender_dragon_griefing")) {
      event.setCancelled(true);
      return;
    }

    // Sheep eating grass
    if (entity instanceof Sheep && plugin.isGriefingBlocked("sheep_griefing")) {
      event.setCancelled(true);
      return;
    }

    // Rabbit eating carrots
    if (entity instanceof Rabbit && plugin.isGriefingBlocked("rabbit_griefing")) {
      event.setCancelled(true);
      return;
    }

    // Silverfish entering/exiting blocks
    if (entity instanceof Silverfish && plugin.isGriefingBlocked("silverfish_griefing")) {
      event.setCancelled(true);
      return;
    }

    // Villager harvesting crops
    if (entity instanceof Villager && plugin.isGriefingBlocked("villager_griefing")) {
      event.setCancelled(true);
    }
  }
}
