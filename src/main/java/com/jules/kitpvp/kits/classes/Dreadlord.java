package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.ShadowBurst;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
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

import java.util.*;

public class Dreadlord extends MegaWallsClass {

    public Dreadlord() {
        super(
                "Dreadlord",
                new String[]{"A dark and powerful kit that", "can drain life from its enemies."},
                KitPVP.getInstance().getConfig().getInt("kits.dreadlord.cost", 9000),
                new ItemStackCreator(Material.WITHER_SKELETON_SKULL, "§8Dreadlord").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 3, 3, 4, 5);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        switch (level) {
            case 1:
                items.add(new ItemStack(Material.STONE_SWORD));
                items.add(new ItemStack(Material.BAKED_POTATO, 1));
                break;
            case 2:
                items.add(new ItemStackCreator(Material.STONE_SWORD).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.BAKED_POTATO, 2));
                break;
            case 3:
                items.add(new ItemStack(Material.IRON_SWORD));
                items.add(new ItemStack(Material.BAKED_POTATO, 3));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                items.add(new ItemStack(Material.BAKED_POTATO, 5));
                break;
            case 5:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.BAKED_POTATO, 8));
                items.add(new ItemStack(Material.DIAMOND_LEGGINGS));
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
        ShadowBurst.use(p, level);
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0) {
            double chance = 0.12 + (level - 1) * 0.02;
            if (new Random().nextDouble() < chance) {
                int duration = level < 3 ? 2 : (level < 5 ? 3 : 4);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * duration, 0));
            }
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
            if (level > 0) {
                double chance = 0.05 + (level - 1) * 0.025;
                if (new Random().nextDouble() < chance) {
                    int speedLevel = level < 4 ? 1 : 2;
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, speedLevel - 1));
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
        return Kit.DREADLORD;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Dreadlord Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.kit.costs"),
                        Arrays.asList(Material.STONE_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_LEGGINGS))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Shadow Burst", "Fires 3 wither skulls dealing " + ChatColor.RED + "3.0" + ChatColor.GRAY + " damage each.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.ability.costs"),
                        Arrays.asList(Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Soul Eater", ChatColor.GREEN + "12%" + ChatColor.GRAY + " chance to gain " + ChatColor.AQUA + "Regeneration I" + ChatColor.GRAY + " for 2 seconds upon killing a player.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.passive1.costs"),
                        Arrays.asList(Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Ethereal", ChatColor.GREEN + "5%" + ChatColor.GRAY + " chance to gain " + ChatColor.AQUA + "Speed I" + ChatColor.GRAY + " for 3 seconds when hit.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.passive2.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        return upgrades;
    }
}
