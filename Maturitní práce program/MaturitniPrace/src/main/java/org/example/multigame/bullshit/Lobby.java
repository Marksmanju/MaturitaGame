package org.example.multigame.bullshit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Lobby {
    private final Map<Integer, GameSession> games = new ConcurrentHashMap<>();
    private final AtomicInteger gameIdCounter = new AtomicInteger(1);

    public GameSession createGame() {
        int id = gameIdCounter.getAndIncrement();
        GameSession session = new GameSession(id);
        games.put(id, session);
        return session;
    }


    public synchronized List<GameInfo> listGames() {
        List<GameInfo> list = new ArrayList<>();
        for (GameSession g : games.values()) {
            list.add(new GameInfo(g.gameId, g.players.size()));
        }
        return list;
    }

    public GameSession getGame(int id) {
        return games.get(id);
    }
}