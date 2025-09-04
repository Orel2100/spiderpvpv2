package com.jules.kitpvp.listeners;

import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.player.MPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    private final ClassManager classManager;

    public GUIListener(ClassManager classManager) {
        this.classManager = classManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof ClassSelectorGUI) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            String kitName = event.getCurrentItem().getItemMeta().getDisplayName();
            KitClass kit = classManager.getClass(kitName);
            if (kit != null) {
                new UpgradeGUI(player, kit).open();
            }
        } else if (holder instanceof UpgradeGUI) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            UpgradeGUI gui = (UpgradeGUI) event.getInventory().getHolder();
            KitClass kit = gui.getKit();
            MPlayer mPlayer = MPlayerManager.getMPlayer(player.getName());
            Upgrade upgrade = new Upgrade(player, kit, UpgradeType.KIT);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            if (event.getSlot() == 15 && event.getCurrentItem().getType() == Material.ANVIL) {
                int cost = kit.getUpgradePrice(upgrade.getNextUpgrade());
                if (mPlayer.getPlayerData().getCoins() >= cost) {
                    mPlayer.getPlayerData().setCoins(mPlayer.getPlayerData().getCoins() - cost);
                    upgrade.upgrade();
                    player.sendMessage(ChatColor.GREEN + "You have successfully upgraded " + kit.getName() + " to level " + upgrade.getCurrentUpgrade() + "!");
                    player.closeInventory();
                    new UpgradeGUI(player, kit).open();
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough coins to purchase this upgrade!");
                }
            }
        } else if (holder instanceof ShopGUI) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            if (event.getSlot() == 12) {
                new ClassSelectorGUI(classManager, ClassType.NORMAL).open(player);
            } else if (event.getSlot() == 14) {
                new ClassSelectorGUI(classManager, ClassType.HERO).open(player);
            }
        }
    }
}
