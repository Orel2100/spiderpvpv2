package com.jules.kitpvp.commands;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "You have " + mPlayer.getCoins() + " coins.");

        return true;
    }
}
