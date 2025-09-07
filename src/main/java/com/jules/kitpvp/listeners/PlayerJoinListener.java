package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.config.Configuration;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {

    private final KitPVP plugin;

    public PlayerJoinListener(KitPVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MPlayer.getMPlayer(player.getUniqueId());
        PlayerData.loadData(player);

        player.getInventory().clear();

        Configuration locations = Configuration.getConfig("locations");
        if (locations.get("lobby.world") != null) {
            Location loc = new Location(
                    Bukkit.getWorld(locations.get("lobby.world").toString()),
                    locations.getInt("lobby.x"),
                    locations.getInt("lobby.y"),
                    locations.getInt("lobby.z"),
                    (float) locations.get("lobby.yaw"),
                    (float) locations.get("lobby.pitch")
            );
            player.teleport(loc);
        }


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
