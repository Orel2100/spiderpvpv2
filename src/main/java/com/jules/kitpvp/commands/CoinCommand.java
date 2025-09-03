package com.jules.kitpvp.commands;

import com.jules.kitpvp.currency.CoinManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinCommand implements CommandExecutor {

    private final CoinManager coinManager;

    public CoinCommand(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        double balance = coinManager.getBalance(player);
        player.sendMessage("Your coin balance is: " + balance);

        return true;
    }
}
