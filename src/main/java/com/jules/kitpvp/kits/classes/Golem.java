package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.IronPunch;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Golem extends KitClass {

    public Golem() {
        super(
                "Golem",
                new String[] {
                        "A very tanky kit.",
                        "Use your ability to deal",
                        "damage to nearby enemies."
                },
                3000,
                new ItemStackCreator(Material.IRON_BLOCK, "§bGolem").build(),
                new Upgrade(UpgradeType.SWORD, 3),
                new Upgrade(UpgradeType.HELMET, 3),
                new Upgrade(UpgradeType.CHESTPLATE, 3),
                new Upgrade(UpgradeType.LEGGINGS, 3),
                new Upgrade(UpgradeType.BOOTS, 3),
                new Upgrade(UpgradeType.GOLDEN_APPLE, 2),
                new Upgrade(UpgradeType.ABILITY, 3)
        );
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();

        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(UpgradeType.ABILITY);
            if(level > 0) {
                p.setExp(p.getExp() + (float) (event.getDamage() / 10));
                if(p.getExp() >= 1) {
                    IronPunch.use(p, level);
                }
            }
        }
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        int swordLevel = mPlayer.getUpgradeLevel(UpgradeType.SWORD);
        if (swordLevel == 1) {
            items.add(new ItemStack(Material.IRON_SWORD));
        } else if (swordLevel == 2) {
            items.add(new ItemStack(Material.DIAMOND_SWORD));
        } else if (swordLevel == 3) {
            items.add(new ItemStackCreator(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
        }

        // Helmet
        int helmetLevel = mPlayer.getUpgradeLevel(UpgradeType.HELMET);
        if (helmetLevel == 1) {
            items.add(new ItemStack(Material.IRON_HELMET));
        } else if (helmetLevel == 2) {
            items.add(new ItemStack(Material.DIAMOND_HELMET));
        } else if (helmetLevel == 3) {
            items.add(new ItemStackCreator(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        }

        // Chestplate
        int chestplateLevel = mPlayer.getUpgradeLevel(UpgradeType.CHESTPLATE);
        if (chestplateLevel == 1) {
            items.add(new ItemStack(Material.IRON_CHESTPLATE));
        } else if (chestplateLevel == 2) {
            items.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        } else if (chestplateLevel == 3) {
            items.add(new ItemStackCreator(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        }

        // Leggings
        int leggingsLevel = mPlayer.getUpgradeLevel(UpgradeType.LEGGINGS);
        if (leggingsLevel == 1) {
            items.add(new ItemStack(Material.IRON_LEGGINGS));
        } else if (leggingsLevel == 2) {
            items.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        } else if (leggingsLevel == 3) {
            items.add(new ItemStackCreator(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        }

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        if (bootsLevel == 1) {
            items.add(new ItemStack(Material.IRON_BOOTS));
        } else if (bootsLevel == 2) {
            items.add(new ItemStack(Material.DIAMOND_BOOTS));
        } else if (bootsLevel == 3) {
            items.add(new ItemStackCreator(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        }

        // Golden Apples
        int gappleLevel = mPlayer.getUpgradeLevel(UpgradeType.GOLDEN_APPLE);
        if (gappleLevel == 1) {
            items.add(new ItemStack(Material.GOLDEN_APPLE, 2));
        } else if (gappleLevel == 2) {
            items.add(new ItemStack(Material.GOLDEN_APPLE, 3));
        }

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        effects.add(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
        return effects;
    }

    @Override
    public Kit getKit() {
        return Kit.GOLEM;
    }
}
