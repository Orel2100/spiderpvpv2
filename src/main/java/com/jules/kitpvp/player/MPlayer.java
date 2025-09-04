package com.jules.kitpvp.player;

import com.jules.kitpvp.kits.KitClass;

public class MPlayer {

    private PlayerData playerData;

    public MPlayer(PlayerData playerData) {
        this.playerData = playerData;
    }

    public KitClass getCurrentClass() {
        return playerData.getKit();
    }
}
