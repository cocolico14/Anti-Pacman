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
    
    public enum Movement {
        UP(0, -1), DOWN(0, 1), RIGHT(1, 0), LEFT(-1, 0), NONE(0, 0);
        final int dx, dy;
        
        Movement(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
    
    
    public static Status checkNEWS(int x, int y) {
        int counter = 0;
        Status result = Status.NONE;
        
        if (!Map.isWall(x, y+1)) {
            counter++;
        }
        if (!Map.isWall(x, y-1)) {
            counter++;
        }
        if (x < Map.getGRID_LENGTH() - 1) {
            if (!Map.isWall(x+1, y)) {
                counter++;
            }
        }
        if (x > 1) {
            if (x > 0 && !Map.isWall(x-1, y)) {
                counter++;
            }
        }
        
        if (counter == 2) {
            if(!Map.isWall(x-1, y) && !Map.isWall(x+1, y)){
                result = Status._2WAY_H; // Horizantal
            } else if(!Map.isWall(x, y-1) && !Map.isWall(x, y+1)){
                result = Status._2WAY_V; // Vertical
            }else if (!Map.isWall(x-1, y) && !Map.isWall(x, y+1)) {
                result = Status._2WAY_DL; // Down Left corner
            } else if (!Map.isWall(x+1, y) && !Map.isWall(x, y+1)) {
                result = Status._2WAY_DR; // Down Right corner
            } else if (!Map.isWall(x-1, y) && !Map.isWall(x, y-1)) {
                result = Status._2WAY_UL; // Up Left corner
            } else if (!Map.isWall(x+1, y) && !Map.isWall(x, y-1)) {
                result = Status._2WAY_UR; // Up Right corner
            }
        }else if(counter == 3){
            if (!Map.isWall(x, y+1)
                    && !Map.isWall(x+1, y) && !Map.isWall(x-1, y)) {
                result = Status._3WAY_DRL; // Down Right Left corner
            } else if (!Map.isWall(x, y-1)
                    && !Map.isWall(x+1, y) && !Map.isWall(x-1, y)) {
                result = Status._3WAY_URL; // Up Right Left corner
            } else if (!Map.isWall(x+1, y)) {
                result = Status._3WAY_RUD; // Right Up Down corner
            } else if (!Map.isWall(x-1, y)) {
                result = Status._3WAY_LUD; // Left Up Down corner
            }
        }else{
            result = Status._4WAY;
        }
        
        return result;
    }
}
