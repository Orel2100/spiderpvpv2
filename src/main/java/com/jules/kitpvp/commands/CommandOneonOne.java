package com.jules.kitpvp.commands;

import com.jules.kitpvp.duel.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOneonOne implements CommandExecutor {

    private final DuelManager duelManager;

    public CommandOneonOne(DuelManager duelManager) {
        this.duelManager = duelManager;
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
            case "accept":
                duelManager.acceptDuelRequest(player);
                player.sendMessage("You have accepted the duel request.");
                break;
            case "deny":
                duelManager.denyDuelRequest(player);
                player.sendMessage("You have denied the duel request.");
                break;
            default:
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    duelManager.sendDuelRequest(player, target);
                    player.sendMessage("You have sent a duel request to " + target.getName());
                    target.sendMessage("You have received a duel request from " + player.getName());
                } else {
                    player.sendMessage("Player not found.");
                }
                break;
        }

        return true;
    }
}
