package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) return;

        FallingBlock block = (FallingBlock) event.getEntity();

        if (block.getMaterial() == Material.IRON_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());

        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onBlockBreak(event);
        }

        com.jules.kitpvp.kits.GatheringManager gatheringManager = com.jules.kitpvp.KitPVP.getInstance().getGatheringManager();
        for (com.jules.kitpvp.kits.Upgrade upgrade : gatheringManager.getGatheringUpgrades()) {
            if (upgrade.getName().equals("Coin Gathering")) {
                int coinGatheringLevel = mPlayer.getUpgradeLevel(upgrade);
                if (coinGatheringLevel > 0) {
                    if (event.getBlock().getType() == org.bukkit.Material.IRON_ORE) {
                        event.setDropItems(false);
                        int coins = (int) (10 * Math.pow(2, coinGatheringLevel - 1));
                        mPlayer.setCoins(mPlayer.getCoins() + coins);
                        player.sendMessage(org.bukkit.ChatColor.GOLD + "+ " + coins + " coins!");
                    }
                }
                break;
            }
        }
    }
}
