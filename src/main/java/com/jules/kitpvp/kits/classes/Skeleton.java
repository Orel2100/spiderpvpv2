package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.ExplosiveArrow;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Skeleton extends MegaWallsClass {

    public Skeleton() {
        super(
                "Skeleton",
                new String[]{"A ranged damage class with a focus", "on using its bow to pick off targets."},
                KitPVP.getInstance().getConfig().getInt("kits.skeleton.cost", 1000),
                new ItemStackCreator(Material.BONE, "§fSkeleton").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 3, 2, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        ItemStack sword = new ItemStackCreator(Material.WOODEN_SWORD, "§fSkeleton Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
        ItemStack bow = new ItemStackCreator(Material.BOW, "§fSkeleton Bow").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
        items.add(new ItemStackCreator(Material.ARROW, "§fSkeleton Arrow").setAmount(16).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.COOKED_BEEF, "§fSkeleton Steak").setAmount(2 + (level > 2 ? 1 : 0) + (level > 3 ? 1 : 0) + (level > 4 ? 1 : 0)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());

        if (level > 1) sword.setType(Material.STONE_SWORD);
        if (level > 3) sword.setType(Material.IRON_SWORD);
        if (level > 4) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        if (level > 2) bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        if (level > 3) bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        if (level > 4) bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

        items.add(0, sword);
        items.add(1, bow);

        return items;
    }

    @Override
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();

        if (event.getBow() == null || !event.getBow().hasItemMeta() || !event.getBow().getItemMeta().hasLore() || !event.getBow().getItemMeta().getLore().contains(KitUtils.KIT_ITEM_LORE)) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        int salvagingLevel = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (salvagingLevel > 0) {
            double chance = 0.1 + (salvagingLevel - 1) * 0.05;
            if (new Random().nextDouble() < chance) {
                p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
            }
        }

        if (p.getLevel() < KitClass.ABILITY_XP_COST) {
            return;
        }
        int explosiveLevel = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        ExplosiveArrow.use(p, explosiveLevel, event);
        p.setLevel(0);
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
        if (level > 0) {
            int duration = 2 + (level > 1 ? 1 : 0) + (level > 3 ? 1 : 0) + (level > 4 ? 1 : 0);
            int regenLevel = level < 3 ? 1 : 2;
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * duration, regenLevel - 1));
        }
    }

    @Override
    public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Arrow) {
            org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) event.getEntity();
            if (ExplosiveArrow.exArrow.containsKey(arrow)) {
                arrow.getWorld().createExplosion(arrow.getLocation(), 2.0f, false);
                double damage = ExplosiveArrow.exArrow.get(arrow);
                for (org.bukkit.entity.Entity entity : arrow.getNearbyEntities(2.0, 2.0, 2.0)) {
                    if (entity instanceof org.bukkit.entity.Player && entity != arrow.getShooter()) {
                        ((org.bukkit.entity.Player) entity).damage(damage, (org.bukkit.entity.Player) arrow.getShooter());
                    }
                }
                ExplosiveArrow.exArrow.remove(arrow);
            }
        }
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) { return new ArrayList<>(); }

    @Override
    public Kit getKit() { return Kit.SKELETON; }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.BOW, "§fSkeleton Bow").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Skeleton Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.kit.costs"),
                        Arrays.asList(Material.BOW, Material.STONE_SWORD, Material.BOW, Material.IRON_SWORD, Material.BOW))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Explosive Arrow", "Fires an explosive arrow that deals " + ChatColor.RED + "4.0" + ChatColor.GRAY + " health damage.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.ability.costs"),
                        Arrays.asList(Material.TNT, Material.TNT, Material.TNT, Material.TNT, Material.TNT))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Salvaging", ChatColor.GREEN + "10%" + ChatColor.GRAY + " chance to get an arrow back when you shoot a bow.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.passive1.costs"),
                        Arrays.asList(Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Bone Shield", "Gain " + ChatColor.AQUA + "Regeneration I" + ChatColor.GRAY + " for 2 seconds after killing a player.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.passive2.costs"),
                        Arrays.asList(Material.BONE, Material.BONE, Material.BONE, Material.BONE, Material.BONE))
        ));

        return upgrades;
    }
}
