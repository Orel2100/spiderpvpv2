package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.GatheringUpgradeGUI;
import com.jules.kitpvp.gui.ShopGUI;
import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.kits.Kit;
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
        if (holder == null) return;

        Player player = (Player) event.getWhoClicked();

        if (holder instanceof ShopGUI || holder instanceof ClassSelectorGUI || holder instanceof UpgradeGUI || holder instanceof GatheringUpgradeGUI) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

            if (holder instanceof ShopGUI) {
                if (itemName.equals("Global Gathering Upgrades")) {
                    new GatheringUpgradeGUI(player).open(player);
                }
                // Other shop items can be handled here
            } else if (holder instanceof ClassSelectorGUI) {
                Kit kit = Kit.valueOf(itemName.toUpperCase());
                MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
                mPlayer.setKit(kit);
                new UpgradeGUI(mPlayer).open(player);
            } else if (holder instanceof UpgradeGUI) {
                KitPVP.getInstance().getUpgradeManager().attemptPurchase(player, itemName);
            } else if (holder instanceof GatheringUpgradeGUI) {
                if (itemName.equals("Gathering Upgrade")) {
                    KitPVP.getInstance().getUpgradeManager().attemptGlobalUpgrade(player);
                }
            }
        }
    }
}
