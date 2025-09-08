package com.jules.kitpvp.kits;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GatheringManager implements Listener {

    private final KitPVP plugin;
    private final List<Upgrade> gatheringUpgrades;

    public GatheringManager(KitPVP plugin) {
        this.plugin = plugin;
        this.gatheringUpgrades = loadGatheringUpgrades();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private List<Upgrade> loadGatheringUpgrades() {
        List<Upgrade> upgrades = new java.util.ArrayList<>();
        org.bukkit.configuration.file.FileConfiguration config = plugin.getConfig();

        upgrades.add(new Upgrade("Fortune", "Chance to get double ores.", 5,
                config.getIntegerList("gathering.fortune.costs"),
                Arrays.asList(Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE)));

        upgrades.add(new Upgrade("Excavator", "Chance to get triple ores.", 3,
                config.getIntegerList("gathering.excavator.costs"),
                Arrays.asList(Material.DIAMOND_PICKAXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_PICKAXE)));

        upgrades.add(new Upgrade("Prospector", "Chance to find gold nuggets when mining stone.", 3,
                config.getIntegerList("gathering.prospector.costs"),
                Arrays.asList(Material.GOLD_NUGGET, Material.GOLD_NUGGET, Material.GOLD_NUGGET)));

        return upgrades;
    }

    public List<Upgrade> getGatheringUpgrades() {
        return gatheringUpgrades;
    }

    public void attemptPurchase(org.bukkit.entity.Player player, String itemName) {
        com.jules.kitpvp.player.MPlayer mPlayer = com.jules.kitpvp.player.MPlayer.getMPlayer(player.getUniqueId());
        String strippedItemName = org.bukkit.ChatColor.stripColor(itemName);
        String[] nameParts = strippedItemName.split(" ");
        if (nameParts.length < 2) {
            return;
        }

        int tier;
        try {
            tier = Integer.parseInt(nameParts[nameParts.length - 1]);
        } catch (NumberFormatException e) {
            return;
        }

        String upgradeName = strippedItemName.substring(0, strippedItemName.length() - 2);

        for (Upgrade upgrade : gatheringUpgrades) {
            if (upgrade.getName().equals(upgradeName)) {
                int currentLevel = mPlayer.getUpgradeLevel(upgrade);
                if (tier == currentLevel + 1) {
                    int cost = upgrade.getCost(tier);
                    if (mPlayer.getCoins() >= cost) {
                        mPlayer.setCoins(mPlayer.getCoins() - cost);
                        mPlayer.setUpgradeLevel(upgrade, tier);
                        player.sendMessage(org.bukkit.ChatColor.GREEN + "You have purchased " + upgrade.getName() + " " + tier + "!");
                        new com.jules.kitpvp.gui.GatheringGUI(mPlayer).open(player);
                    } else {
                        player.sendMessage(org.bukkit.ChatColor.RED + "You cannot afford this!");
                    }
                }
                return;
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        com.jules.kitpvp.player.MPlayer mPlayer = com.jules.kitpvp.player.MPlayer.getMPlayer(event.getPlayer().getUniqueId());

        // Fortune
        int fortuneLevel = mPlayer.getUpgradeLevel(gatheringUpgrades.get(0));
        if (fortuneLevel > 0) {
            if (event.getBlock().getType() == Material.IRON_ORE) {
                int coins = (int) (10 * Math.pow(2, fortuneLevel - 1));
                mPlayer.setCoins(mPlayer.getCoins() + coins);
                event.getPlayer().sendMessage(org.bukkit.ChatColor.GOLD + "+ " + coins + " coins!");
            } else if (event.getBlock().getType().name().endsWith("_ORE")) {
                double chance = 0.1 + (fortuneLevel - 1) * 0.1;
                if (new java.util.Random().nextDouble() < chance) {
                    event.getBlock().getDrops().forEach(itemStack -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack));
                }
            }
        }

        // Excavator
        int excavatorLevel = mPlayer.getUpgradeLevel(gatheringUpgrades.get(1));
        if (excavatorLevel > 0) {
            if (event.getBlock().getType().name().endsWith("_ORE")) {
                double chance = 0.05 + (excavatorLevel - 1) * 0.05;
                if (new java.util.Random().nextDouble() < chance) {
                    event.getBlock().getDrops().forEach(itemStack -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack));
                    event.getBlock().getDrops().forEach(itemStack -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack));
                }
            }
        }

        // Prospector
        int prospectorLevel = mPlayer.getUpgradeLevel(gatheringUpgrades.get(2));
        if (prospectorLevel > 0) {
            if (event.getBlock().getType() == Material.STONE) {
                double chance = 0.01 + (prospectorLevel - 1) * 0.01;
                if (new java.util.Random().nextDouble() < chance) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new org.bukkit.inventory.ItemStack(Material.GOLD_NUGGET));
                }
            }
        }
    }
}
