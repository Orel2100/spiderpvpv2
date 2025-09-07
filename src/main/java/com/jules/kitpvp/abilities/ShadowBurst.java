package com.jules.kitpvp.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ShadowBurst {

    public static void use(Player p, int level) {
        for(int i = 0; i<3; i++) {
            WitherSkull ws = p.launchProjectile(WitherSkull.class);
            ws.setShooter(p);
            ws.setYield(0);
            ws.setIsIncendiary(false);
            double damage = 3.0 + (level - 1) * 0.75;
            ws.setCustomName(String.valueOf(damage));
            ws.setCustomNameVisible(false);
            if(i == 0) {
                ws.setDirection(p.getEyeLocation().getDirection());
                ws.setVelocity(ws.getDirection().multiply(0.5));
            }
            if(i == 1) {
                Location loc = ws.getLocation();
                loc.setYaw(p.getEyeLocation().getYaw() - 25);
                ws.teleport(loc);
                ws.setDirection(ws.getLocation().getDirection());
                ws.setVelocity(ws.getDirection().multiply(-0.1));
            }
            if(i == 2) {
                Location loc = ws.getLocation();
                loc.setYaw(p.getEyeLocation().getYaw() + 25);
                ws.teleport(loc);
                ws.setDirection(ws.getLocation().getDirection());
                ws.setVelocity(ws.getDirection().multiply(-0.2));
            }
        }
        p.setLevel(0);
        p.setExp(0);
    }


    private static int HelmetDefense(ItemStack helmet) {
        if (helmet != null) {
            switch (helmet.getType()) {
                case DIAMOND_HELMET:
                    return 12;
                case IRON_HELMET:
                case CHAINMAIL_HELMET:
                case GOLDEN_HELMET:
                    return 8;
                case LEATHER_HELMET:
                    return 4;
                default:
                    return 0;
            }
        }

        return 0;
    }

    private static int ChestplateDefense(ItemStack chestplate) {
        if (chestplate != null) {
            switch (chestplate.getType()) {
                case DIAMOND_CHESTPLATE:
                    return 32;
                case IRON_CHESTPLATE:
                    return 24;
                case CHAINMAIL_CHESTPLATE:
                case GOLDEN_CHESTPLATE:
                    return 20;
                case LEATHER_CHESTPLATE:
                    return 12;
                default:
                    return 0;
            }
        }

        return 0;
    }

    private static int LeggingsDefense(ItemStack leggings) {
        if (leggings != null) {
            switch (leggings.getType()) {
                case DIAMOND_LEGGINGS:
                    return 24;
                case IRON_LEGGINGS:
                    return 20;
                case CHAINMAIL_LEGGINGS:
                case GOLDEN_LEGGINGS:
                    return 12;
                case LEATHER_LEGGINGS:
                    return 8;
                default:
                    return 0;
            }
        }

        return 0;
    }

    private static int BootsDefense(ItemStack boots) {
        if (boots != null) {
            switch (boots.getType()) {
                case DIAMOND_BOOTS:
                    return 12;
                case IRON_BOOTS:
                    return 8;
                case CHAINMAIL_BOOTS:
                case GOLDEN_BOOTS:
                case LEATHER_BOOTS:
                    return 4;
                default:
                    return 0;
            }
        }

        return 0;
    }

    public static double getNewDamage(Player p, double d){
        PlayerInventory playerInventory = p.getInventory();
        double absorption = HelmetDefense(playerInventory.getHelmet()) + ChestplateDefense(playerInventory.getChestplate()) + LeggingsDefense(playerInventory.getLeggings()) + BootsDefense(playerInventory.getBoots());

        double newDamage =  (d * (1 - absorption * (1 - 25 / 100) / 100) / (1 - absorption / 100));

        short baseDurabilityLoss = (short) (d / 4);

        if (baseDurabilityLoss < 1)
            baseDurabilityLoss = 1;

        short durabilityLossSurplus = (short) (newDamage / 4 - baseDurabilityLoss);

        for (ItemStack armor : playerInventory.getArmorContents())
            armor.setDurability((short) (armor.getDurability() - durabilityLossSurplus));
        return newDamage;
    }

}
