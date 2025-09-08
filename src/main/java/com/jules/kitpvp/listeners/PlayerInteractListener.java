package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.game.GameManager;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.ShopGUI;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final KitPVP plugin;
    private final GameManager gameManager;

    public PlayerInteractListener(KitPVP plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        // Handle Spawn Setter item
        if (item.getType() == Material.NETHER_STAR && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Spawn Setter")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!player.hasPermission("kitpvp.admin")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this item.");
                    return;
                }
                Location spawnLocation = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
                gameManager.addSpawn(spawnLocation);
                player.sendMessage(ChatColor.GREEN + "Spawn point " + gameManager.getSpawns().size() + " has been set!");
                event.setCancelled(true);
            }
            return;
        }

        // Handle PLAY! item (Cake)
        if (item.getType() == Material.CAKE && item.hasItemMeta() && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("PLAY!")) {
            Location spawnPoint = gameManager.getRandomSpawn();
            if (spawnPoint != null) {
                player.teleport(spawnPoint);
                player.sendMessage(ChatColor.GREEN + "You have been teleported to the arena!");
            } else {
                player.sendMessage(ChatColor.RED + "There are no spawn points set! Please contact an admin.");
            }
            event.setCancelled(true);
            return;
        }

        if (item.hasItemMeta()) {
            String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

            if (item.getType() == Material.IRON_SWORD && "Kit Selector".equals(displayName)) {
                event.setCancelled(true);
                new ClassSelectorGUI(ClassType.NORMAL).open(player);
                return;
            }

            if (item.getType() == Material.EMERALD && "Shop".equals(displayName)) {
                event.setCancelled(true);
                new ShopGUI().open(player);
                return;
            }
        }

        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onInteract(player, event);
        }
    }
}
