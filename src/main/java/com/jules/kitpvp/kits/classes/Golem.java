package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.IronPunch;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Golem extends MegaWallsClass {

    public Golem() {
        super(
                "Golem",
                new String[] {
                        "A very tanky kit.",
                        "Use your ability to deal",
                        "damage to nearby enemies."
                },
                KitPVP.getInstance().getConfig().getInt("kits.golem.cost", 3000),
                new ItemStackCreator(Material.IRON_BLOCK, "§bGolem").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 5, 1, 2, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        switch (level) {
            case 1:
                items.add(new ItemStack(Material.IRON_SWORD));
                items.add(new ItemStack(Material.COOKED_BEEF, 2));
                break;
            case 2:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 2));
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                break;
            case 5:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 4));
                items.add(Utils.getPotionRegen(3, 2));
                break;
        }
        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        IronPunch.use(p, level);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
            if (level > 0) {
                double chance = 0.1 + (level - 1) * 0.05;
                if (new Random().nextDouble() < chance) {
                    int resistanceLevel = level < 4 ? 1 : 2;
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, resistanceLevel -1));
                }
            }
        }
        if(event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
            if (level > 0) {
                double chance = 0.05 + (level - 1) * 0.025;
                if (new Random().nextDouble() < chance) {
                    event.getEntity().setVelocity(p.getLocation().getDirection().multiply(2).add(new Vector(0, 0.5, 0)));
                }
            }
        }
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

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Golem Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Iron Punch", "Deals damage and applies Slowness.", 5,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.ability.costs"),
                        Arrays.asList(Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Iron Skin", "Chance to gain Resistance when hit.", 5,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.passive1.costs"),
                        Arrays.asList(Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Stomper", "Chance to deal a knockback effect on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.passive2.costs"),
                        Arrays.asList(Material.STICKY_PISTON, Material.STICKY_PISTON, Material.STICKY_PISTON, Material.STICKY_PISTON, Material.STICKY_PISTON))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Iron Punch":
                lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + (2.5 + (level - 1) * 0.5));
                lore.add(ChatColor.GRAY + "Slowness: " + ChatColor.AQUA + (level < 4 ? "II" : "III"));
                break;
            case "Iron Skin":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 5) + "%");
                lore.add(ChatColor.GRAY + "Resistance: " + ChatColor.AQUA + (level < 4 ? "I" : "II"));
                break;
            case "Stomper":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (5 + (level - 1) * 2.5) + "%");
                break;
        }
        return lore;
    }
}
