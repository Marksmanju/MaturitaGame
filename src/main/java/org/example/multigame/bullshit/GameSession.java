package org.example.multigame.bullshit;

import org.example.multigame.server.ClientHandler;
import org.example.multigame.server.ServerGameLogic;

import java.util.HashSet;
import java.util.Set;

public class GameSession {
    public final int gameId;
    public final ServerGameLogic logic = new ServerGameLogic();
    public final Set<ClientHandler> players = new HashSet<>();

    public GameSession(int gameId) {
        this.gameId = gameId;
    }
}