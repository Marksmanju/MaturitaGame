package org.example.multigame.lobby;

import org.example.multigame.server.ClientHandler;
import org.example.multigame.server.ServerGameLogic;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    public String name;
    public ServerGameLogic logic = new ServerGameLogic();
    public List<ClientHandler> players = new ArrayList<>();

    public void addPlayer(ClientHandler player) {
        players.add(player);
        // Start game if 4 players join, or wait for start command
    }
}