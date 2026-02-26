package org.example.multigame.server;

import org.example.multigame.lobby.LobbyManager;

import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) throws Exception {
        new DiscoveryServer().start();

        ServerSocket serverSocket = new ServerSocket(5555);
        LobbyManager lobbyManager = new LobbyManager(); // One manager for the whole server

        System.out.println("Server started. Waiting for players...");

        while (true) {
            Socket socket = serverSocket.accept();
            // Pass the lobbyManager instead of a specific logic instance
            new ClientHandler(socket, lobbyManager).start();
        }
    }
}