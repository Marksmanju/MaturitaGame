package org.example.multigame.server;

import org.example.multigame.client.Game;
import org.example.multigame.lobby.LobbyManager;
import org.example.multigame.shared.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer extends JFrame {

    public GameServer() throws HeadlessException, IOException {

        new DiscoveryServer().start();

        ServerSocket serverSocket = new ServerSocket(5555);
        LobbyManager lobbyManager = new LobbyManager(); // One manager for the whole server

        System.out.println("Server started. Waiting for players...");

        JLabel label = new JLabel("SERVER IS RUNNING");
        JButton button = new JButton("EXIT SERVER");

        button.setBounds(50,100,200,50);

        add(button);
        add(label);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setTitle("Server");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        while (true) {
            Socket socket = serverSocket.accept();
            // Pass the lobbyManager instead of a specific logic instance
            new ClientHandler(socket, lobbyManager).start();
        }
    }

    public static void main(String[] args) throws Exception {
        new GameServer();
    }
}