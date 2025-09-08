package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.gui.AbilityTestGUI;
import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeCategory;
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
        } else if (holder instanceof AbilityTestGUI) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasLore()) {
                return;
            }

            String kitNameLine = null;
            for (String line : event.getCurrentItem().getItemMeta().getLore()) {
                if (line.startsWith("§7Kit: §a")) {
                    kitNameLine = line;
                    break;
                }
            }

            if (kitNameLine == null) {
                return;
            }

            String kitName = ChatColor.stripColor(kitNameLine.substring("§7Kit: §a".length()));
            Kit kit = Kit.valueOf(kitName.toUpperCase());

            MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
            mPlayer.setKit(kit);

            try {
                KitClass kitInstance = kit.getKitClass().newInstance();
                if (kitInstance.getUpgrades().containsKey(UpgradeCategory.ABILITY)) {
                    Upgrade abilityUpgrade = kitInstance.getUpgrades().get(UpgradeCategory.ABILITY).get(0);
                    mPlayer.setUpgradeLevel(abilityUpgrade, 5);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            player.closeInventory();
            com.jules.kitpvp.util.KitUtils.givePlayerKitItems(player, kit);
            player.sendMessage(ChatColor.GREEN + "You have been given the " + kitName + " kit with max level ability.");
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
