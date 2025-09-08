package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Heal;
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

public class Zombie extends MegaWallsClass {

    public Zombie() {
        super(
                "Zombie",
                new String[]{"A resilient and tanky kit."},
                KitPVP.getInstance().getConfig().getInt("kits.zombie.cost", 500),
                new ItemStackCreator(Material.ROTTEN_FLESH, "§2Zombie").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 4, 1, 3, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        items.add(new ItemStack(Material.IRON_SWORD));
        items.add(new ItemStack(Material.COOKED_BEEF, 2));

        switch (level) {
            case 2:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 1).build());
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 1).build());
                break;
            case 5:
                items.add(new ItemStackCreator(Material.DIAMOND_SWORD).build());
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
        Heal.use(p, level);
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
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 0));
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
        return Kit.ZOMBIE;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Zombie Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Heal", "Heal yourself for a small amount.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.ability.costs"),
                        Arrays.asList(Material.APPLE, Material.APPLE, Material.APPLE, Material.APPLE, Material.APPLE))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Toughness", "Chance to gain Regeneration when hit.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.passive1.costs"),
                        Arrays.asList(Material.LEATHER_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.LEATHER_CHESTPLATE))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Well Fed", "Start with extra food.", 5,
                        plugin.getConfig().getIntegerList("kits.zombie.upgrades.passive2.costs"),
                        Arrays.asList(Material.COOKED_BEEF, Material.COOKED_BEEF, Material.COOKED_BEEF, Material.COOKED_BEEF, Material.COOKED_BEEF))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Heal":
                lore.add(ChatColor.GRAY + "Heal Amount: " + ChatColor.GREEN + (2 + level) + " hearts");
                break;
            case "Toughness":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 5) + "%");
                break;
            case "Well Fed":
                lore.add(ChatColor.GRAY + "Extra Food: " + ChatColor.GREEN + level);
                break;
        }
        return lore;
    }
}
