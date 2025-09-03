package com.jules.kitpvp.commands;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.arena.Arena;
import com.jules.kitpvp.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommands implements CommandExecutor {

    private final KitPVP plugin;
    private final ArenaManager arenaManager;

    public ArenaCommands(KitPVP plugin, ArenaManager arenaManager) {
        this.plugin = plugin;
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Send help message
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length == 2) {
                    arenaManager.createArena(args[1]);
                    player.sendMessage("Arena " + args[1] + " created.");
                } else {
                    player.sendMessage("Usage: /arena create <name>");
                }
                break;
            case "join":
                if (args.length == 2) {
                    Arena arena = arenaManager.getArena(args[1]);
                    if (arena != null) {
                        arena.addPlayer(player);
                        player.sendMessage("You have joined arena " + args[1]);
                    } else {
                        player.sendMessage("Arena " + args[1] + " not found.");
                    }
                } else {
                    player.sendMessage("Usage: /arena join <name>");
                }
                break;
            case "leave":
                // This will be implemented more thoroughly later
                player.sendMessage("You have left the arena.");
                break;
            case "setspawn":
                if (args.length == 2) {
                    Arena arena = arenaManager.getArena(args[1]);
                    if (arena != null) {
                        arena.addSpawnPoint(player.getLocation());
                        player.sendMessage("Spawn point set for arena " + args[1]);
                    } else {
                        player.sendMessage("Arena " + args[1] + " not found.");
                    }
                } else {
                    player.sendMessage("Usage: /arena setspawn <name>");
                }
                break;
            default:
                // Send help message
                break;
        }

        return true;
    }
}
