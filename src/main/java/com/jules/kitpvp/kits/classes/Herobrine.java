package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Wrath;
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

import java.util.*;

public class Herobrine extends MegaWallsClass {

    public Herobrine() {
        super(
                "Herobrine",
                new String[]{"A mysterious and powerful kit."},
                KitPVP.getInstance().getConfig().getInt("kits.herobrine.cost", 10000),
                new ItemStackCreator(Material.NETHER_STAR, "§dHerobrine").build()
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
                items.add(new ItemStack(Material.COOKED_BEEF, 2));
                break;
            case 2:
                items.add(new ItemStackCreator(Material.STONE_SWORD).addEnchantment(Enchantment.DURABILITY, 1).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 2));
                break;
            case 3:
                items.add(new ItemStack(Material.IRON_SWORD));
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                items.add(Utils.getPotionHeal(1, 1));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 2).build());
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                items.add(Utils.getPotionHeal(1, 1));
                items.add(Utils.getPotionSpeed(1));
                break;
            case 5:
                items.add(new ItemStack(Material.DIAMOND_SWORD));
                items.add(new ItemStack(Material.COOKED_BEEF, 3));
                items.add(Utils.getPotionHeal(2, 1));
                items.add(Utils.getPotionSpeed(2));
                items.add(new ItemStackCreator(Material.IRON_HELMET).addEnchantment(Enchantment.WATER_WORKER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
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
        Wrath.use(p, level);
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * (int) (2 + (level * 0.5)), 0));
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
            double chance = (0.27 + (level - 1) * 0.06);
            if (new Random().nextDouble() < chance) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 2));
            }
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.IRON_ORE) {
            MPlayer mPlayer = MPlayer.getMPlayer(e.getPlayer().getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.GATHERING).get(0));
            if (level > 0) {
                mPlayer.setCoins(mPlayer.getCoins() + level * 10);
                e.getPlayer().sendMessage(ChatColor.GOLD + "+ " + level * 10 + " coins!");
                double chance = (0.1 + (level - 1) * 0.11);
                if (new Random().nextDouble() < chance) {
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND));
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
        return Kit.HEROBRINE;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Herobrine Kit",
                        Arrays.asList(ChatColor.GRAY + "Upgrade your starting kit."),
                        5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.kit.costs"),
                        Arrays.asList(Material.STONE_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_HELMET))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Wrath",
                        Arrays.asList(ChatColor.GRAY + "Strikes nearby enemies with lightning", ChatColor.GRAY + "for " + ChatColor.RED + "1.0" + ChatColor.GRAY + " damage."),
                        5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.ability.costs"),
                        Arrays.asList(Material.NETHER_STAR, Material.NETHER_STAR, Material.NETHER_STAR, Material.NETHER_STAR, Material.NETHER_STAR))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Power",
                        Arrays.asList(ChatColor.GRAY + "Gain a " + ChatColor.RED + "Strength I" + ChatColor.GRAY + " effect for " + ChatColor.GREEN + "2.0", ChatColor.GRAY + "seconds upon killing an enemy."),
                        5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.passive1.costs"),
                        Arrays.asList(Material.DIAMOND_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Flurry",
                        Arrays.asList(ChatColor.GREEN + "27%" + ChatColor.GRAY + " chance to gain " + ChatColor.AQUA + "Speed II" + ChatColor.GRAY + " for 1 second on hit."),
                        5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.passive2.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        upgrades.put(UpgradeCategory.GATHERING, Arrays.asList(
                new Upgrade("Treasure Hunter",
                        Arrays.asList(ChatColor.GREEN + "10.0%" + ChatColor.GRAY + " chance to find extra", ChatColor.GRAY + "treasures when mining."),
                        5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.gathering.costs"),
                        Arrays.asList(Material.GOLD_INGOT, Material.GOLD_INGOT, Material.GOLD_INGOT, Material.GOLD_INGOT, Material.GOLD_INGOT))
        ));

        return upgrades;
    }
}
