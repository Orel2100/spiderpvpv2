package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.BurningSoul;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Pigman extends MegaWallsClass {

    public Pigman() {
        super(
                "Pigman",
                new String[]{"A strong and tanky kit that gets", "stronger as it takes damage."},
                KitPVP.getInstance().getConfig().getInt("kits.pigman.cost", 7500),
                new ItemStackCreator(Material.PORKCHOP, "§dPigman").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 4, 2, 3, 3);
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
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 4));
                break;
            case 5:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 5));
                items.add(new ItemStack(Material.IRON_CHESTPLATE));
                break;
        }
        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        BurningSoul.use(p, level);
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
        if (level > 0 && p.getHealth() <= 12) {
            int duration = (int) (2 + (level - 1) * 0.5);
            int resistanceLevel = level < 5 ? 2 : 3;
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * duration, resistanceLevel -1));
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0) {
            double chance = 0.02 + (level - 1) * 0.01;
            if (new Random().nextDouble() < chance) {
                int resistanceLevel = level < 4 ? 1 : 2;
                int regenLevel = level < 5 ? 1 : 2;
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 8, resistanceLevel - 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, regenLevel - 1));
                for (Entity entity : p.getNearbyEntities(5, 5, 5)) {
                    if (entity instanceof Player && entity != p) {
                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 8, resistanceLevel - 1));
                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, regenLevel - 1));
                    }
                }
            }
        }
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.PIGMAN;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Pigman Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_CHESTPLATE))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Burning Soul", "Summons a fire bubble that deals damage over time.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.ability.costs"),
                        Arrays.asList(Material.FIRE_CHARGE, Material.FIRE_CHARGE, Material.FIRE_CHARGE, Material.FIRE_CHARGE, Material.FIRE_CHARGE))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Valor", "Chance to give Resistance and Regen to you and nearby teammates.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.passive1.costs"),
                        Arrays.asList(Material.GOLDEN_APPLE, Material.GOLDEN_APPLE, Material.GOLDEN_APPLE, Material.GOLDEN_APPLE, Material.GOLDEN_APPLE))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Endurance", "Gain Resistance when below 6 hearts.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.passive2.costs"),
                        Arrays.asList(Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE))
        ));

        upgrades.put(UpgradeCategory.GATHERING, Arrays.asList(
                new Upgrade("Resourcefulness", "Chance to find an extra piece of iron armor in mining chests.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.gathering.costs"),
                        Arrays.asList(Material.CHEST, Material.CHEST, Material.CHEST, Material.CHEST, Material.CHEST))
        ));
        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Burning Soul":
                lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + (2.0 + (level - 1) * 0.5));
                break;
            case "Valor":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (2.0 + (level - 1) * 1.0) + "%");
                lore.add(ChatColor.GRAY + "Resistance: " + ChatColor.AQUA + (level < 4 ? "I" : "II"));
                lore.add(ChatColor.GRAY + "Regen: " + ChatColor.AQUA + (level < 5 ? "I" : "II"));
                break;
            case "Endurance":
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.GREEN + (2.0 + (level - 1) * 0.5) + "s");
                lore.add(ChatColor.GRAY + "Resistance: " + ChatColor.AQUA + (level < 5 ? "II" : "III"));
                break;
            case "Resourcefulness":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 10) + "%");
                break;
        }
        return lore;
    }
}
