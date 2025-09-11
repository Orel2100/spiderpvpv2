package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Heal;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Zombie extends MegaWallsClass {

    public Zombie() {
        super(
                "Zombie",
                new String[]{"A tanky kit that gets stronger", "the more zombies are nearby."},
                KitPVP.getInstance().getConfig().getInt("kits.zombie.cost", 500),
                new ItemStackCreator(Material.ROTTEN_FLESH, "§2Zombie").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 4, 1, 1, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        ItemStack sword = new ItemStackCreator(Material.IRON_SWORD, "§2Zombie Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
        items.add(new ItemStackCreator(Material.COOKED_BEEF, "§2Zombie Steak").setAmount(2 + (level > 3 ? 1 : 0) + (level > 4 ? 1 : 0)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());

        ItemStack chestplate = new ItemStackCreator(Material.CHAINMAIL_CHESTPLATE, "§2Zombie Chestplate").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
        if (level > 1) chestplate.setType(Material.IRON_CHESTPLATE);
        if (level > 2) chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (level > 4) chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.add(chestplate);

        if (level > 3) {
            sword.addEnchantment(Enchantment.DURABILITY, level - 3);
            if (level > 4) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        }

        items.add(0, sword);

        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasLore() || !p.getItemInHand().getItemMeta().getLore().contains(KitUtils.KIT_ITEM_LORE)) return;
        if (p.getLevel() < KitClass.ABILITY_XP_COST) {
            return;
        }

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        Heal.use(p, (int) (2.0 + (level - 1) * 0.5));
        p.setLevel(0);
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
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, resistanceLevel - 1));
                }
            }
        }
        if (event.getDamager() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            Player p = (Player) event.getDamager();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
            if (level > 0) {
                double chance = 0.1 + (level - 1) * 0.05;
                if (new Random().nextDouble() < chance) {
                    int speedLevel = level < 4 ? 1 : 2;
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, speedLevel - 1));
                }
            }
        }
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) { return new ArrayList<>(); }

    @Override
    public Kit getKit() { return Kit.ZOMBIE; }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.IRON_SWORD, "§2Zombie Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Zombie Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.kit.costs"),
                        Arrays.asList(Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_SWORD, Material.IRON_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Circle of Healing", "Heals " + ChatColor.GREEN + "2.0" + ChatColor.GRAY + " health to nearby allies.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.ability.costs"),
                        Arrays.asList(Material.APPLE, Material.APPLE, Material.APPLE, Material.APPLE, Material.APPLE))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Toughness", ChatColor.GREEN + "10%" + ChatColor.GRAY + " chance to gain " + ChatColor.AQUA + "Resistance I" + ChatColor.GRAY + " for 3 seconds when hit.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.passive1.costs"),
                        Arrays.asList(Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Berserk", ChatColor.GREEN + "10%" + ChatColor.GRAY + " chance to gain " + ChatColor.RED + "Strength I" + ChatColor.GRAY + " and " + ChatColor.AQUA + "Speed I" + ChatColor.GRAY + " for 3 seconds when hit by an arrow.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.passive2.costs"),
                        Arrays.asList(Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        return new ArrayList<>();
    }

    @Override
    public String getDifficulty() {
        return "§c●";
    }

    @Override
    public String getPlayStyle() {
        return "§cTank";
    }

    @Override
    public String getSkillName() {
        return "§6Circle of Healing";
    }

    @Override
    public String getSkillDescription() {
        return "Costs 100 Energy\\nHeals all nearby teammates.\\nCharge up by hitting people.";
    }
}
