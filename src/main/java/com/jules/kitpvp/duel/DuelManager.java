package com.jules.kitpvp.duel;

import com.jules.kitpvp.arena.Arena;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelManager {

    private final List<Duel> activeDuels = new ArrayList<>();
    private final Map<Player, Player> duelRequests = new HashMap<>();

    public void sendDuelRequest(Player requester, Player target) {
        duelRequests.put(target, requester);
    }

    public void acceptDuelRequest(Player target) {
        Player requester = duelRequests.get(target);
        if (requester != null) {
            // For now, we'll just create the duel. Arena selection will be improved later.
            Arena arena = null; // Placeholder
            startDuel(requester, target, arena);
            duelRequests.remove(target);
        }
    }

    public void denyDuelRequest(Player target) {
        duelRequests.remove(target);
    }

    public void startDuel(Player player1, Player player2, Arena arena) {
        activeDuels.add(new Duel(player1, player2, arena));
        // Teleport players to arena, etc.
    }

    public void endDuel(Duel duel) {
        activeDuels.remove(duel);
        // Teleport players back, etc.
    }
}
