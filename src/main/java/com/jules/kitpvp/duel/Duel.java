package com.jules.kitpvp.duel;

import com.jules.kitpvp.arena.Arena;
import org.bukkit.entity.Player;

public class Duel {

    private final Player player1;
    private final Player player2;
    private final Arena arena;
    private Player winner;

    public Duel(Player player1, Player player2, Arena arena) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Arena getArena() {
        return arena;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
