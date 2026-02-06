package org.example.multigame.client;

import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public GameClient(String ip) throws IOException {
        Socket socket = new Socket(ip, 5555);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendInput(PlayerInput input) throws IOException {
        out.writeObject(input);
        out.flush();
    }

    public GameState receiveState() throws IOException, ClassNotFoundException {
        return (GameState) in.readObject();
    }
}