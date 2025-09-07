package com.jules.kitpvp.commands;

import com.jules.kitpvp.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        Configuration locations = Configuration.getConfig("locations");
        if (locations.get("lobby.world") == null) {
            player.sendMessage(ChatColor.RED + "The lobby spawn point has not been set yet!");
            return true;
        }

        Location loc = new Location(
                Bukkit.getWorld(locations.get("lobby.world").toString()),
                locations.getInt("lobby.x"),
                locations.getInt("lobby.y"),
                locations.getInt("lobby.z"),
                (float) locations.get("lobby.yaw"),
                (float) locations.get("lobby.pitch")
        );

        player.teleport(loc);
        player.sendMessage(ChatColor.GREEN + "Teleported to the lobby!");

        return true;
    }
}
