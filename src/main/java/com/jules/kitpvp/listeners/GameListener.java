package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {

    private final KitPVP plugin;
    private final GameManager gameManager;

    public GameListener(KitPVP plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.NETHER_STAR) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Spawn Setter")) {
            if (!player.hasPermission("kitpvp.admin")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this item.");
                return;
            }

            Location spawnLocation = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
            gameManager.addSpawn(spawnLocation);
            player.sendMessage(ChatColor.GREEN + "Spawn point " + gameManager.getSpawns().size() + " has been set!");
            event.setCancelled(true);
        }
    }
}
