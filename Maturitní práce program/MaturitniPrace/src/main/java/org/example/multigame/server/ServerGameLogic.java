package org.example.multigame.server;

import org.example.multigame.shared.*;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGameLogic {
    public GameState state = new GameState();
    private int idCounter = 1;
    private final int speed = 5;
    private long lastTimerUpdate = System.currentTimeMillis();
    private static final int PROJECTILE_SPEED = 8;
    private static final int PROJECTILE_TTL = 120; // ~2 seconds at 60 FPS
    private int projectileMultiplier = 0;
    Random random = new Random();


    public ServerGameLogic() {
        state.player1 = new PlayerState(250-20, 160-20,false);
        state.player2 = new PlayerState(250-20, 450-20,false);
        state.player3 = new PlayerState(450-20, 250-20,false);
        state.player4 = new PlayerState(50-20, 250-20,false);
        state.pointState = new PointState(random.nextInt(0,800-50),random.nextInt(120,800-70) );
        state.gameTimer = 120;
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
        if ((input.up) && !(p.y - speed <= 120-2)) p.y -= speed;
        if ((input.down) && !(p.y + speed >= 800 -70)) p.y += speed;
        if ((input.left) && !(p.x - speed <= 0-2)) p.x -= speed;
        if ((input.right) && !(p.x + speed >= 800 -50))  p.x += speed;

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
    public synchronized void pointTouched(int playerId){
        //System.out.println("FUCKKKKKKK");
        PlayerState p = (playerId == 1) ? state.player1
                : (playerId == 2) ? state.player2
                : (playerId == 3) ? state.player3
                : (playerId == 4) ? state.player4
                : null;
        PointState point = state.pointState;
        if (((p.x < point.x +20) && (p.x > point.x -40)) && ((p.y < point.y+20) && (p.y > point.y-40))){
            System.out.println(p.score);
            System.out.println(state.player1.score);
            System.out.println(state.player2.score);
            System.out.println(state.player3.score);
            System.out.println(state.player4.score);
            changePointPosition();
            p.score += 1;
        }
    }

    public synchronized void changePointPosition(){
        PointState p = state.pointState;
        p.x = random.nextInt(0,800-50);
        p.y = random.nextInt(120,800-70);
    }

    public synchronized void changeGameState(){

    }
    /*
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
*/
    public synchronized void updateTimer() {
        long now = System.currentTimeMillis();

        if (now - lastTimerUpdate >= 1000) { // 1 second
            lastTimerUpdate = now;
            state.gameTimer--;
        }
    }
    /*
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
*/
}