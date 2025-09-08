package com.jules.kitpvp.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GatheringTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE));

        org.bukkit.Location loc = player.getLocation();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                loc.clone().add(x, 0, z).getBlock().setType(Material.IRON_ORE);
            }
        }

        player.sendMessage("You have been given a diamond pickaxe and some iron ores have been placed around you.");
        return true;
    }
}
