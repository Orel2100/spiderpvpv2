package com.jules.kitpvp.commands;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCoinsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kitpvp.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /addcoins <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount.");
            return true;
        }

        MPlayer mPlayer = MPlayer.getMPlayer(target.getUniqueId());
        mPlayer.setCoins(mPlayer.getCoins() + amount);

        sender.sendMessage(ChatColor.GREEN + "You have added " + amount + " coins to " + target.getName() + ".");
        target.sendMessage(ChatColor.GREEN + "You have received " + amount + " coins.");

        return true;
    }
}
