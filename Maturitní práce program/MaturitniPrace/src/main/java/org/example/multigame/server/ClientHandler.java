package org.example.multigame.server;

import org.example.multigame.bullshit.GameSession;
import org.example.multigame.bullshit.Lobby;
import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private static int idCounter = 1;

    public int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerGameLogic logic;
    private Lobby lobby;
    private GameSession session;

    public ClientHandler(Socket socket, ServerGameLogic logic) throws IOException {
        this.logic = logic;
        this.playerId = nextId();


        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Player " + playerId + " connected");

    }
    private static synchronized int nextId() {
        return idCounter++;
    }
    public void run() {
        try {
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
            System.out.println("Player " + playerId + " disconnected");
            logic.setOffline(playerId);
        }
    }


}