/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package antipacmanfinal;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author soheil
 */
public class Game {
    
    private AnimationTimer timer;
    
    private final int dot = 210;
    private boolean isHollow = false;
    private long time = 0;
    private int ghostC = 4;
    private boolean won = false;
    private Pathfinder pathFinder;
    
    private Pacman pacman;
    private Ghosts[] ghosts = new Ghosts[4];
    private GhostFoc[] gfVal = GhostFoc.values();
    private Dots dots;
    private Map map;
    
    /**
     * This enum is for show which ghost is focused
     */
    private enum GhostFoc {
        RED(0), PINK(1), CYAN(2), ORANGE(3);
        final int index;
        
        GhostFoc(int index) {
            this.index = index;
        }
    }
    private GhostFoc gFocus = GhostFoc.RED;
    
    /**
     * This method is used to setup the game
     *
     * @param root This is the Group parameter for setting nodes on
     * @return Scene This returns scene to start method.
     */
    public Scene launchGame(Group root) throws FileNotFoundException {
        
        map = new Map();
        pathFinder = new Pathfinder(map);
        dots = new Dots(this);
        
        
        Scene scene = new Scene(root, map.getGRID_LENGTH() * map.getBLOCK_SIZE(), map.getGRID_HEIGHT() * map.getBLOCK_SIZE(), Color.BLACK);
        startUp(root);
        
        inputKey(scene, root);
        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 200000000) {
                    try {
                        onUpdate(root);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    lastUpdate = now;
                }
            }
            
        };
        timer.start();
        
        return scene;
    }
    
    /**
     * This method is used to initial game objects
     *
     * @param root This is the Group parameter for setting nodes on
     */
    private void startUp(Group root) throws FileNotFoundException {
        
        for (int i = 0; i < map.getGRID_HEIGHT(); i++) {
            for (int j = 0; j < map.getGRID_LENGTH(); j++) {
                if (null != map.getBoard().get(new Pair(i, j))) switch (map.getBoard().get(new Pair(i, j))) {
                    case '#':
                        Rectangle block = new Rectangle();
                        block.setX(j * map.getBLOCK_SIZE());
                        block.setY(i * map.getBLOCK_SIZE());
                        block.setWidth(map.getBLOCK_SIZE());
                        block.setHeight(map.getBLOCK_SIZE());
                        block.setFill(Color.BLUE);
                        block.setArcHeight(10);
                        block.setOpacity(0.6);
                        block.setSmooth(true);
                        block.setStroke(Color.ALICEBLUE);
                        block.setArcWidth(10);
                        root.getChildren().add(block);
                        break;
                    case '+':
                        Line gate = new Line((j * map.getBLOCK_SIZE()), (i * map.getBLOCK_SIZE()), ((j + 1) * map.getBLOCK_SIZE()), (i * map.getBLOCK_SIZE()));
                        gate.setFill(Color.GOLD);
                        gate.setStroke(Color.DARKMAGENTA);
                        gate.setStrokeWidth(5);
                        root.getChildren().add(gate);
                        break;
                    case '-':
                        dots.setDot(j, i, 0, root);
                        break;
                    case '^':
                        dots.setDot(j, i, 1, root);
                        break;
                    default:
                        break;
                }
            }
        }
        
        pacman = new Pacman(12, 15, root, this);
        ghosts[0] = new Ghosts(10, 9, root, Color.RED, this);
        ghosts[1] = new Ghosts(11, 9, root, Color.PINK, this);
        ghosts[2] = new Ghosts(13, 9, root, Color.CYAN, this);
        ghosts[3] = new Ghosts(14, 9, root, Color.ORANGE, this);
        ghosts[gFocus.index].getIv().setEffect(Ghosts.getGlow());
    }
    
    /**
     * This method is used to update game objects
     *
     * @param root This is the Group parameter for setting nodes on
     */
    private void onUpdate(Group root) throws FileNotFoundException {
        
        checkStatus(root);
        
        ghosts[gFocus.index].moveGhost(ghosts[gFocus.index].getmGhost());
        int uhum = dots.eatDot(pacman.getLocX(), pacman.getLocY()
                , map.getBoard().get(new Pair(pacman.getLocY(), pacman.getLocX())));
        map.updateBoard(pacman.getLocX(), pacman.getLocY(), ' ');
        if (uhum == 1) {
            isHollow = true;
            time = System.currentTimeMillis();
        }
        
        if (isHollow) {
            pacman.AI(true);
            pacman.movePacman(pacman.getMovePacman());
            for (int i = 0; i < 4; i++) {
                ghosts[i].HollowMode();
                if (i != gFocus.index) {
                    ghosts[i].randomGhost();
                    ghosts[i].moveGhost(ghosts[i].getRandomMoveGhost());
                }
                if (pathFinder.checkColision(pacman, ghosts[i]) && ghosts[i].getCheckV() != 0) {
                    for (int j = 0; j < 4; j++) {
                        if(i != j && ghosts[j].getCheckV() == 1){
                            gFocus = gfVal[j];
                            ghosts[i].getIv().setEffect(null);
                            ghosts[j].getIv().setEffect(Ghosts.getGlow());
                            break;
                        }
                    }
                    ghosts[i].setUnvisible();
                    ghostC--;
                }
            }
        }else{
            pacman.AI(false);
            pacman.movePacman(pacman.getMovePacman());
            
            for (int i = 0; i < 4; i++) {
                if (i != gFocus.index) {
                    ghosts[i].randomGhost();
                    ghosts[i].moveGhost(ghosts[i].getRandomMoveGhost());
                }
                if (pathFinder.checkColision(pacman, ghosts[i]) && ghosts[i].getCheckV() != 0) {
                    won = true;
                }
            }
        }
        
        if (isHollow && time + 10*1000 < System.currentTimeMillis()) {
            time = 0;
            isHollow = false;
            for (int i = 0; i < 4; i++) {
                ghosts[i].resetColor();
            }
        }
        
        checkStatus(root);
    }
    
    /**
     * This method is used to check game state
     */
    private void checkStatus(Group root) {
        if (dots.getDotCounter() == 0) {
            System.out.println("GAMEOVER");
            String win = "YOU LOSE";
            
            HBox hBox = new HBox();
            hBox.setTranslateX((map.getGRID_LENGTH() * map.getBLOCK_SIZE())/3);
            hBox.setTranslateY((map.getGRID_HEIGHT()* map.getBLOCK_SIZE())/2.5);
            root.getChildren().add(hBox);
            
            for (int i = 0; i < win.toCharArray().length; i++) {
                char letter = win.charAt(i);
                
                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);
                text.setFill(Color.RED);
                
                hBox.getChildren().add(text);
                
                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(i * 0.15));
                ft.play();
            }
            timer.stop();
        }
        if (won == true) {
            System.out.println("GAMEOVER");
            String win = "YOU WIN";
            
            HBox hBox = new HBox();
            hBox.setTranslateX((map.getGRID_LENGTH() * map.getBLOCK_SIZE())/3);
            hBox.setTranslateY((map.getGRID_HEIGHT()* map.getBLOCK_SIZE())/2.5);
            root.getChildren().add(hBox);
            
            for (int j = 0; j < win.toCharArray().length; j++) {
                char letter = win.charAt(j);
                
                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);
                text.setFill(Color.GREEN);
                
                hBox.getChildren().add(text);
                
                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(j * 0.15));
                ft.play();
            }
            timer.stop();
        }
        
        
        if (ghostC == 0) {
            System.out.println("GAMEOVER");
            String win = "YOU LOSE";
            
            HBox hBox = new HBox();
            hBox.setTranslateX((map.getGRID_LENGTH() * map.getBLOCK_SIZE())/3);
            hBox.setTranslateY((map.getGRID_HEIGHT()* map.getBLOCK_SIZE())/2.5);
            root.getChildren().add(hBox);
            
            for (int j = 0; j < win.toCharArray().length; j++) {
                char letter = win.charAt(j);
                
                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);
                text.setFill(Color.RED);
                
                hBox.getChildren().add(text);
                
                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(j * 0.15));
                ft.play();
            }
            timer.stop();
        }
        
    }
    
    /**
     * This method is used to get data from keyboard
     *
     * @param scene This is the Scene parameter for setting keyevent on
     * @param root This is the Group parameter for setting nodes on
     */
    private void inputKey(Scene scene, Group root) {
        Scanner input = new Scanner(System.in);
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case UP:
                    if (!ghosts[gFocus.index].checkColGhost(Movement.UP)) {
                        ghosts[gFocus.index].setmGhost(Movement.UP);
                    }
                    break;
                case RIGHT:
                    if (!ghosts[gFocus.index].checkColGhost(Movement.RIGHT)) {
                        ghosts[gFocus.index].setmGhost(Movement.RIGHT);
                    }
                    break;
                case DOWN:
                    if (!ghosts[gFocus.index].checkColGhost(Movement.DOWN)) {
                        ghosts[gFocus.index].setmGhost(Movement.DOWN);
                    }
                    break;
                case LEFT:
                    if (!ghosts[gFocus.index].checkColGhost(Movement.LEFT)) {
                        ghosts[gFocus.index].setmGhost(Movement.LEFT);
                    }
                    break;
                case DIGIT1:
                    if(ghosts[0].getCheckV() == 1){
                        ghosts[gFocus.index].getIv().setEffect(null);
                        gFocus = GhostFoc.RED;
                        ghosts[gFocus.index].getIv().setEffect(Ghosts.getGlow());
                    }
                    break;
                case DIGIT2:
                    if(ghosts[1].getCheckV() == 1){
                        ghosts[gFocus.index].getIv().setEffect(null);
                        gFocus = GhostFoc.PINK;
                        ghosts[gFocus.index].getIv().setEffect(Ghosts.getGlow());
                    }
                    break;
                case DIGIT3:
                    if(ghosts[2].getCheckV() == 1){
                        ghosts[gFocus.index].getIv().setEffect(null);
                        gFocus = GhostFoc.CYAN;
                        ghosts[gFocus.index].getIv().setEffect(Ghosts.getGlow());
                    }
                    break;
                case DIGIT4:
                    if(ghosts[3].getCheckV() == 1){
                        ghosts[gFocus.index].getIv().setEffect(null);
                        gFocus = GhostFoc.ORANGE;
                        ghosts[gFocus.index].getIv().setEffect(Ghosts.getGlow());
                    }
                    break;
            }
        });
    }

    public Pathfinder getPathFinder() {
        return pathFinder;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public Ghosts[] getGhosts() {
        return ghosts;
    }

    public Map getMap() {
        return map;
    }
    
}
