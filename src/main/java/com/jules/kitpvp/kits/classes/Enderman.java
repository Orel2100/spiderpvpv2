package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Teleport;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
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
                4000,
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
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
            MPlayer mPlayer = MPlayer.getMPlayer(e.getPlayer().getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.GATHERING).get(0));
            if (level > 0) {
                double chance = 0.1 + (level - 1) * 0.05;
                if (new Random().nextDouble() < chance) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND));
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
        return Kit.ENDERMAN;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Enderman Kit", Arrays.asList("Upgrade your kit items."), 5,
                        Arrays.asList(100, 200, 300, 400, 500),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Blink", Arrays.asList("Teleport forward."), 5,
                        Arrays.asList(100, 200, 300, 400, 500),
                        Arrays.asList(Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL, Material.ENDER_PEARL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Fear", Arrays.asList("Chance to apply Slowness on hit."), 5,
                        Arrays.asList(1000, 2000, 3000, 4000, 5000),
                        Arrays.asList(Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND, Material.SOUL_SAND))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Teleportation Master", Arrays.asList("Gain Speed after using your ability."), 5,
                        Arrays.asList(1000, 2000, 3000, 4000, 5000),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        upgrades.put(UpgradeCategory.GATHERING, Arrays.asList(
                new Upgrade("Ender's Touch", Arrays.asList("Chance to drop extra diamonds", "when mining diamond ore."), 5,
                        Arrays.asList(500, 1000, 1500, 2000, 2500),
                        Arrays.asList(Material.DIAMOND, Material.DIAMOND, Material.DIAMOND, Material.DIAMOND, Material.DIAMOND))
        ));
        return upgrades;
    }
}
