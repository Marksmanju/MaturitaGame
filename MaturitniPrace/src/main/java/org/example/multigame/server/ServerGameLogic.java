package org.example.multigame.server;

import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;
import org.example.multigame.shared.PlayerState;
import org.example.multigame.shared.ProjectileState;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGameLogic {
    public GameState state = new GameState();
    private int idCounter = 1;
    private final int speed = 5;
    private long lastTimerUpdate = System.currentTimeMillis();
    private static final int PROJECTILE_SPEED = 8;
    private static final int PROJECTILE_TTL = 120; // ~2 seconds at 60 FPS


    public ServerGameLogic() {
        state.player1 = new PlayerState(250-20, 50-20,false);
        state.player2 = new PlayerState(250-20, 450-20,false);
        state.player3 = new PlayerState(450-20, 250-20,false);
        state.player4 = new PlayerState(50-20, 250-20,false);
        state.gameTimer = 5;
        state.activeLobbies = new ConcurrentHashMap<>();
    }

    public synchronized int getNextPlayerId() {
        if (idCounter > 4) return -1; // Lobby full
        return idCounter++;
    }

    public synchronized void applyInput(PlayerInput input, int playerId) {
        PlayerState p = (playerId == 1) ? state.player1
                : (playerId == 2) ? state.player2
                : (playerId == 3) ? state.player3
                : (playerId == 4) ? state.player4
                : null;
        //System.out.println("Input is applied");
        if ((input.up) && (playerId == 3 || playerId == 4) && (p.y > 140)) p.y -= speed;
        if ((input.down) && (playerId == 3 || playerId == 4)&& (p.y < 320)) p.y += speed;
        if ((input.left)  && (playerId == 1 || playerId == 2)&& (p.x > 140)) p.x -= speed;
        if ((input.right) && (playerId == 1 || playerId == 2)&& (p.x < 320)) p.x += speed;

        p.targetMouseX = input.mouseX;
        p.targetMouseY = input.mouseY;
    }

    public synchronized void setOnline(int playerId){
        boolean b = (playerId == 1) ? state.player1.online = true
                : (playerId == 2) ? state.player2.online = true
                : (playerId == 3) ? state.player3.online = true
                : (playerId == 4) ? state.player4.online = true
                : null;
    }

    public synchronized void setOffline(int playerId){
        boolean b = (playerId == 1) ? state.player1.online = false
                : (playerId == 2) ? state.player2.online = false
                : (playerId == 3) ? state.player3.online = false
                : (playerId == 4) ? state.player4.online = false
                : null;
    }


    public synchronized void changeGameState(){

    }

    private void spawnProjectiles() {
        spawnForPlayer(state.player1);
        spawnForPlayer(state.player2);
        spawnForPlayer(state.player3);
        spawnForPlayer(state.player4);
    }

    private void spawnForPlayer(PlayerState p) {
        if (!p.online) return;

        double angle = Math.atan2(
                p.targetMouseY - p.y,
                p.targetMouseX - p.x
        );

        state.projectileStates.add(
                new ProjectileState(
                        p.x + 10,
                        p.y + 10,
                        PROJECTILE_SPEED,
                        PROJECTILE_TTL,
                        angle
                )
        );
    }

    public synchronized void updateTimer() {
        long now = System.currentTimeMillis();

        if (now - lastTimerUpdate >= 1000) { // 1 second
            lastTimerUpdate = now;

            if (state.gameTimer <= 0) {
                state.gameTimer = 5;
                spawnProjectiles();
            } else {
                state.gameTimer--;
            }
        }
    }
    public synchronized void updateProjectiles() {
        Iterator<ProjectileState> it = state.projectileStates.iterator();
        while (it.hasNext()) {
            ProjectileState p = it.next();

            p.x += (int)(Math.cos(p.direction) * p.speed);
            p.y += (int)(Math.sin(p.direction) * p.speed);
            p.timeToLive--;

            if (p.timeToLive <= 0) {
                it.remove();
            }
        }
    }

}