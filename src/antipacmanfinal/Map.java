/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package antipacmanfinal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @author soheil
 */
public class Map {
    
    private static final int BLOCK_SIZE = 30;
    
    private final String fileName = System.getProperty("user.dir")+"/src/antipacmanfinal/map.txt";
    private final int GRID_LENGTH;
    private final int GRID_HEIGHT;
    
    private HashMap<Pair<Integer, Integer>, Character> board = new HashMap<>();
    
    
    public Map() {
        
        String line = null;
        int c = 0;
        String lastLine = "";
        try{
            FileReader fileReader =
                    new FileReader(fileName);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    board.put(new Pair(c, i), line.charAt(i));
                }
                c++;
                lastLine = line;
            }
            bufferedReader.close();
        }catch(Exception e) {
        }finally{
            this.GRID_LENGTH = lastLine.length();
            this.GRID_HEIGHT = c;
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
        for (int i = 0; i < this.GRID_HEIGHT; i++) {
            for (int j = 0; j < this.GRID_LENGTH; j++) {
                tmp+=board.get(new Pair(i, j));
            }
            tmp+="\n";
        }
        return tmp;
    }
    
    public boolean isWall(int x, int y){
        return board.get(new Pair(y, x)).equals(Character.valueOf('#'));
    }
    
    public int getGRID_LENGTH() {
        return GRID_LENGTH;
    }
    
    public int getGRID_HEIGHT() {
        return GRID_HEIGHT;
    }
    
    public static int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }
    
}
