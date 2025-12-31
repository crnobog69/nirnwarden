package com.crnobog.nirnwarden.commands;

import com.crnobog.nirnwarden.NirnWarden;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Command handler for NirnWarden plugin.
 * Provides commands for viewing and modifying griefing settings.
 */
public class NirnWardenCommand implements CommandExecutor, TabCompleter {

  private static final List<String> GRIEFING_OPTIONS = Arrays.asList(
      "creeper_griefing",
      "ender_crystal_griefing",
      "ender_dragon_griefing",
      "enderman_griefing",
      "fireball_griefing",
      "rabbit_griefing",
      "sheep_griefing",
      "silverfish_griefing",
      "villager_griefing",
      "wither_griefing",
      "wither_skull_griefing"
  );

  private static final List<String> SUBCOMMANDS = Arrays.asList(
      "help", "status", "reload", "set"
  );

  private final NirnWarden plugin;

  public NirnWardenCommand(NirnWarden plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // Check permission
    if (!sender.hasPermission("nirnwarden.admin")) {
      sender.sendMessage(Component.text("You don't have permission to use this command.")
          .color(NamedTextColor.RED));
      return true;
    }

    if (args.length == 0) {
      showHelp(sender);
      return true;
    }

    String subCommand = args[0].toLowerCase();

    switch (subCommand) {
      case "help":
        showHelp(sender);
        break;
      case "status":
        showStatus(sender);
        break;
      case "reload":
        reloadConfig(sender);
        break;
      case "set":
        if (args.length < 3) {
          sender.sendMessage(Component.text("Usage: /nw set <option> <true|false>")
              .color(NamedTextColor.RED));
          return true;
        }
        setOption(sender, args[1].toLowerCase(), args[2].toLowerCase());
        break;
      default:
        sender.sendMessage(Component.text("Unknown subcommand. Use /nw help for available commands.")
            .color(NamedTextColor.RED));
    }

    return true;
  }

  private void showHelp(CommandSender sender) {
    sender.sendMessage(Component.text("=== NirnWarden Commands ===").color(NamedTextColor.GOLD));
    sender.sendMessage(Component.text("/nw help").color(NamedTextColor.AQUA)
        .append(Component.text(" - Show this help message").color(NamedTextColor.GRAY)));
    sender.sendMessage(Component.text("/nw status").color(NamedTextColor.AQUA)
        .append(Component.text(" - Show current griefing protection settings").color(NamedTextColor.GRAY)));
    sender.sendMessage(Component.text("/nw reload").color(NamedTextColor.AQUA)
        .append(Component.text(" - Reload configuration from file").color(NamedTextColor.GRAY)));
    sender.sendMessage(Component.text("/nw set <option> <true|false>").color(NamedTextColor.AQUA)
        .append(Component.text(" - Toggle a griefing option").color(NamedTextColor.GRAY)));
    sender.sendMessage(Component.text("Available options: ").color(NamedTextColor.GRAY)
        .append(Component.text(String.join(", ", GRIEFING_OPTIONS)).color(NamedTextColor.YELLOW)));
  }

  private void showStatus(CommandSender sender) {
    sender.sendMessage(Component.text("=== NirnWarden Status ===").color(NamedTextColor.GOLD));
    sender.sendMessage(Component.text("(true = protected, false = allowed)").color(NamedTextColor.GRAY));

    for (String option : GRIEFING_OPTIONS) {
      boolean value = plugin.isGriefingBlocked(option);
      NamedTextColor valueColor = value ? NamedTextColor.GREEN : NamedTextColor.RED;
      sender.sendMessage(Component.text(option + ": ").color(NamedTextColor.AQUA)
          .append(Component.text(String.valueOf(value)).color(valueColor)));
    }
  }

  private void reloadConfig(CommandSender sender) {
    plugin.reloadConfig();
    sender.sendMessage(Component.text("Configuration reloaded!").color(NamedTextColor.GREEN));
  }

  private void setOption(CommandSender sender, String option, String valueStr) {
    if (!GRIEFING_OPTIONS.contains(option)) {
      sender.sendMessage(Component.text("Unknown option: " + option).color(NamedTextColor.RED));
      sender.sendMessage(Component.text("Available options: " + String.join(", ", GRIEFING_OPTIONS))
          .color(NamedTextColor.GRAY));
      return;
    }

    boolean value;
    if (valueStr.equals("true")) {
      value = true;
    } else if (valueStr.equals("false")) {
      value = false;
    } else {
      sender.sendMessage(Component.text("Value must be 'true' or 'false'").color(NamedTextColor.RED));
      return;
    }

    plugin.setGriefingOption(option, value);

    NamedTextColor valueColor = value ? NamedTextColor.GREEN : NamedTextColor.RED;
    sender.sendMessage(Component.text("Set ").color(NamedTextColor.GRAY)
        .append(Component.text(option).color(NamedTextColor.AQUA))
        .append(Component.text(" to ").color(NamedTextColor.GRAY))
        .append(Component.text(String.valueOf(value)).color(valueColor)));
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias,
      String[] args) {
    if (!sender.hasPermission("nirnwarden.admin")) {
      return new ArrayList<>();
    }

    if (args.length == 1) {
      return SUBCOMMANDS.stream()
          .filter(s -> s.startsWith(args[0].toLowerCase()))
          .collect(Collectors.toList());
    }

    if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
      return GRIEFING_OPTIONS.stream()
          .filter(s -> s.startsWith(args[1].toLowerCase()))
          .collect(Collectors.toList());
    }

    if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
      return Arrays.asList("true", "false").stream()
          .filter(s -> s.startsWith(args[2].toLowerCase()))
          .collect(Collectors.toList());
    }

    return new ArrayList<>();
  }
}
