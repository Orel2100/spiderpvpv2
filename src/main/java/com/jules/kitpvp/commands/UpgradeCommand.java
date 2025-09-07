package com.jules.kitpvp.commands;

import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpgradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());

        if (mPlayer.getKitClass() == null) {
            player.sendMessage("You have not selected a kit!");
            return true;
        }

        try {
            new UpgradeGUI(mPlayer).open(player);
        } catch (IllegalArgumentException e) {
            player.sendMessage(org.bukkit.ChatColor.RED + "This kit has not been updated with the new upgrade system yet!");
        }
        return true;
    }
}
