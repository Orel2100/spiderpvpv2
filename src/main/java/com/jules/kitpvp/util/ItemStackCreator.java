package com.jules.kitpvp.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackCreator {

    private ItemStack item;
    private ItemMeta meta;

    public ItemStackCreator(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemStackCreator(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemStackCreator(Material material, String name) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
        this.meta.setDisplayName(name);
    }

    public ItemStackCreator setName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemStackCreator setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStackCreator addLoreLine(String line) {
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        return this;
    }

    public ItemStackCreator setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemStackCreator addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStackCreator setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public ItemStackCreator addItemFlag(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
