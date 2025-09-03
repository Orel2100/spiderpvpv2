package com.jules.kitpvp.commands;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.kits.ClassManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {

    private final ClassManager classManager;

    public ClassCommand(ClassManager classManager) {
        this.classManager = classManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        ClassSelectorGUI gui = new ClassSelectorGUI(classManager);
        gui.open(player);

        return true;
    }
}
