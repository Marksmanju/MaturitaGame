package org.example.otherstuff;

public class GameLogic {
    public Game game;
    public Player player;
    public PlayerTwo playerTwo;
    public int steps = 5;
    public GameLogic(Game game) {
        this.game = game;

        this.player = null;
        this.playerTwo = null;
    }

    public void initialize(){
        player = new Player(50, 50);
        playerTwo = new PlayerTwo(100,100);
    }
    public void update() {
        movePlayer();
    }
    public void movePlayer(){
        if(game.upPressed == true){
            player.setY(player.getY() - steps);
        }else if(game.downPressed == true){
            player.setY(player.getY() + steps);
        }else if(game.leftPressed == true){
            player.setX(player.getX() - steps);
        }else if(game.rightPressed == true){
            player.setX(player.getX() + steps);
        }
    }
}
