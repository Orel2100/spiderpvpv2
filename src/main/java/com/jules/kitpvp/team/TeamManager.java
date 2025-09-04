package com.jules.kitpvp.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamManager {

    private static final Map<String, Team> teams = new HashMap<>();
    private static final Map<Player, Team> playerTeams = new HashMap<>();

    public static Team getTeam(String name) {
        return teams.get(name);
    }

    public static void createTeam(String name, Player owner) {
        Team team = new Team();
        team.addPlayer(owner);
        teams.put(name, team);
        playerTeams.put(owner, team);
    }

    public static Team getTeamByPlayer(Player p) {
        return playerTeams.get(p);
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (Team team : teams.values()) {
            players.addAll(team.getPlayers());
        }
        return players;
    }
}
