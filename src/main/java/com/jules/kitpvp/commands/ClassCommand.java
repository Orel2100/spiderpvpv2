package com.jules.kitpvp.commands;

import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.kits.ClassType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        ClassSelectorGUI gui = new ClassSelectorGUI(ClassType.NORMAL, player);
        gui.open(player);

        return true;
    }
}
