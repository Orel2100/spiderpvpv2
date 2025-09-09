package com.jules.kitpvp.util;

import com.jules.kitpvp.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class KitUtils {

    public static final String KIT_ITEM_LORE = "§8Kit Item";
    public static final List<String> KIT_ITEM_LORE_LIST = Arrays.asList(KIT_ITEM_LORE);

    public static void givePlayerKitItems(Player player, Kit kit) {
        player.getInventory().clear();
        player.setLevel(100);
        try {
            for (ItemStack item : kit.getKitClass().newInstance().getStartingItems(player)) {
                player.getInventory().addItem(item);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        player.updateInventory();
    }
}
