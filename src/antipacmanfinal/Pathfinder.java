/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package antipacmanfinal;

/**
 *
 * @author soheil
 */
public class Pathfinder {
    
    private Map map;

    public Pathfinder(Map map) {
        this.map = map;
    }
    
    public Status checkNEWS(int x, int y) {
        int counter = 0;
        Status result = Status.NONE;
        
        if (!map.isWall(x, y+1)) {
            counter++;
        }
        if (!map.isWall(x, y-1)) {
            counter++;
        }
        if (x < map.getGRID_LENGTH() - 1) {
            if (!map.isWall(x+1, y)) {
                counter++;
            }
        }
        if (x > 1) {
            if (x > 0 && !map.isWall(x-1, y)) {
                counter++;
            }
        }
        
        if (counter == 2) {
            if(!map.isWall(x-1, y) && !map.isWall(x+1, y)){
                result = Status._2WAY_H; // Horizantal
            } else if(!map.isWall(x, y-1) && !map.isWall(x, y+1)){
                result = Status._2WAY_V; // Vertical
            }else if (!map.isWall(x-1, y) && !map.isWall(x, y+1)) {
                result = Status._2WAY_DL; // Down Left corner
            } else if (!map.isWall(x+1, y) && !map.isWall(x, y+1)) {
                result = Status._2WAY_DR; // Down Right corner
            } else if (!map.isWall(x-1, y) && !map.isWall(x, y-1)) {
                result = Status._2WAY_UL; // Up Left corner
            } else if (!map.isWall(x+1, y) && !map.isWall(x, y-1)) {
                result = Status._2WAY_UR; // Up Right corner
            }
        }else if(counter == 3){
            if (!map.isWall(x, y+1)
                    && !map.isWall(x+1, y) && !map.isWall(x-1, y)) {
                result = Status._3WAY_DRL; // Down Right Left corner
            } else if (!map.isWall(x, y-1)
                    && !map.isWall(x+1, y) && !map.isWall(x-1, y)) {
                result = Status._3WAY_URL; // Up Right Left corner
            } else if (!map.isWall(x+1, y)) {
                result = Status._3WAY_RUD; // Right Up Down corner
            } else if (!map.isWall(x-1, y)) {
                result = Status._3WAY_LUD; // Left Up Down corner
            }
        }else {
            result = Status._4WAY;
        }
        
        return result;
    }
    
    public boolean checkColision(Pacman p, Ghosts g){
        if(p.getLocY() == g.getLocY()){
            if(p.getLocX() - g.getLocX() <= 1
                    && p.getLocX() - g.getLocX() >= 0){ // pacman is right side of ghost
                if(p.getMovePacman() == Movement.LEFT
                        && (g.getRandomMoveGhost() == Movement.RIGHT
                            || g.getmGhost() == Movement.RIGHT)){
                    return true;
                }
            }else if(g.getLocX() - p.getLocX() <= 1
                    && g.getLocX() - p.getLocX() >= 0){ // pacman is left side of ghost
                if(p.getMovePacman() == Movement.RIGHT
                        && (g.getRandomMoveGhost() == Movement.LEFT
                            || g.getmGhost() == Movement.LEFT)){
                    return true;
                }
            }
        }else if(p.getLocX() == g.getLocX()){
            if(p.getLocY() - g.getLocY() <= 1
                    && p.getLocY() - g.getLocY() >= 0){ // pacman is below ghost
                if(p.getMovePacman() == Movement.UP
                        && (g.getRandomMoveGhost() == Movement.DOWN
                            || g.getmGhost() == Movement.DOWN)){
                    return true;
                }
            }else if(g.getLocY() - p.getLocY() <= 1
                    && g.getLocY() - p.getLocY() >= 0){ // pacman is above ghost
                if(p.getMovePacman() == Movement.DOWN
                        && (g.getRandomMoveGhost() == Movement.UP
                            || g.getmGhost() == Movement.UP)){
                    return true;
                }
            }
        }
        return (p.getLocX() == g.getLocX() && p.getLocY() == g.getLocY());
    }
}
