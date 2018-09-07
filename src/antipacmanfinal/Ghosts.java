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
    private Map map;
    private Pathfinder pathFinder;
    private int checkV = 1;
    
    private Image image;
    private ImageView iv;
    private Color color;
    private static DropShadow glow;
    
    private Movement mGhost = Movement.NONE;
    private Movement randomMoveGhost = Movement.NONE;
    private Movement prevMove = Movement.NONE;
    
    public Ghosts(int x, int y, Group root, Color c, Game game) throws FileNotFoundException {
        this.locX = x;
        this.locY = y;
        this.map = game.getMap();
        this.pathFinder = game.getPathFinder();
        this.color = c;
        
        if (c == Color.CYAN) image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/cyan.png"));
        else if (c == Color.RED) image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/red.png"));
        else if (c == Color.ORANGE) image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/orange.png"));
        else image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/pink.png"));
        iv = new ImageView(image);
        iv.setX((locX * map.getBLOCK_SIZE()) + (map.getBLOCK_SIZE()/5));
        iv.setY((locY * map.getBLOCK_SIZE()) + (map.getBLOCK_SIZE()/5));
        iv.setFitHeight(map.getBLOCK_SIZE()/1.5);
        iv.setFitWidth(map.getBLOCK_SIZE()/1.5);
        iv.setPreserveRatio(true);
        glow = new DropShadow(map.getBLOCK_SIZE(), locX-10, locY-5, Color.AZURE);
        glow.setSpread(.6);
        root.getChildren().add(iv);
    }
    
    public ImageView getIv() {
        return iv;
    }
    
    public int getLocX() {
        return locX;
    }
    
    public int getLocY() {
        return locY;
    }
    
    public Color getColor() {
        return color;
    }
    
    public static DropShadow getGlow() {
        return glow;
    }
    
    public Movement getmGhost() {
        return mGhost;
    }
    
    public Movement getRandomMoveGhost() {
        return randomMoveGhost;
    }
    
    public Movement getPreMove() {
        return prevMove;
    }
    
    public void setmGhost(Movement mGhost) {
        this.mGhost = mGhost;
    }
    
    public int getCheckV() {
        return checkV;
    }
    
    public void setUnvisible(){
        this.iv.setVisible(false);
        this.checkV = 0;
    }
    
    
    /**
     * This method is used to check if ghost colide or not
     *
     * @param m This is the Movement parameter for giving a direction
     * @return boolean
     */
    public boolean checkColGhost(Movement m) {
        if(this.locX <= 0  && m.dx == -1){
            return false;
        }else if(this.locX >= map.getGRID_LENGTH()-1 && m.dx == 1){
            return false;
        }
        if(!map.isWall(this.getLocX() + m.dx, this.getLocY() + m.dy)) {
            return false;
        }
        return true;
    }
    
    /**
     * This method is used to move ghosts
     *
     * @param m This is the Movement parameter for giving a direction
     */
    public void moveGhost(Movement m) {
        if(this.checkV == 0){
            return;
        }
        if (this.getLocX() == 0 && m == Movement.LEFT) {
            iv.setTranslateX(iv.getTranslateX() + (map.getGRID_LENGTH()) * map.getBLOCK_SIZE());
            this.locX = map.getGRID_LENGTH();
        } else if (this.getLocX() == map.getGRID_LENGTH() - 1 && m == Movement.RIGHT) {
            iv.setTranslateX(iv.getTranslateX() - (map.getGRID_LENGTH()) * map.getBLOCK_SIZE());
            this.locX = -1;
        }
        if (!checkColGhost(m)) {
            iv.setTranslateY(iv.getTranslateY() + (m.dy) * (map.getBLOCK_SIZE()));
            this.locY = this.getLocY() + m.dy;
            iv.setTranslateX(iv.getTranslateX() + (m.dx) * (map.getBLOCK_SIZE()));
            this.locX = this.getLocX() + m.dx;
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
        ArrayList<Movement> moves = new ArrayList<>();
        moves.add(Movement.DOWN);
        moves.add(Movement.UP);
        moves.add(Movement.LEFT);
        moves.add(Movement.RIGHT);
        prevMove = randomMoveGhost;
        randomMoveGhost = Movement.NONE;
        while (randomMoveGhost == Movement.NONE) {
            Collections.shuffle(moves);
            if(pathFinder.checkNEWS(this.locX, this.locY) == Status._2WAY_V
                    || (pathFinder.checkNEWS(this.locX, this.locY) == Status._2WAY_H)){
                randomMoveGhost = prevMove;
            }else{
                for (int i = 0; i < moves.size(); i++) {
                    if (prevMove != moves.get(i)
                            && !checkColGhost(moves.get(i))) {
                        randomMoveGhost = moves.get(i);
                    }
                }
            }
        }
    }
    
    /**
     * This method is used to activate hollow mode
     *
     */
    public void HollowMode() throws FileNotFoundException{
        this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/hollow.png")));
    }
    
    /**
     * This method is used to reset hollow mode
     *
     */
    public void resetColor() throws FileNotFoundException{
        if (color == Color.CYAN) this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/cyan.png")));
        else if (color == Color.RED) this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/red.png")));
        else if (color == Color.ORANGE) this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/orange.png")));
        else this.iv.setImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/pink.png")));
    }
    
}
