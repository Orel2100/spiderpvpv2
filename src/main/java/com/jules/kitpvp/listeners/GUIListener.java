package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        Player player = (Player) event.getWhoClicked();

        if (holder instanceof ClassSelectorGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            String kitName = event.getCurrentItem().getItemMeta().getDisplayName();
            Kit kit = Kit.valueOf(kitName.toUpperCase());
            MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
            mPlayer.setKit(kit);
            new UpgradeGUI(mPlayer).open(player);
        } else if (holder instanceof UpgradeGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
            // "§bSword Upgrade" -> "SWORD"
            String upgradeName = ChatColor.stripColor(itemName).replace(" Upgrade", "").replace(" ", "_").toUpperCase();
            try {
                UpgradeType type = UpgradeType.valueOf(upgradeName);
                MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
                KitPVP.getInstance().getUpgradeManager().upgrade(mPlayer, type);
            } catch (IllegalArgumentException e) {
                // Not an upgrade item
            }
        }
    }
}
