package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.player.MPlayer;
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
        } else if (holder instanceof com.jules.kitpvp.gui.ShopGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            if (event.getCurrentItem().getType() == Material.GOLDEN_PICKAXE) {
                new com.jules.kitpvp.gui.GatheringGUI(MPlayer.getMPlayer(player.getUniqueId())).open(player);
                return;
            }
            // Handle other shop items if necessary
        } else if (holder instanceof UpgradeGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            KitPVP.getInstance().getUpgradeManager().attemptPurchase(player, event.getCurrentItem().getItemMeta().getDisplayName());
        } else if (holder instanceof com.jules.kitpvp.gui.GatheringGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            if (event.getCurrentItem().getType() == Material.ARROW) {
                new UpgradeGUI(MPlayer.getMPlayer(player.getUniqueId())).open(player);
                return;
            }
            KitPVP.getInstance().getGatheringManager().attemptPurchase(player, event.getCurrentItem().getItemMeta().getDisplayName());
        }
    }
}
