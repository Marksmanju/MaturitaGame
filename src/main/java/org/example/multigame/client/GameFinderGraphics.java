package org.example.multigame.client;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GameFinderGraphics extends JPanel {
    public String serverIp;
    public ArrayList<String> lobbyList;
    ImageIcon ii = new ImageIcon(getClass().getResource("/" + "Back.gif"));
    ImageIcon findImage = new ImageIcon(getClass().getResource("/" + "Back.gif"));
    public GameFinderGraphics(String serverIp) {
        this.serverIp = serverIp;
        this.lobbyList = fetchLobbies(serverIp);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(255, 255, 255));
        g.fillRect(0,0,1000,1000);

        g.setColor(Color.black);
        g.drawRect(1,1,150,500);

        /*g.setColor(new Color(255, 255, 255));
        g.fillRect(200,200,300,300);*/

        g.drawImage(findImage.getImage(),250, 250, 200, 200,this);



        //g.drawImage(ii.getImage(),0,0,500,500,this);
    }
    private static ArrayList<String> fetchLobbies(String ip) {
        try (Socket socket = new Socket(ip, 5555);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("GET_LOBBIES");
            out.flush();
            return (ArrayList<String>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // Return empty if server is down
        }
    }

    public ImageIcon getFindImage() {
        return findImage;
    }

    public void setFindImage(ImageIcon findImage) {
        this.findImage = findImage;
    }
}
