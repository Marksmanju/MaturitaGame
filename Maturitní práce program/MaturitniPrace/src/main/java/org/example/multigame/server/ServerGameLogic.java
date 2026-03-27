package org.example.multigame.server;

import org.example.multigame.shared.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGameLogic {
    public GameState state = new GameState();
    private int idCounter = 1;
    private int speed = 5;
    private long lastTimerUpdate = System.currentTimeMillis();
    Random random = new Random();
    private boolean pointInit = false;
    private boolean bombInit = false;
    private boolean winInit = false;
    private int eventHappen = 10;

    public ServerGameLogic() {
        state.player1 = new PlayerState("blue",0, 120,false);
        state.player2 = new PlayerState("red",745, 120,false);
        state.player3 = new PlayerState("yellow",0, 720,false);
        state.player4 = new PlayerState("green",745, 720,false);
        state.pointState = new PointState(380,420);

        state.winner = new PlayerState("",9999,9999,false);

        state.MpointState1 = new PointState(5000,5000);
        state.MpointState2 = new PointState(5000,5000);

        state.bombState1 = new BombState(5000,5000);
        state.bombState2 = new BombState(5000,5000);
        state.bombState3 = new BombState(5000,5000);

        state.preGameTimer = 30;
        state.gameTimer = 120;
        state.postGameTimer = 15;
        state.activeLobbies = new ConcurrentHashMap<>();
        state.events = Events.NONE;
        state.stateOfGame = 0;
        state.roomPlayerOnlineList = new ArrayList<>();


        state.roomPlayerOnlineList.add(false);
        state.roomPlayerOnlineList.add(false);
        state.roomPlayerOnlineList.add(false);
        state.roomPlayerOnlineList.add(false);
        state.roomPlayerOnlineList.add(false);
    }
    public synchronized int getNextPlayerOnlineId() {
        // Start from 0 to catch the first player, and use < size to avoid errors
        System.out.println("run");
        for (int i = 1; i <= state.roomPlayerOnlineList.size(); i++) {
            System.out.println("1" + i);
            if (!state.roomPlayerOnlineList.get(i)) { // Check if false
                state.roomPlayerOnlineList.set(i, true);
                System.out.println("2" + i);
                return i; // Return the index immediately; no need for labels or switches
            }
        }
        return -1; // Return -1 if no slots are available
    }

    public synchronized void applyInput(PlayerInput input, int playerId) {
        PlayerState p = (playerId == 1) ? state.player1
                : (playerId == 2) ? state.player2
                : (playerId == 3) ? state.player3
                : (playerId == 4) ? state.player4
                : null;
        //System.out.println("Input is applied");
        double speedup;
        if(state.events == Events.SPEEDUP){
            speedup = 1.5;
        }else if(state.events == Events.SPEEDDOWN){
            speedup = 0.5;
        }
        else{
            speedup = 1;
        }
        if(state.stateOfGame == 1) {
            if ((input.up) && !(p.y - speed*speedup<= 120 - 2)) p.y -= speed*speedup;
            if ((input.down) && !(p.y + speed*speedup >= 800 - 70)) p.y += speed*speedup;
            if ((input.left) && !(p.x - speed*speedup <= 0 - 2)) p.x -= speed*speedup;
            if ((input.right) && !(p.x + speed*speedup >= 800 - 50)) p.x += speed*speedup;
        }
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

    public synchronized void pointTouched(int playerId, PointState pointState){
        //System.out.println("FUCKKKKKKK");
        PlayerState p = (playerId == 1) ? state.player1
                : (playerId == 2) ? state.player2
                : (playerId == 3) ? state.player3
                : (playerId == 4) ? state.player4
                : null;
        PointState point = pointState;
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
    public synchronized void bombTouched(int playerId, BombState bombState){
        //System.out.println("FUCKKKKKKK");
        PlayerState p = (playerId == 1) ? state.player1
                : (playerId == 2) ? state.player2
                : (playerId == 3) ? state.player3
                : (playerId == 4) ? state.player4
                : null;
        BombState bomb = bombState;
        if (((p.x < bomb.x +20) && (p.x > bomb.x -40)) && ((p.y < bomb.y+20) && (p.y > bomb.y-40))){
            changeBombPosition(bomb);
            p.score -= 1;
        }
    }
    public synchronized void changePointPosition(){
        PointState p = state.pointState;
        PointState p2 = state.MpointState1;
        PointState p3 = state.MpointState2;

        p.x = random.nextInt(0,800-50);
        p.y = random.nextInt(120,800-70);

        if((state.events == Events.MOREPOINTS ) || (state.events == Events.FAKEPOINTS)) {
            p2.x = random.nextInt(0, 800 - 50);
            p2.y = random.nextInt(120, 800 - 70);

            p3.x = random.nextInt(0, 800 - 50);
            p3.y = random.nextInt(120, 800 - 70);
        }else {
            p2.x = 5000;
            p2.y = 5000;

            p3.x = 5000;
            p3.y = 5000;
        }
    }

    public synchronized void changePointPosition(PointState pointState){
        PointState p = pointState;
        p.x = random.nextInt(0,800-50);
        p.y = random.nextInt(120,800-70);
    }

    public synchronized void changeBombPosition(BombState bombState){
        BombState b = bombState;
        b.x = random.nextInt(0,800-50);
        b.y = random.nextInt(120,800-70);
    }

    public synchronized void morePoints(){
        if ((state.events == Events.MOREPOINTS) || (state.events == Events.FAKEPOINTS)){
            if(pointInit == false) {
                changePointPosition(state.MpointState1);
                changePointPosition(state.MpointState2);
                pointInit = true;
            }else {

            }
        }else {
            pointInit = false;
            state.MpointState1.x = 5000;
            state.MpointState1.y = 5000;
            state.MpointState2.x = 5000;
            state.MpointState2.y = 5000;
        }
    }

    public synchronized void bombs(){
        if (state.events == Events.BOMBS){
            if(bombInit == false) {
                changeBombPosition(state.bombState1);
                changeBombPosition(state.bombState2);
                changeBombPosition(state.bombState3);
                bombInit = true;
            }else {

            }
        }else {
            bombInit = false;
            state.bombState1.x = 5000;
            state.bombState1.y = 5000;
            state.bombState2.x = 5000;
            state.bombState2.y = 5000;
            state.bombState3.x = 5000;
            state.bombState3.y = 5000;
        }
    }

     public synchronized void updateGameTimer() {
        long now = System.currentTimeMillis();
        switch (state.stateOfGame){
            case 0:
                if (now - lastTimerUpdate >= 1000) { // 1 second
                    lastTimerUpdate = now;
                    if (state.preGameTimer > 0){
                        state.preGameTimer--;
                    }else {
                        state.stateOfGame = 1;
                    }
                }
                break;
            case 1:
                if (now - lastTimerUpdate >= 1000) { // 1 second
                    lastTimerUpdate = now;
                    if (state.gameTimer > 1){
                        state.gameTimer--;
                        randomEvent();
                    }else {
                        state.stateOfGame = 2;
                    }
                }
                break;
            case 2:
                if (now - lastTimerUpdate >= 1000) { // 1 second
                    lastTimerUpdate = now;
                    if (state.postGameTimer > 0){
                        getWinner();
                        state.events = Events.NONE;
                        state.pointState.x = 5000;
                        state.pointState.y = 5000;
                        state.postGameTimer--;
                    }else {

                    }
                }
                break;
        }
    }
    public synchronized void randomEvent(){
        if (state.gameTimer > 60){
            eventHappen --;
        }
        else {
            eventHappen -= 2;
        }

        int ranEvent;
        if (eventHappen <= 0){
            ranEvent = random.nextInt(9); //9 Due to there being a bigger chance for none to happen
            switch (ranEvent){
                case 1:
                    state.events = Events.BOMBS;
                    break;
                case 2:
                    state.events = Events.SPEEDDOWN;
                    break;
                case 3:
                    state.events = Events.SPEEDUP;
                    break;
                case 4:
                    state.events = Events.FAKEPOINTS;
                    break;
                case 5:
                    state.events = Events.MOREPOINTS;
                    break;
                case 6:
                    state.events = Events.SAMECOLOR;
                    break;
                default:
                    state.events = Events.NONE;
            }
            eventHappen = 10;
        }
    }
    public synchronized void getWinner(){
        if (winInit == false) {
            ArrayList<PlayerState> winList = new ArrayList<>();

            winList.add(state.player1);
            winList.add(state.player2);
            winList.add(state.player3);
            winList.add(state.player4);

            PlayerState max = winList.get(0);


            for (int i = 0; i < winList.size(); i++) {
                if (winList.get(i).score >= max.score) {
                    // Then update max element
                    max = winList.get(i);
                    if (state.winner.color != "") {
                        state.winner.color += " and " + winList.get(i).color;
                    } else {
                        state.winner.color += "" + winList.get(i).color;
                    }
                }
            }
            winInit = true;
        }
    }
}