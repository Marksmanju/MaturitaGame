package org.example.multigame.lobby;

import java.io.Serializable;
import java.util.List;

public class LobbyCommand implements Serializable {
    public String action; // "CREATE", "JOIN", "LIST"
    public String lobbyName;
    public List<String> availableLobbies;
}