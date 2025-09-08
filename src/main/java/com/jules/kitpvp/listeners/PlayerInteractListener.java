package com.jules.kitpvp.listeners;

import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.ShopGUI;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.hasItemMeta()) {
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

            if (item.getType() == Material.COMMAND_BLOCK && "PLAY!".equals(displayName)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.GREEN + "Joining arena...");
                // Arena joining logic would go here
                return;
            }
        }


        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onInteract(player, event);
        }
    }
}
