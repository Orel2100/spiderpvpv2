package com.jules.kitpvp.kits;

import com.jules.kitpvp.kits.classes.*;

public enum Kit {

    GOLEM("Golem", Golem.class, ClassType.NORMAL),
    ARCANIST("Arcanist", Arcanist.class, ClassType.HERO),
    BLAZE("Blaze", Blaze.class, ClassType.HERO),
    CREEPER("Creeper", Creeper.class, ClassType.NORMAL),
    DREADLORD("Dreadlord", Dreadlord.class, ClassType.HERO),
    ENDERMAN("Enderman", Enderman.class, ClassType.NORMAL),
    HEROBRINE("Herobrine", Herobrine.class, ClassType.NORMAL),
    HUNTER("Hunter", Hunter.class, ClassType.HERO),
    PIGMAN("Pigman", Pigman.class, ClassType.HERO),
    PIRATE("Pirate", Pirate.class, ClassType.HERO),
    SHAMAN("Shaman", Shaman.class, ClassType.HERO),
    SKELETON("Skeleton", Skeleton.class, ClassType.NORMAL),
    SPIDER("Spider", Spider.class, ClassType.NORMAL),
    SQUID("Squid", Squid.class, ClassType.NORMAL),
    ZOMBIE("Zombie", Zombie.class, ClassType.NORMAL);

    private String name;
    private Class<? extends KitClass> kitClass;
    private ClassType classType;

    Kit(String name, Class<? extends KitClass> kitClass, ClassType classType) {
        this.name = name;
        this.kitClass = kitClass;
        this.classType = classType;
    }

    public String getName() {
        return name;
    }

    public Class<? extends KitClass> getKitClass() {
        return kitClass;
    }

    public ClassType getClassType() {
        return classType;
    }
}
