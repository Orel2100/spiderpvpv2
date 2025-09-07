package com.jules.kitpvp.commands;

import com.jules.kitpvp.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        Configuration locations = Configuration.getConfig("locations");
        locations.set("lobby.world", loc.getWorld().getName());
        locations.set("lobby.x", loc.getX());
        locations.set("lobby.y", loc.getY());
        locations.set("lobby.z", loc.getZ());
        locations.set("lobby.yaw", loc.getYaw());
        locations.set("lobby.pitch", loc.getPitch());
        locations.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Lobby spawn point set!");

        return true;
    }
}
