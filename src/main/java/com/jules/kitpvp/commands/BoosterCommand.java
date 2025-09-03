package com.jules.kitpvp.commands;

import com.jules.kitpvp.currency.BoosterManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BoosterCommand implements CommandExecutor {

    private final BoosterManager boosterManager;

    public BoosterCommand(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kitpvp.booster")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /booster <multiplier> <duration>");
            return true;
        }

        try {
            double multiplier = Double.parseDouble(args[0]);
            int duration = Integer.parseInt(args[1]);
            boosterManager.activateBooster(multiplier, duration);
            sender.sendMessage("Coin booster activated with a multiplier of " + multiplier + " for " + duration + " seconds.");
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number format.");
        }

        return true;
    }
}
