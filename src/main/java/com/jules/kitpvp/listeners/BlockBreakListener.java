package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());

        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onBlockBreak(event);
        }

        if (event.getBlock().getType().name().endsWith("_ORE")) {
            int level = mPlayer.getGatheringLevel();
            if (level > 0) {
                int coins = level * 5;
                mPlayer.setCoins(mPlayer.getCoins() + coins);
                player.sendMessage(org.bukkit.ChatColor.GOLD + "+ " + coins + " coins!");
            }
        }
    }
}
