package org.example.otherstuff.serverstuff;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(5555)){
            serverSocket.setReuseAddress(true);
            System.out.println("Server běží na portu 5555...");

            while(true){
                Socket client = serverSocket.accept();

                InetSocketAddress remote = (InetSocketAddress) client.getRemoteSocketAddress();
                String ip = remote.getAddress().getHostAddress();
                int port = remote.getPort();

                System.out.printf("Klient %s:%d připojen%n",ip,port);
                client.setSoTimeout(60_000);
                
                pool.execute(new ServerThread(client, ip, port));
            }
        } finally {
            pool.shutdown();
        }
    }
}





























