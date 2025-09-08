package com.jules.kitpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("kitpvp.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        ItemStack spawnSetter = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = spawnSetter.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Spawn Setter");
        spawnSetter.setItemMeta(meta);

        player.getInventory().addItem(spawnSetter);
        player.sendMessage(ChatColor.GREEN + "You have received the Spawn Setter item. Right-click on a block to set a spawn point.");

        return true;
    }
}
