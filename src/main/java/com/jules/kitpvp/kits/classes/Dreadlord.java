package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.ShadowBurst;
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

public class Dreadlord extends MegaWallsClass {

    public Dreadlord() {
        super(
                "Dreadlord",
                new String[]{"A fearsome warrior of the shadows."},
                KitPVP.getInstance().getConfig().getInt("kits.dreadlord.cost", 9000),
                new ItemStackCreator(Material.IRON_HOE, "§5Dreadlord").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 3, 3, 3, 3);
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
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 4));
                items.add(Utils.getPotionSpeed(1));
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
        ShadowBurst.use(p, level);
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * (int) (2 + (level * 0.5)), 0));
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));

        if (level > 0) {
            double chance = (0.1 + (level - 1) * 0.05);
            if (new Random().nextDouble() < chance) {
                if (event.getEntity() instanceof Player) {
                    ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 3, 1));
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
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Shadow Burst", "Teleport forward and deal damage.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.ability.costs"),
                        Arrays.asList(Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Soul Eater", "Gain Regeneration on kill.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.passive1.costs"),
                        Arrays.asList(Material.GHAST_TEAR, Material.GHAST_TEAR, Material.GHAST_TEAR, Material.GHAST_TEAR, Material.GHAST_TEAR))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Wither Skull", "Chance to apply Wither on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.dreadlord.upgrades.passive2.costs"),
                        Arrays.asList(Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Shadow Burst":
                lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + (2.0 + (level - 1) * 0.5));
                break;
            case "Soul Eater":
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.GREEN + (2.0 + (level - 1) * 0.5) + "s");
                break;
            case "Wither Skull":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 5) + "%");
                break;
        }
        return lore;
    }
}
