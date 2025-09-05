package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.arena.Arena;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.ShopGUI;
import com.jules.kitpvp.kits.ClassType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    private final KitPVP plugin;

    public ItemListener(KitPVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        Material itemType = player.getItemInHand().getType();
        String itemName = player.getItemInHand().hasItemMeta() ? player.getItemInHand().getItemMeta().getDisplayName() : "";

        if (itemType == Material.COMMAND_BLOCK && itemName.equals(ChatColor.GREEN + "PLAY!")) {
            Arena arena = plugin.getArenaManager().getArena("ffa");
            if (arena != null && !arena.getSpawnPoints().isEmpty()) {
                player.teleport(arena.getSpawnPoints().get(0));
            }
        } else if (itemType == Material.EMERALD && itemName.equals(ChatColor.GREEN + "Shop")) {
            new ShopGUI().open(player);
        } else if (itemType == Material.IRON_SWORD && itemName.equals(ChatColor.GREEN + "Kit Selector")) {
            new ClassSelectorGUI(ClassType.NORMAL).open(player);
        }
    }
}
