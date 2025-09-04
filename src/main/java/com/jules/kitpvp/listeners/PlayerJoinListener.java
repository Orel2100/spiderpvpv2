package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.player.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {

    private final KitPVP plugin;
    private final PlayerManager playerManager;

    public PlayerJoinListener(KitPVP plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerManager.addPlayer(player);

        player.getInventory().clear();

        ItemStack playItem = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta playMeta = playItem.getItemMeta();
        playMeta.setDisplayName(ChatColor.GREEN + "PLAY!");
        playItem.setItemMeta(playMeta);
        player.getInventory().setItem(0, playItem);

        ItemStack shopItem = new ItemStack(Material.EMERALD);
        ItemMeta shopMeta = shopItem.getItemMeta();
        shopMeta.setDisplayName(ChatColor.GREEN + "Shop");
        shopItem.setItemMeta(shopMeta);
        player.getInventory().setItem(4, shopItem);

        ItemStack kitSelectorItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta kitSelectorMeta = kitSelectorItem.getItemMeta();
        kitSelectorMeta.setDisplayName(ChatColor.GREEN + "Kit Selector");
        kitSelectorItem.setItemMeta(kitSelectorMeta);
        player.getInventory().setItem(8, kitSelectorItem);
    }
}
