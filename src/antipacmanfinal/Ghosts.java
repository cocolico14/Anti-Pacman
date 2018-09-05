/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package antipacmanfinal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author soheil
 */
public class Ghosts {
    
    private int locX, locY;
    private int checkV = 1;
    
    private Circle ghost;
    private Image image;
    private ImageView iv;
    private Color color;
    private static DropShadow glow;
    
    private Pathfinder.Movement mGhost = Pathfinder.Movement.NONE;
    private Pathfinder.Movement randomMoveGhost = Pathfinder.Movement.NONE;
    
    public Ghosts(int x, int y, Group root, Color c) throws FileNotFoundException {
        locX = x;
        locY = y;
        color = c;
        ghost = new Circle((locX * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE()/2), (locY * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE()/2), (Map.getBLOCK_SIZE()/2));
        if (c == Color.CYAN) image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/cyan.png"));
        else if (c == Color.RED) image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/red.png"));
        else if (c == Color.ORANGE) image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/orange.png"));
        else image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/pink.png"));
        iv = new ImageView(image);
        iv.setX((locX * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE()/5));
        iv.setY((locY * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE()/5));
        glow = new DropShadow(Map.getBLOCK_SIZE(), locX-10, locY-5, Color.AZURE);
        glow.setSpread(.6);
        ghost.setFill(c);
        //root.getChildren().add(ghost);
        root.getChildren().add(iv);
    }
    
    public ImageView getIv() {
        return iv;
    }
    
    
    public int getLocX() {
        return locX;
    }
    
    public void setLocX(int locX) {
        this.locX = locX;
    }
    
    public int getLocY() {
        return locY;
    }
    
    public void setLocY(int locY) {
        this.locY = locY;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public Circle getGhost() {
        return ghost;
    }
    
    public void setGhost(Circle ghost) {
        this.ghost = ghost;
    }
    
    public static DropShadow getGlow() {
        return glow;
    }
    
    public Pathfinder.Movement getmGhost() {
        return mGhost;
    }
    
    public Pathfinder.Movement getRandomMoveGhost() {
        return randomMoveGhost;
    }
    
    public void setmGhost(Pathfinder.Movement mGhost) {
        this.mGhost = mGhost;
    }
    
    public void setRandomMoveGhost(Pathfinder.Movement randomMoveGhost) {
        this.randomMoveGhost = randomMoveGhost;
    }
    
    public int getCheckV() {
        return checkV;
    }
    
    public void setUnvisible(){
        this.ghost.setVisible(false);
        this.iv.setVisible(false);
        this.checkV = 0;
    }
    
    
    /**
     * This method is used to check if ghost colide or not
     *
     * @param m This is the Movement parameter for giving a direction
     * @return boolean
     */
    public boolean checkColGhost(Pathfinder.Movement m) {
        if(this.checkV == 0){
            return true;
        }
        if(this.locX < 0  && m.dx == -1){
            return false;
        }else if(this.locX > Map.getGRID_LENGTH()-1 && m.dx == 1){
            return false;
        }
        if(!Map.isWall(this.getLocX() + m.dx, this.getLocY() + m.dy)) {
            return false;
        }
        return true;
    }
    
    /**
     * This method is used to move ghosts
     *
     * @param m This is the Movement parameter for giving a direction
     */
    public void moveGhost(Pathfinder.Movement m) {
        if(this.checkV == 0){
            return;
        }
        if (this.getLocX() == 0 && m == Pathfinder.Movement.LEFT) {
            ghost.setTranslateX(ghost.getTranslateX() + (Map.getGRID_LENGTH()) * Map.getBLOCK_SIZE());
            iv.setTranslateX(ghost.getTranslateX() + (Map.getGRID_LENGTH()) * Map.getBLOCK_SIZE());
            this.setLocX(Map.getGRID_LENGTH());
        } else if (this.getLocX() == Map.getGRID_LENGTH() - 1 && m == Pathfinder.Movement.RIGHT) {
            ghost.setTranslateX(ghost.getTranslateX() - (Map.getGRID_LENGTH()) * Map.getBLOCK_SIZE());
            iv.setTranslateX(ghost.getTranslateX() - (Map.getGRID_LENGTH()) * Map.getBLOCK_SIZE());
            this.setLocX(-1);
        }
        if (!checkColGhost(m)) {
            ghost.setTranslateY(ghost.getTranslateY() + (m.dy) * 40);
            iv.setTranslateY(ghost.getTranslateY() + (m.dy) * (0.01));
            this.setLocY(this.getLocY() + m.dy);
            ghost.setTranslateX(ghost.getTranslateX() + (m.dx) * 40);
            iv.setTranslateX(ghost.getTranslateX() + (m.dx) * (0.01));
            this.setLocX(this.getLocX() + m.dx);
        }
    }
    
    /**
     * This method is used to give random direction
     *
     */
    public void randomGhost() {
        if(this.checkV == 0){
            return;
        }
        int generator = 0;
        ArrayList<Pathfinder.Movement> moves = new ArrayList<>();
        moves.add(Pathfinder.Movement.DOWN);
        moves.add(Pathfinder.Movement.UP);
        moves.add(Pathfinder.Movement.LEFT);
        moves.add(Pathfinder.Movement.RIGHT);
        randomMoveGhost = Pathfinder.Movement.NONE;
        while (randomMoveGhost == Pathfinder.Movement.NONE) {
            Collections.shuffle(moves);
            if (!checkColGhost(moves.get(0))) {
                randomMoveGhost = moves.get(0);
            }
        }
    }
    
    /**
     * This method is used to activate hollow mode
     *
     */
    public void HollowMode() throws FileNotFoundException{
        this.ghost.setFill(Color.LIGHTGRAY);
        this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/hollow.png")));
    }
    
    /**
     * This method is used to reset hollow mode
     *
     */
    public void resetColor() throws FileNotFoundException{
        this.ghost.setFill(color);
        if (color == Color.CYAN) this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/cyan.png")));
        else if (color == Color.RED) this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/red.png")));
        else if (color == Color.ORANGE) this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/orange.png")));
        else this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/pink.png")));
    }
    
}
