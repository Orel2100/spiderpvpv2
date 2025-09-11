package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Fireball;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Blaze extends MegaWallsClass {

    public Blaze() {
        super(
                "Blaze",
                new String[]{"A fiery kit that can shoot", "fireballs and is immune to fire."},
                7000,
                new ItemStackCreator(Material.BLAZE_ROD, "§6Blaze").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 2, 3, 4, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        ItemStack sword = new ItemStackCreator(Material.STONE_SWORD, "§6Blaze Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
        if (level > 2) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        if (level > 4) sword.setType(Material.IRON_SWORD);

        items.add(sword);
        items.add(new ItemStackCreator(Material.COOKED_BEEF, "§6Blaze Steak").setAmount(3 + (level > 1 ? 1 : 0) + (level > 3 ? 2 : 0)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
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
        Fireball.use(p, level);
        p.setLevel(0);
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        return effects;
    }

    @Override
    public Kit getKit() {
        return Kit.BLAZE;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§6Blaze Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Blaze Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.blaze.upgrades.kit.costs"),
                        Arrays.asList(Material.STONE_SWORD, Material.STONE_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.IRON_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Fireball", "Shoots a fireball.", 5,
                        plugin.getConfig().getIntegerList("kits.blaze.upgrades.ability.costs"),
                        Arrays.asList(Material.FIRE_CHARGE, Material.FIRE_CHARGE, Material.FIRE_CHARGE, Material.FIRE_CHARGE, Material.FIRE_CHARGE))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        return new ArrayList<>();
    }

    @Override
    public String getDifficulty() {
        return "";
    }

    @Override
    public String getPlayStyle() {
        return "";
    }

    @Override
    public String getSkillName() {
        return "";
    }

    @Override
    public String getSkillDescription() {
        return "";
    }
}
