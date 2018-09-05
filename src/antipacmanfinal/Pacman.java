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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

/**
 *
 * @author soheil
 */
public class Pacman {
    
    private int locX, locY;
    
    private Circle pacman;
    private Image image;
    private ImageView iv;
    private Color c;
    
    private Pathfinder.Movement MovePacman = Pathfinder.Movement.NONE;
    private Pathfinder.Movement prevMove = Pathfinder.Movement.NONE;
    
    public Pacman(int x, int y, Group root, Color color) throws FileNotFoundException {
        this.locX = x;
        this.locY = y;
        c = color;
        
        pacman = new Circle((x * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE() / 2), (y * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE() / 2), (Map.getBLOCK_SIZE() / 2));
        new Image(new FileInputStream(System.getProperty("user.dir")+"/src/antipacmanfinal/images/pacmanO.png"));
        iv = new ImageView(image);
        iv.setX((locX * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE()/5));
        iv.setY((locY * Map.getBLOCK_SIZE()) + (Map.getBLOCK_SIZE()/5));
        pacman.setFill(c);
        root.getChildren().add(pacman);
        root.getChildren().add(iv);
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
    
    public Circle getPacman() {
        return pacman;
    }
    
    public void setPacman(Circle pacman) {
        this.pacman = pacman;
    }
    
    public Color getC() {
        return c;
    }
    
    public void setC(Color c) {
        this.c = c;
    }
    
    public Pathfinder.Movement getMovePacman() {
        return MovePacman;
    }
    
    /**
     * This method is used to check if pacman colide or not
     *
     * @param m This is the Movement parameter to give direction
     * @return Boolean.
     */
    public boolean checkColPacman(Pathfinder.Movement m) {
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
    
    public void movePacman(Pathfinder.Movement m) {
        if (this.getLocX() < 1 && m == Pathfinder.Movement.LEFT) {
            pacman.setTranslateX(pacman.getTranslateX() + (Map.getGRID_LENGTH() - 1) * Map.getBLOCK_SIZE());
            iv.setTranslateX(pacman.getTranslateX() + (Map.getGRID_LENGTH() - 1) * Map.getBLOCK_SIZE());
            this.setLocX(Map.getGRID_LENGTH() - 1);
        } else if (this.getLocX() > Map.getGRID_LENGTH() - 2 && m == Pathfinder.Movement.RIGHT) {
            pacman.setTranslateX(pacman.getTranslateX() - (Map.getGRID_LENGTH() - 1) * Map.getBLOCK_SIZE());
            iv.setTranslateX(pacman.getTranslateX() - (Map.getGRID_LENGTH() - 1) * Map.getBLOCK_SIZE());
            this.setLocX(0);
        }
        if (!checkColPacman(m)) {
            pacman.setTranslateY(pacman.getTranslateY() + (m.dy) * Map.getBLOCK_SIZE());
            iv.setTranslateY(pacman.getTranslateY() + (m.dy) * Map.getBLOCK_SIZE());
            this.setLocY(this.getLocY() + m.dy);
            pacman.setTranslateX(pacman.getTranslateX() + (m.dx) * Map.getBLOCK_SIZE());
            iv.setTranslateX(pacman.getTranslateX() + (m.dx) * Map.getBLOCK_SIZE());
            this.setLocX(this.getLocX() + m.dx);
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
        MovePacman = Pathfinder.Movement.NONE;
        for (int i = 0; i < 4; i++) {
            dis[i] = distance(Game.ghosts[i]);
        }
        switch (Pathfinder.checkNEWS(this.locX, this.locY)) {
            case _3WAY_RUD: //|- 3 way (up, down, right)
                for (int i = 0; i < 4; i++) {
                    if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] > 0 && dis[i][1] < distThr){ // pacman is under the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Pathfinder.Movement.RIGHT; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.DOWN; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.UP; //eat ghost
                            }
                        }else if(dis[i][1] < 0 && dis[i][1] > -distThr){ // pacman is above the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Pathfinder.Movement.RIGHT; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.UP; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.DOWN; //eat ghost
                            }
                        }
                    }else if(dis[i][1] == 0){ // pacman has the same Y cord as i th ghost
                        if(dis[i][0] < 0 && dis[i][0] > -distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Pathfinder.Movement.UP;
                            }else{
                                MovePacman = Pathfinder.Movement.DOWN;
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.RIGHT; //eat ghost
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
                                MovePacman = Pathfinder.Movement.LEFT; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.DOWN; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.UP; //eat ghost
                            }
                        }else if(dis[i][1] < 0 && dis[i][1] > -distThr){ // pacman is above the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Pathfinder.Movement.LEFT; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.UP; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.DOWN; //eat ghost
                            }
                        }
                    }else if(dis[i][1] == 0){ // pacman has the same Y cord as i th ghost
                        if(dis[i][0] > 0 && dis[i][0] < distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Pathfinder.Movement.UP;
                            }else{
                                MovePacman = Pathfinder.Movement.DOWN;
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.LEFT; //eat ghost
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
                                MovePacman = Pathfinder.Movement.DOWN; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.RIGHT; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.LEFT; //eat ghost
                            }
                        }else if(dis[i][0] < 0 && dis[i][0] > -distThr){ // pacman is in left side of the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Pathfinder.Movement.DOWN; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.LEFT; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.RIGHT; //eat ghost
                            }
                        }
                    }else if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] < 0 && dis[i][1] > -distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Pathfinder.Movement.RIGHT;
                            }else{
                                MovePacman = Pathfinder.Movement.LEFT;
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.DOWN; //eat ghost
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
                                MovePacman = Pathfinder.Movement.UP; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.RIGHT; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.LEFT; //eat ghost
                            }
                        }else if(dis[i][0] < 0 && dis[i][0] > -distThr){ // pacman is in left side of the ghost
                            if(Math.random() > 0.2){ // random run away
                                MovePacman = Pathfinder.Movement.UP; //run away
                            }else{
                                MovePacman = Pathfinder.Movement.LEFT; //run away
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.RIGHT; //eat ghost
                            }
                        }
                    }else if(dis[i][0] == 0){ // pacman has the same X cord as i th ghost
                        if(dis[i][1] > 0 && dis[i][1] < distThr){ // pacman is near the ghost
                            if(Math.random() > 0.5){ // random run away
                                MovePacman = Pathfinder.Movement.RIGHT;
                            }else{
                                MovePacman = Pathfinder.Movement.LEFT;
                            }
                            if(isHollow){
                                MovePacman = Pathfinder.Movement.UP; //eat ghost
                            }
                        }
                    }
                }
                break;
            case _2WAY_UR: // |_ 2 way (up, right)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) { // same Y and near ghost
                        MovePacman = Pathfinder.Movement.UP;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.RIGHT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) { // same X and near ghost
                        MovePacman = Pathfinder.Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.UP;
                        }
                    }
                }
                break;
            case _2WAY_UL: // _| 2 way (up, left)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) {
                        MovePacman = Pathfinder.Movement.UP;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.RIGHT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) {
                        MovePacman = Pathfinder.Movement.LEFT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.UP;
                        }
                    }
                }
                break;
            case _2WAY_DR: // |- 2 way (down, right)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) {
                        MovePacman = Pathfinder.Movement.DOWN;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.RIGHT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) {
                        MovePacman = Pathfinder.Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.DOWN;
                        }
                    }
                }
                break;
            case _2WAY_DL: // -| 2 way (down, left)
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) {
                        MovePacman = Pathfinder.Movement.DOWN;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.LEFT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) {
                        MovePacman = Pathfinder.Movement.LEFT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.DOWN;
                        }
                    }
                }
                break;
            case _4WAY: // 4 way
                for (int i = 0; i < 4; i++) {
                    if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) { // same X and ghost coming from left
                        MovePacman = Pathfinder.Movement.DOWN;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.LEFT;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) { // same X and ghost comming from right
                        MovePacman = Pathfinder.Movement.UP;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.RIGHT;
                        }
                    } else if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) { // same Y and ghost comming from above
                        MovePacman = Pathfinder.Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.UP;
                        }
                    } else if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) { // same Y and ghost comming from below
                        MovePacman = Pathfinder.Movement.LEFT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.DOWN;
                        }
                    }
                }
                break;
            case _2WAY_H:
                for (int i = 0; i < 4; i++) {
                    if (dis[i][1] == 0 && (dis[i][0] > 0 && dis[i][0] < distThr)) {
                        MovePacman = Pathfinder.Movement.RIGHT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.LEFT;
                        }
                    } else if (dis[i][1] == 0 && (dis[i][0] < 0 && dis[i][0] > -distThr)) {
                        MovePacman = Pathfinder.Movement.LEFT;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.RIGHT;
                        }
                    }
                }
                break;
            case _2WAY_V:
                for (int i = 0; i < 4; i++) {
                    if (dis[i][0] == 0 && (dis[i][1] < 0 && dis[i][1] > -distThr)) {
                        MovePacman = Pathfinder.Movement.UP;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.DOWN;
                        }
                    } else if (dis[i][0] == 0 && (dis[i][1] > 0 && dis[i][1] < distThr)) {
                        MovePacman = Pathfinder.Movement.DOWN;
                        if(isHollow){
                            MovePacman = Pathfinder.Movement.UP;
                        }
                    }
                }
            default:
                break;
        }
        if(MovePacman == Pathfinder.Movement.NONE){
            dotFinder();
        }else{
            //System.out.println(Pathfinder.checkNEWS(this.locX, this.locY) + " AI !!! " + MovePacman);
        }
        //System.out.println(dis[0][0] + " : " + dis[0][1]);
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
        if(this.locX > 0 && this.locX < Map.getGRID_LENGTH()-1){
            if ((Game.map.getBoard().get(new Pair(this.locY + 1, this.locX)) == '-'
                    || Game.map.getBoard().get(new Pair(this.locY + 1, this.locX)) == '^')){
                MovePacman = Pathfinder.Movement.DOWN;
            } else if ((Game.map.getBoard().get(new Pair(this.locY - 1, this.locX)) == '-'
                    || Game.map.getBoard().get(new Pair(this.locY - 1, this.locX)) == '^')){
                MovePacman = Pathfinder.Movement.UP;
            } else if ((Game.map.getBoard().get(new Pair(this.locY, this.locX + 1)) == '-'
                    || Game.map.getBoard().get(new Pair(this.locY, this.locX + 1)) == '^')){
                MovePacman = Pathfinder.Movement.RIGHT;
            } else if ((Game.map.getBoard().get(new Pair(this.locY, this.locX - 1)) == '-'
                    || Game.map.getBoard().get(new Pair(this.locY, this.locX - 1)) == '^')){
                MovePacman = Pathfinder.Movement.LEFT;
            }else {
                searchDot();
            }
        } else {
            searchDot();
        }
        prevMove = MovePacman;
    }
    
    public void searchDot(){
        for (int i = 0; i < Map.getGRID_LENGTH(); i++) {
            for (int j = 0; j < Map.getGRID_HEIGHT(); j++) {
                if(Game.map.getBoard().containsKey(new Pair(i, j))
                        && (Game.map.getBoard().get(new Pair(i, j)) == '-'
                        || Game.map.getBoard().get(new Pair(i, j)) == '^')){
                    if(distanceIJ(i, j)[0] == 0){
                        if(distanceIJ(i, j)[1] > 0
                                && !checkColPacman(Pathfinder.Movement.UP)){
                            MovePacman = Pathfinder.Movement.UP;
                        }else if (!checkColPacman(Pathfinder.Movement.DOWN)){
                            MovePacman = Pathfinder.Movement.DOWN;
                        }
                    }else if(distanceIJ(i, j)[1] == 0){
                        if(distanceIJ(i, j)[0] > 0
                                && !checkColPacman(Pathfinder.Movement.LEFT)){
                            MovePacman = Pathfinder.Movement.LEFT;
                        }else if (!checkColPacman(Pathfinder.Movement.RIGHT)){
                            MovePacman = Pathfinder.Movement.RIGHT;
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
        ArrayList<Pathfinder.Movement> moves = new ArrayList<>();
        moves.add(Pathfinder.Movement.DOWN);
        moves.add(Pathfinder.Movement.UP);
        moves.add(Pathfinder.Movement.LEFT);
        moves.add(Pathfinder.Movement.RIGHT);
        MovePacman = Pathfinder.Movement.NONE;
        while (MovePacman == Pathfinder.Movement.NONE) {
            Collections.shuffle(moves);
            if(!checkColPacman(prevMove)
                    && Math.random() > 0.2){
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
    
//    public void HollowMode() {
//        if (Pathfinder.checkNEWS(this.locX, this.locY) == 8) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() < 0 && this.locX - Game.ghosts[i].getLocX() > -5)) {
//                    MovePacman = Pathfinder.Movement.DOWN;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() > 0 && this.locY - Game.ghosts[i].getLocY() < 5)) {
//                    MovePacman = Pathfinder.Movement.LEFT;
//                } else {
//                    dotFinder();
//                }
//            }
//        } else if (Pathfinder.checkNEWS(this.locX, this.locY) == 7) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() > 0 && this.locX - Game.ghosts[i].getLocX() < 5)) {
//                    MovePacman = Pathfinder.Movement.DOWN;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() > 0 && this.locY - Game.ghosts[i].getLocY() < 5)) {
//                    MovePacman = Pathfinder.Movement.RIGHT;
//                } else {
//                    dotFinder();
//                }
//            }
//        } else if (Pathfinder.checkNEWS(this.locX, this.locY) == 6) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() < 0 && this.locX - Game.ghosts[i].getLocX() > -5)) {
//                    MovePacman = Pathfinder.Movement.UP;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() < 0 && this.locY - Game.ghosts[i].getLocY() > -5)) {
//                    MovePacman = Pathfinder.Movement.LEFT;
//                } else {
//                    dotFinder();
//                }
//            }
//        } else if (Pathfinder.checkNEWS(this.locX, this.locY) == 5) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() > 0 && this.locX - Game.ghosts[i].getLocX() < 5)) {
//                    MovePacman = Pathfinder.Movement.UP;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() < 0 && this.locY - Game.ghosts[i].getLocY() > -5)) {
//                    MovePacman = Pathfinder.Movement.RIGHT;
//                } else {
//                    dotFinder();
//                }
//            }
//        } else if (Pathfinder.checkNEWS(this.locX, this.locY) == 4) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() > 0 && this.locY - Game.ghosts[i].getLocY() < 5)) {
//                    MovePacman = Pathfinder.Movement.UP;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() < 0 && this.locY - Game.ghosts[i].getLocY() > -5)) {
//                    MovePacman = Pathfinder.Movement.DOWN;
//                } else if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() > 0 && this.locX - Game.ghosts[i].getLocX() < 5)) {
//                    MovePacman = Pathfinder.Movement.LEFT;
//                } else if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() < 0 && this.locX - Game.ghosts[i].getLocX() > -5)) {
//                    MovePacman = Pathfinder.Movement.RIGHT;
//                } else {
//                    dotFinder();
//                }
//            }
//        } else if (Pathfinder.checkNEWS(this.locX, this.locY) == 3) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() > 0 && this.locY - Game.ghosts[i].getLocY() < 5) && !checkColPacman(Pathfinder.Movement.DOWN)) {
//                    MovePacman = Pathfinder.Movement.UP;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() < 0 && this.locY - Game.ghosts[i].getLocY() > -5) && !checkColPacman(Pathfinder.Movement.UP)) {
//                    MovePacman = Pathfinder.Movement.DOWN;
//                } else if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() > 0 && this.locX - Game.ghosts[i].getLocX() < 5) && !checkColPacman(Pathfinder.Movement.RIGHT)) {
//                    MovePacman = Pathfinder.Movement.LEFT;
//                } else if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() < 0 && this.locX - Game.ghosts[i].getLocX() > -5) && !checkColPacman(Pathfinder.Movement.LEFT)) {
//                    MovePacman = Pathfinder.Movement.RIGHT;
//                } else {
//                    dotFinder();
//                }
//            }
//        } else if (Pathfinder.checkNEWS(this.locX, this.locY) == 2) {
//            for (int i = 0; i < 4; i++) {
//                if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() > 0 && this.locX - Game.ghosts[i].getLocX() < 4)) {
//                    MovePacman = Pathfinder.Movement.LEFT;
//                } else if (this.locY == Game.ghosts[i].getLocY() && (this.locX - Game.ghosts[i].getLocX() < 0 && this.locX - Game.ghosts[i].getLocX() > -4)) {
//                    MovePacman = Pathfinder.Movement.RIGHT;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() < 0 && this.locY - Game.ghosts[i].getLocY() > -4)) {
//                    MovePacman = Pathfinder.Movement.DOWN;
//                } else if (this.locX == Game.ghosts[i].getLocX() && (this.locY - Game.ghosts[i].getLocY() > 0 && this.locY - Game.ghosts[i].getLocY() < 4)) {
//                    MovePacman = Pathfinder.Movement.UP;
//                } else {
//                    dotFinder();
//                }
//            }
//
//        }
//    }
}
