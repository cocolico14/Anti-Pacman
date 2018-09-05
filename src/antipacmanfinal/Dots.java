/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antipacmanfinal;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author soheil
 */
public class Dots {

    private Circle[][] dotN = new Circle[Map.getGRID_LENGTH()][Map.getGRID_HEIGHT()];
    private int dotCounter = 210;

    public Dots() {
    }
    
    

    /**
     * This method is used to set dots on map
     *
     * @param x This is the int parameter for x cord
     * @param y This is the int parameter for y cord
     * @param mode This check if dot is magic or not
     * @param root This is the Group parameter for setting nodes on
     */
    public void setDot(int x, int y, int mode, Group root) {
        if (mode == 0) {
            this.dotN[x][y] = new Circle((x * Map.getBLOCK_SIZE()) + 15, (y * Map.getBLOCK_SIZE()) + 15, 2, Color.WHITE);
        } else {
            this.dotN[x][y] = new Circle((x * Map.getBLOCK_SIZE()) + 15, (y * Map.getBLOCK_SIZE()) + 15, 5, Color.DARKORANGE);
        }
        root.getChildren().add(this.dotN[x][y]);
    }

    /**
     * This method is used to make dot disapear after they eaten by pacman
     *
     * @param x This is the int parameter for x cord
     * @param y This is the int parameter for y cord
     * @return int This returns 0 or 1  or -1 for finding dots.
     */
    public int eatDot(int x, int y, Character ch) {
//        System.out.println(ch + " : " + Character.valueOf('-'));
        if (ch.equals(Character.valueOf('-'))) {
            this.dotN[x][y].setVisible(false);
            dotCounter--;
            return 0;
        }else if(ch.equals(Character.valueOf('^'))){
            this.dotN[x][y].setVisible(false);
            dotCounter--;
            return 1;
        }
        return -1;
    }

    public int getDotCounter() {
        return dotCounter;
    }  

}
