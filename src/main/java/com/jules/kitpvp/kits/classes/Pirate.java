package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.CannonFire;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Pirate extends MegaWallsClass {

    public Pirate() {
        super(
                "Pirate",
                new String[]{"A swashbuckling kit that excels", "at sea and has a powerful cannon."},
                8000,
                new ItemStackCreator(Material.TNT, "§cPirate").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 2, 3, 3, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        items.add(new ItemStackCreator(Material.IRON_SWORD, "§cPirate Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());

        if (level > 0) items.add(new ItemStackCreator(Material.LEATHER_HELMET, "§cPirate Hat").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 1) items.add(new ItemStackCreator(Material.LEATHER_CHESTPLATE, "§cPirate Tunic").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 2) items.add(new ItemStackCreator(Material.LEATHER_LEGGINGS, "§cPirate Leggings").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 3) items.add(new ItemStackCreator(Material.LEATHER_BOOTS, "§cPirate Boots").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 4) {
            items.removeIf(item -> item.getType().toString().startsWith("LEATHER_"));
            items.add(new ItemStackCreator(Material.CHAINMAIL_HELMET, "§cPirate Hat").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
            items.add(new ItemStackCreator(Material.CHAINMAIL_CHESTPLATE, "§cPirate Tunic").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
            items.add(new ItemStackCreator(Material.CHAINMAIL_LEGGINGS, "§cPirate Leggings").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
            items.add(new ItemStackCreator(Material.CHAINMAIL_BOOTS, "§cPirate Boots").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        }
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
        CannonFire.use(p);
        p.setLevel(0);
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.PIRATE;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.IRON_SWORD, "§cPirate Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Pirate Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.pirate.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Cannon Fire", "Fires a powerful cannonball.", 5,
                        plugin.getConfig().getIntegerList("kits.pirate.upgrades.ability.costs"),
                        Arrays.asList(Material.TNT, Material.TNT, Material.TNT, Material.TNT, Material.TNT))
        ));

        return upgrades;
    }
}
