package com.jules.kitpvp.commands;

import com.jules.kitpvp.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Set1v1Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        Configuration locations = Configuration.getConfig("locations");
        locations.set("1v1.world", loc.getWorld().getName());
        locations.set("1v1.x", loc.getX());
        locations.set("1v1.y", loc.getY());
        locations.set("1v1.z", loc.getZ());
        locations.set("1v1.yaw", loc.getYaw());
        locations.set("1v1.pitch", loc.getPitch());
        locations.saveConfig();

        player.sendMessage(ChatColor.GREEN + "1v1 lobby spawn point set!");

        return true;
    }
}
