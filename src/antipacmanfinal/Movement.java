/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package antipacmanfinal;

/**
 *
 * @author soheilchangizi
 */
public enum Movement {
    
    UP(0, -1), DOWN(0, 1), RIGHT(1, 0), LEFT(-1, 0), NONE(0, 0);
    final int dx, dy;
    
    Movement(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
}
