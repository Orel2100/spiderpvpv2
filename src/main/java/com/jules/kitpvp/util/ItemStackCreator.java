package com.jules.kitpvp.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ItemStackCreator {

    public static ItemStack createItem(ItemStack item, String name, int amount, List<String> lore) {
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItemStack(ItemStack item, int amount, String name, List<String> lore, HashMap<Enchantment, Integer> enchantments) {
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        if (enchantments != null) {
            item.addUnsafeEnchantments(enchantments);
        }
        return item;
    }
}
