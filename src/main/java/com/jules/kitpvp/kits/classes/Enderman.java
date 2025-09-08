package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Teleport;
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

public class Enderman extends MegaWallsClass {

    public Enderman() {
        super(
                "Enderman",
                new String[]{"A teleporting kit that can", "reposition itself in battle."},
                KitPVP.getInstance().getConfig().getInt("kits.enderman.cost", 4000),
                new ItemStackCreator(Material.ENDER_PEARL, "§5Enderman").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 5, 3, 3);
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
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                break;
            case 5:
                items.add(new ItemStack(Material.DIAMOND_SWORD));
                items.add(new ItemStack(Material.COOKED_BEEF, 4));
                items.add(new ItemStackCreator(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_FALL, 4).build());
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
        Teleport.use(p, 15 + (level - 1) * 5);

        int level2 = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
        if (level2 > 0) {
            int speedLevel = level2 < 3 ? 1 : (level2 < 5 ? 2 : 3);
            int duration = level2 == 1 || level2 == 2 ? 3 : (level2 == 3 || level2 == 4 ? 4 : 5);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * duration, speedLevel - 1));
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0) {
            double chance = 0.1 + (level - 1) * 0.05;
            if (new Random().nextDouble() < chance) {
                int slownessLevel = level < 4 ? 1 : 2;
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, slownessLevel - 1));
            }
        }
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.ENDERMAN;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Enderman Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.enderman.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Blink", "Teleport forward.", 5,
                        plugin.getConfig().getIntegerList("kits.enderman.upgrades.ability.costs"),
                        Arrays.asList(Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Fear", "Chance to apply Slowness on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.enderman.upgrades.passive1.costs"),
                        Arrays.asList(Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Teleportation Master", "Gain Speed after using your ability.", 5,
                        plugin.getConfig().getIntegerList("kits.enderman.upgrades.passive2.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Blink":
                lore.add(ChatColor.GRAY + "Distance: " + ChatColor.GREEN + (15 + (level - 1) * 5) + " blocks");
                break;
            case "Fear":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 5) + "%");
                lore.add(ChatColor.GRAY + "Slowness: " + ChatColor.AQUA + (level < 4 ? "I" : "II"));
                break;
            case "Teleportation Master":
                lore.add(ChatColor.GRAY + "Speed: " + ChatColor.AQUA + (level < 3 ? "I" : (level < 5 ? "II" : "III")));
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.GREEN + (level == 1 || level == 2 ? 3 : (level == 3 || level == 4 ? 4 : 5)) + "s");
                break;
        }
        return lore;
    }
}
