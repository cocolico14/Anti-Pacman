/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antipacmanfinal;

import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @author soheil
 */
public class Map {
    
    private static final String[] gameMap = {"#########################",
        "#-----------#-----------#",
        "#^###-#####-#-#####-###^#",
        "#-----------------------#",
        "#-###-#-#########-#-###-#",
        "#-----#-----#-----#-----#",
        "#####-#####-#-#####-#####",
        "#####-#-----------#-#####",
        "#####-#-###+++###-#-#####",
        "*-------##*****##-------*",
        "#####-#-#########-#-#####",
        "#####-#-----------#-#####",
        "#####-#####-#-#####-#####",
        "#-----#-----#-----#-----#",
        "#-###-#-#########-#-###-#",
        "#-----------------------#",
        "#^###-#####-#-#####-###^#",
        "#-----------#-----------#",
        "#########################"};

    private static final int GRID_LENGTH = 25;
    private static final int GRID_HEIGHT = 19;
    private static final int BLOCK_SIZE = 40;

    private HashMap<Pair<Integer, Integer>, Character> board = new HashMap<>();
    
    public Map() {
        int m = 0;
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length(); j++) {
                board.put(new Pair(i, j), gameMap[i].charAt(j));
                if(gameMap[i].charAt(j) == '-' || gameMap[i].charAt(j) == '^'){
                    m++;
                }
            }
        }
        System.out.println(m);
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length(); j++) {
                System.out.print(board.get(new Pair(i, j)));
            }
            System.out.println("");
        }
    }
    
    public HashMap<Pair<Integer, Integer>, Character> getBoard(){
        return this.board;
    }
    
    public void updateBoard(int x, int y, Character ch){
        this.board.put(new Pair(y, x), ch);
    }
    

    @Override
    public String toString() {
        String tmp = "";
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length(); j++) {
                tmp+=board.get(new Pair(i, j));
            }
            tmp+="\n";
        }
        return tmp;
    }
    
    public static boolean isWall(int x, int y){
        return gameMap[y].charAt(x) == '#';
    }
    
/*
    public static String[] getGameMap() {
        return gameMap;
    }
*/
    public static int getGRID_LENGTH() {
        return GRID_LENGTH;
    }

    public static int getGRID_HEIGHT() {
        return GRID_HEIGHT;
    }

    public static int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }

}
