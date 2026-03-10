package org.example.multigame.lobby;

import org.example.multigame.server.ServerGameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyManager {
    // Map of Lobby Names to their specific Game Logic instances
    private final Map<String, ServerGameLogic> activeLobbies = new ConcurrentHashMap<>();

    /**
     * Gets an existing lobby logic or creates a new one if it doesn't exist.
     */
    public synchronized ServerGameLogic getOrCreateLobby(String lobbyName) {
        if (!activeLobbies.containsKey(lobbyName)) {
            System.out.println("Creating new lobby: " + lobbyName);
            activeLobbies.put(lobbyName, new ServerGameLogic());
            for (String i: activeLobbies.keySet()){
                System.out.println(i);
            }
        }
        return activeLobbies.get(lobbyName);
    }

    /**
     * Optional: Clean up empty lobbies to save memory.
     */
    public synchronized void removeLobby(String lobbyName) {
        activeLobbies.remove(lobbyName);
    }

    public Map<String, ServerGameLogic> getActiveLobbies() {
        return activeLobbies;
    }

    public synchronized List<String> getLobbyNames() {
        return new ArrayList<>(activeLobbies.keySet());
    }
}