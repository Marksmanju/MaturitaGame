package org.example.multigame.server;


import org.example.multigame.lobby.LobbyManager;
import org.example.multigame.shared.Events;
import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ClientHandler extends Thread {
    private LobbyManager lobbyManager;
    public int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerGameLogic logic;
    private String lobbyName;

    public ClientHandler(Socket socket, LobbyManager manager) throws IOException {
        this.lobbyManager = manager;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Player " + playerId + " connected");

    }
    public void run() {
        try {
            // PHASE 1: Lobby Selection
            // The first thing the client sends must be the Lobby Name (String)
            Object firstObject = in.readObject();

            // If the client sends "GET_LOBBIES", send the list and close connection
            if ("GET_LOBBIES".equals(firstObject)) {
                out.writeObject(lobbyManager.getLobbyNames());
                out.flush();
                return;
            }

            // Otherwise, treat the object as the Lobby Name to join
            lobbyName = (String) firstObject;

            this.logic = lobbyManager.getOrCreateLobby(lobbyName);

            synchronized (logic) {
                if (logic.state.stateOfGame == 0) {
                    this.playerId = logic.getNextPlayerOnlineId();
                }else {
                    this.playerId = -1;
                }
            }

            out.writeInt(this.playerId); // Send the ID (or -1) back
            out.flush();

            if (this.playerId == -1) {
                System.out.println("Lobby " + lobbyName + " is full or the game has started.");
                return; // Or send a "Lobby Full" message back
            }

            System.out.println("Player " + playerId + " joined lobby: " + lobbyName);

            // PHASE 2: Game Loop (Existing Logic)
            while (true) {
                PlayerInput input = (PlayerInput) in.readObject();
                logic.applyInput(input, playerId);
                logic.setOnline(playerId);
                logic.pointTouched(playerId);
                if (logic.state.events != Events.FAKEPOINTS) {
                    logic.pointTouched(playerId, logic.state.MpointState1);
                    logic.pointTouched(playerId, logic.state.MpointState2);
                }
                logic.bombTouched(playerId,logic.state.bombState1);
                logic.bombTouched(playerId,logic.state.bombState2);
                logic.bombTouched(playerId,logic.state.bombState3);
                logic.updateGameTimer();
                logic.morePoints();
                logic.bombs();

                /*Ok so if i understand right, snapshot is a copy of the gamestate from server sent to the client
                //First we send gamestate to logic where its changed accordingly.
                //Logic gets the input and knowing what thread and game it is, it changes the correct player position
                //Logic sends it here where the current state is copied and only thing changed
                //Is the local player id, which is sent back*/
                GameState snapshot;
                synchronized (logic) {
                    snapshot = logic.state.copy(); // you must implement this
                }

                snapshot.localPlayerId = playerId;

                out.reset();
                out.writeObject(snapshot);
                out.flush();

            }
        } catch (Exception e) {
            if (logic != null){
                logic.setOffline(playerId);
                logic.state.roomPlayerOnlineList.set(playerId,false);

                int toRemove = 0;
                for (int i = 0; i < logic.state.roomPlayerOnlineList.size(); i++){
                    if (logic.state.roomPlayerOnlineList.get(i) == false){
                        toRemove++;
                    }
                }
                System.out.println(toRemove);
                if (toRemove == 5){
                    lobbyManager.removeLobby(lobbyName);
                }
                System.out.println("Player " + playerId + " left.");
            }
        }
    }


}