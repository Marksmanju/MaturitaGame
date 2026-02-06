package org.example.otherstuff.serverstuff;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class ServerThread implements Runnable {
    private final Socket clientSocket;
    private final String ip;
    private final int port;

    public ServerThread(Socket client, String ip, int port) {
        this.clientSocket = client;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try(clientSocket;
            var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            var out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            out.println("Vítej na serveru! Použij echo <text> nebo quit pro komunikaci.");
            String line;

            while ((line = in.readLine())!= null){
                line = line.trim();
                if (line.equalsIgnoreCase("quit")){
                    System.out.println("Klient " + ip + ":" + port + " - Bye");
                    out.println("Bye!");
                    break;
                } else if (line.startsWith("echo ")){
                    System.out.println("Klient " + ip + ":" + port + " zadal " + line.substring(5));
                    out.println(line.substring(5));
                } else {
                    System.out.println("Proč se to jako dostalo sem?");
                    out.println("Neznámý vstup! Použij echo <text> nebo quit pro komunikaci");
                }
            }

        } catch (SocketTimeoutException e){
            System.out.println("Došlo k timeoutu po asi jedné minutě, pokud to není v serveru nastaveno jinak");
            System.exit(1);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}





















