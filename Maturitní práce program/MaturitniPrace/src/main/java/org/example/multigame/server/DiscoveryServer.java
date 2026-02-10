package org.example.multigame.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DiscoveryServer extends Thread {
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);
            System.out.println("Discovery Server active...");

            while (true) {
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet); // Wait for a client to shout "WHERE IS THE SERVER?"

                String message = new String(packet.getData()).trim();
                if (message.equals("DISCOVER_GAME_SERVER")) {
                    byte[] sendData = "I_AM_THE_SERVER".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            packet.getAddress(), packet.getPort());
                    socket.send(sendPacket); // Shout back: "I'm right here!"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}