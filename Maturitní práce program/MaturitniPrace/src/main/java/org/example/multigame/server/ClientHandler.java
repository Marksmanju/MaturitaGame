package org.example.multigame.server;

import org.example.multigame.bullshit.GameSession;
import org.example.multigame.lobby.LobbyManager;
import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    //private static int idCounter = 1;
    private LobbyManager lobbyManager;

    public int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerGameLogic logic;
    //private GameSession session;

    public ClientHandler(Socket socket, LobbyManager manager) throws IOException {
        //this.logic = logic;
        //this.playerId = nextId();
        this.lobbyManager = manager;


        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Player " + playerId + " connected");

    }
    /*private static synchronized int nextId() {
        return idCounter++;
    }*/
    public void run() {
                /*Ok so if i understand right, snapshot is a copy of the gamestate from server sent to the client
                //First we send gamestate to logic where its changed accordingly.
                //Logic gets the input and knowing what thread and game it is, it changes the correct player position
                //Logic sends it here where the current state is copied and only thing changed
                //Is the local player id, which is sent back*/

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
            String lobbyName = (String) firstObject;

            this.logic = lobbyManager.getOrCreateLobby(lobbyName);

            synchronized (logic) {
                this.playerId = logic.getNextPlayerId();
            }

            out.writeInt(this.playerId); // Send the ID (or -1) back
            out.flush();

            if (this.playerId == -1) {
                System.out.println("Lobby " + lobbyName + " is full.");
                return; // Or send a "Lobby Full" message back
            }

            System.out.println("Player " + playerId + " joined lobby: " + lobbyName);

            // PHASE 2: Game Loop (Existing Logic)
            while (true) {
                PlayerInput input = (PlayerInput) in.readObject();
                logic.applyInput(input, playerId);
                logic.setOnline(playerId);
                logic.updateTimer();
                logic.updateProjectiles();
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
            if (logic != null) logic.setOffline(playerId);
        }
    }


}