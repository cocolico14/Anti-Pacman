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
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author soheil
 */
public class Pacman {
    
    private int locX, locY;
    private Map map;
    private Pathfinder pathFinder;
    private Ghosts ghosts[];
    
    private Image image;
    private ImageView iv;
    
    private Movement MovePacman = Movement.NONE;
    private Movement prevMove = Movement.NONE;
    
    final TranslateTransition transition;
    
    public Pacman(int x, int y, Group root, Game game) throws FileNotFoundException {
        this.locX = x;
        this.locY = y;
        this.map = game.getMap();
        this.pathFinder = game.getPathFinder();
        this.ghosts = game.getGhosts();
        
        transition = new TranslateTransition(Duration.millis(500), root);
        
        image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/pacmanO.png"));
        iv = new ImageView(image);
        iv.setX((locX * map.getBLOCK_SIZE()) + (map.getBLOCK_SIZE()/5));
        iv.setY((locY * map.getBLOCK_SIZE()) + (map.getBLOCK_SIZE()/5));
        iv.setFitHeight(map.getBLOCK_SIZE()/1.5);
        iv.setFitWidth(map.getBLOCK_SIZE()/1.5);
        iv.setPreserveRatio(true);
        root.getChildren().add(iv);
    }
    
    public int getLocX() {
        return locX;
    }
    
    public int getLocY() {
        return locY;
    }
    
    public Movement getMovePacman() {
        return MovePacman;
    }
    
    /**
     * This method is used to check if pacman colide or not
     *
     * @param m This is the Movement parameter to give direction
     * @return Boolean.
     */
    public boolean checkColPacman(Movement m) {
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
    
    public void movePacman(Movement m) {
        
        
        if (this.getLocX() == 0 && m == Movement.LEFT) {
            iv.setTranslateX(iv.getTranslateX() + (map.getGRID_LENGTH()) * map.getBLOCK_SIZE());
            this.locX = map.getGRID_LENGTH();
        } else if (this.getLocX() == map.getGRID_LENGTH() - 1 && m == Movement.RIGHT) {
            iv.setTranslateX(iv.getTranslateX() - (map.getGRID_LENGTH()) * map.getBLOCK_SIZE());
            this.locX = -1;
        }
        if (!checkColPacman(m)) {
            iv.setTranslateY(iv.getTranslateY() + (m.dy) * map.getBLOCK_SIZE());
            this.locY = this.getLocY() + m.dy;
            iv.setTranslateX(iv.getTranslateX() + (m.dx) *  map.getBLOCK_SIZE());
            this.locX = this.getLocX() + m.dx;
        }
    }
    
    
    /**
     * This method is used to move pacman with AI
     *
     * @param isHollow
     */
    public void AI(boolean isHollow) {
        int[][] dis = new int[4][];
        int distThr = 3;
        if(isHollow) distThr = 6;
        prevMove = MovePacman;
        MovePacman = Movement.NONE;
        for (int i = 0; i < 4; i++) {
            dis[i] = distance(ghosts[i]);
        }
        switch (pathFinder.checkNEWS(this.locX, this.locY)) {
            case _3WAY_RUD: //|- 3 way (up, down, right)
                for (int i = 0; i < 4; i++) {
                    if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] > 0 && dis[i][1] < distThr){ // pacman is under the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.RIGHT; //run away
                            }else{
                                MovePacman = Movement.DOWN; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.UP; //eat ghost
                            }
                        }else if(dis[i][1] < 0 && dis[i][1] > -distThr){ // pacman is above the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.RIGHT; //run away
                            }else{
                                MovePacman = Movement.UP; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.DOWN; //eat ghost
                            }
                        }
                    }else if(dis[i][1] == 0){ // pacman has the same Y cord as i th ghost
                        if(dis[i][0] < 0 && dis[i][0] > -distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Movement.UP;
                            }else{
                                MovePacman = Movement.DOWN;
                            }
                            if(isHollow){
                                MovePacman = Movement.RIGHT; //eat ghost
                            }
                        }
                    }
                }
                break;
            case _3WAY_LUD: // -| 3 way (up, down, left)
                for (int i = 0; i < 4; i++) {
                    if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] > 0 && dis[i][1] < distThr){ // pacman is under the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.LEFT; //run away
                            }else{
                                MovePacman = Movement.DOWN; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.UP; //eat ghost
                            }
                        }else if(dis[i][1] < 0 && dis[i][1] > -distThr){ // pacman is above the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.LEFT; //run away
                            }else{
                                MovePacman = Movement.UP; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.DOWN; //eat ghost
                            }
                        }
                    }else if(dis[i][1] == 0){ // pacman has the same Y cord as i th ghost
                        if(dis[i][0] > 0 && dis[i][0] < distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Movement.UP;
                            }else{
                                MovePacman = Movement.DOWN;
                            }
                            if(isHollow){
                                MovePacman = Movement.LEFT; //eat ghost
                            }
                        }
                    }
                }
                break;
            case _3WAY_DRL: // -|- 3 way (down, right, left)
                for (int i = 0; i < 4; i++) {
                    if(dis[i][1] == 0){ // pacman has the same Y cord as i th ghost
                        if(dis[i][0] > 0 && dis[i][0] < distThr){ // pacman is in right side of the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.DOWN; //run away
                            }else{
                                MovePacman = Movement.RIGHT; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.LEFT; //eat ghost
                            }
                        }else if(dis[i][0] < 0 && dis[i][0] > -distThr){ // pacman is in left side of the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.DOWN; //run away
                            }else{
                                MovePacman = Movement.LEFT; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.RIGHT; //eat ghost
                            }
                        }
                    }else if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] < 0 && dis[i][1] > -distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Movement.RIGHT;
                            }else{
                                MovePacman = Movement.LEFT;
                            }
                            if(isHollow){
                                MovePacman = Movement.DOWN; //eat ghost
                            }
                        }
                    }
                }
                break;
            case _3WAY_URL: // // _|_ 3 way (up, right, left)
                for (int i = 0; i < 4; i++) {
                    if(dis[i][1] == 0){ // pacman has the same Y cord as i th ghost
                        if(dis[i][0] > 0 && dis[i][0] < distThr){ // pacman is in right side of the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.UP; //run away
                            }else{
                                MovePacman = Movement.RIGHT; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.LEFT; //eat ghost
                            }
                        }else if(dis[i][0] < 0 && dis[i][0] > -distThr){ // pacman is in left side of the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Movement.UP; //run away
                            }else{
                                MovePacman = Movement.LEFT; //run away
                            }
                            if(isHollow){
                                MovePacman = Movement.RIGHT; //eat ghost
                            }
                        }
                    }else if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] > 0 && dis[i][1] < distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Movement.RIGHT;
                            }else{
                                MovePacman = Movement.LEFT;
                            }
                            if(isHollow){
                                MovePacman = Movement.UP; //eat ghost
                            }
                        }
                    }
                }
                break;
            case _2WAY_UR: // |_ 2 way (up, right)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) { // same Y and near ghost
                        MovePacman = Movement.UP;
                        if(isHollow){
                            MovePacman = Movement.RIGHT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) { // same X and near ghost
                        MovePacman = Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Movement.UP;
                        }
                    }
                }
                break;
            case _2WAY_UL: // _| 2 way (up, left)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) {
                        MovePacman = Movement.UP;
                        if(isHollow){
                            MovePacman = Movement.RIGHT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) {
                        MovePacman = Movement.LEFT;
                        if(isHollow){
                            MovePacman = Movement.UP;
                        }
                    }
                }
                break;
            case _2WAY_DR: // |- 2 way (down, right)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) {
                        MovePacman = Movement.DOWN;
                        if(isHollow){
                            MovePacman = Movement.RIGHT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) {
                        MovePacman = Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Movement.DOWN;
                        }
                    }
                }
                break;
            case _2WAY_DL: // -| 2 way (down, left)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) {
                        MovePacman = Movement.DOWN;
                        if(isHollow){
                            MovePacman = Movement.LEFT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) {
                        MovePacman = Movement.LEFT;
                        if(isHollow){
                            MovePacman = Movement.DOWN;
                        }
                    }
                }
                break;
            case _4WAY: // 4 way
                for (int i = 0; i < 4; i++) {
                    if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) { // same X and ghost coming from left
                        MovePacman = Movement.DOWN;
                        if(isHollow){
                            MovePacman = Movement.LEFT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) { // same X and ghost comming from right
                        MovePacman = Movement.UP;
                        if(isHollow){
                            MovePacman = Movement.RIGHT;
                        }
                    } else if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) { // same Y and ghost comming from above
                        MovePacman = Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Movement.UP;
                        }
                    } else if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) { // same Y and ghost comming from below
                        MovePacman = Movement.LEFT;
                        if(isHollow){
                            MovePacman = Movement.DOWN;
                        }
                    } else if (dis[i][1] == 0 && dis[i][0] <= -map.getGRID_LENGTH() + 1){ // out of bound cases
                        MovePacman = Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Movement.LEFT;
                        }
                    } else if (dis[i][1] == 0 && dis[i][0] >= map.getGRID_LENGTH() - 1){ // out of bound cases
                        MovePacman = Movement.LEFT;
                        if(isHollow){
                            MovePacman = Movement.RIGHT;
                        }
                    }
                }
                break;
            case _2WAY_H:
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) {
                        MovePacman = Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Movement.LEFT;
                        }
                    } else if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) {
                        MovePacman = Movement.LEFT;
                        if(isHollow){
                            MovePacman = Movement.RIGHT;
                        }
                    } 
                }
                break;
            case _2WAY_V:
                for (int i = 0; i < 4; i++) {
                    if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) {
                        MovePacman = Movement.UP;
                        if(isHollow){
                            MovePacman = Movement.DOWN;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) {
                        MovePacman = Movement.DOWN;
                        if(isHollow){
                            MovePacman = Movement.UP;
                        }
                    }
                }
            default:
                break;
        }
        System.out.println(dis[0][0] + " ; " + map.isWall(this.locX, this.locY+1));
        if(MovePacman == Movement.NONE){
            dotFinder();
        }
    }
    
    /**
     * This method is used to give distance between ghost and pacman
     *
     */
    private int[] distance(Ghosts g) {
        int[] result = new int[2];
        result[0] = this.locX - g.getLocX();
        result[1] = this.locY - g.getLocY();
        if(g.getCheckV() == 0) {
            result[0] = 1000;
            result[1] = 1000;
        }
        return result;
    }
    
    /**
     * This method is used to give distance between point i,j and pacman
     *
     */
    private int[] distanceIJ(int x, int y) {
        int[] result = new int[2];
        result[0] = this.locX - x;
        result[1] = this.locY - y;
        return result;
    }
    
    /**
     * This method is used to go after dots
     *
     */
    private void dotFinder() {
        if(this.locX > 0 && this.locX < map.getGRID_LENGTH()-1){
            if ((map.getBoard().get(new Pair(this.locY + 1, this.locX)) == '-'
                    || map.getBoard().get(new Pair(this.locY + 1, this.locX)) == '^')){
                MovePacman = Movement.DOWN;
            } else if ((map.getBoard().get(new Pair(this.locY - 1, this.locX)) == '-'
                    || map.getBoard().get(new Pair(this.locY - 1, this.locX)) == '^')){
                MovePacman = Movement.UP;
            } else if ((map.getBoard().get(new Pair(this.locY, this.locX + 1)) == '-'
                    || map.getBoard().get(new Pair(this.locY, this.locX + 1)) == '^')){
                MovePacman = Movement.RIGHT;
            } else if ((map.getBoard().get(new Pair(this.locY, this.locX - 1)) == '-'
                    || map.getBoard().get(new Pair(this.locY, this.locX - 1)) == '^')){
                MovePacman = Movement.LEFT;
            }else {
                searchDot();
            }
        } else {
            searchDot();
        }
        changeFaceDir(MovePacman);
    }
    
    public void searchDot(){
        for (int i = 0; i < map.getGRID_LENGTH(); i++) {
            for (int j = 0; j < map.getGRID_HEIGHT(); j++) {
                if(map.getBoard().containsKey(new Pair(i, j))
                        && (map.getBoard().get(new Pair(i, j)) == '-'
                        || map.getBoard().get(new Pair(i, j)) == '^')){
                    if(distanceIJ(i, j)[0] == 0){
                        if(distanceIJ(i, j)[1] > 0
                                && !checkColPacman(Movement.UP)){
                            MovePacman = Movement.UP;
                        }else if (!checkColPacman(Movement.DOWN)){
                            MovePacman = Movement.DOWN;
                        }
                    }else if(distanceIJ(i, j)[1] == 0){
                        if(distanceIJ(i, j)[0] > 0
                                && !checkColPacman(Movement.LEFT)){
                            MovePacman = Movement.LEFT;
                        }else if (!checkColPacman(Movement.RIGHT)){
                            MovePacman = Movement.RIGHT;
                        }
                    }else{
                        randomPacman();
                    }
                    break;
                }
            }
        }
    }
    
    public void randomPacman(){
        ArrayList<Movement> moves = new ArrayList<>();
        moves.add(Movement.DOWN);
        moves.add(Movement.UP);
        moves.add(Movement.LEFT);
        moves.add(Movement.RIGHT);
        MovePacman = Movement.NONE;
        while (MovePacman == Movement.NONE) {
            Collections.shuffle(moves);
            if(pathFinder.checkNEWS(this.locX, this.locY) == Status._2WAY_V
                    || (pathFinder.checkNEWS(this.locX, this.locY) == Status._2WAY_H)
                    || (locX == 0 || locX == map.getGRID_LENGTH()-1)){
                MovePacman = prevMove;
            }else{
                for (int i = 0; i < moves.size(); i++) {
                    if (prevMove != moves.get(i)
                            && !checkColPacman(moves.get(i))) {
                        MovePacman = moves.get(i);
                    }
                }
            }
        }
    }
    
    
    public void changeFaceDir(Movement m){
        switch(m){
                case UP:
                    iv.setRotate(90);
                    break;
                case DOWN:
                    iv.setRotate(-90);
                    break;
                case RIGHT:
                    iv.setRotate(180);
                    break;
                case LEFT:
                    iv.setRotate(0);
                    break;
                default:
                    break;
        }
    }
    
}
