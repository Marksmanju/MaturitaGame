package org.example.multigame.server;

import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5555);
        ServerGameLogic logic = new ServerGameLogic();

        System.out.println("Server started on port 5555");

        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket, logic).start();
        }
    }
}