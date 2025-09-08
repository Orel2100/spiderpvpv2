package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.BurningSoul;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Pigman extends MegaWallsClass {

    public Pigman() {
        super(
                "Pigman",
                new String[]{"A powerful warrior with a fiery soul."},
                KitPVP.getInstance().getConfig().getInt("kits.pigman.cost", 7500),
                new ItemStackCreator(Material.PORKCHOP, "§dPigman").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 4, 2, 3, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        switch (level) {
            case 1:
                items.add(new ItemStack(Material.IRON_SWORD));
                items.add(new ItemStack(Material.COOKED_PORKCHOP, 2));
                break;
            case 2:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.COOKED_PORKCHOP, 2));
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                items.add(new ItemStack(Material.COOKED_PORKCHOP, 3));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.COOKED_PORKCHOP, 3));
                break;
            case 5:
                items.add(new ItemStackCreator(Material.DIAMOND_SWORD).build());
                items.add(new ItemStack(Material.COOKED_PORKCHOP, 4));
                items.add(Utils.getPotionRegen(1, 1));
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
        BurningSoul.use(p, level);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
            if (level > 0) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * (int) (1 + (level * 0.5)), 0));
            }
        }
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
            if (level > 0) {
                double chance = 0.05 + (level - 1) * 0.025;
                if (new Random().nextDouble() < chance) {
                    p.setFireTicks(20 * 3);
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
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Burning Soul", "Gain Regeneration and Resistance.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.ability.costs"),
                        Arrays.asList(Material.BLAZE_POWDER, Material.BLAZE_POWDER, Material.BLAZE_POWDER, Material.BLAZE_POWDER, Material.BLAZE_POWDER))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Endurance", "Gain Strength on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.passive1.costs"),
                        Arrays.asList(Material.PORKCHOP, Material.PORKCHOP, Material.PORKCHOP, Material.PORKCHOP, Material.PORKCHOP))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Valor", "Chance to set enemies on fire.", 5,
                        plugin.getConfig().getIntegerList("kits.pigman.upgrades.passive2.costs"),
                        Arrays.asList(Material.FLINT_AND_STEEL, Material.FLINT_AND_STEEL, Material.FLINT_AND_STEEL, Material.FLINT_AND_STEEL, Material.FLINT_AND_STEEL))
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
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.GREEN + (4 + level) + "s");
                break;
            case "Endurance":
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.GREEN + (1.0 + (level - 1) * 0.5) + "s");
                break;
            case "Valor":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (5 + (level - 1) * 2.5) + "%");
                break;
        }
        return lore;
    }
}
