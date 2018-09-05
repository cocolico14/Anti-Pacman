/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antipacmanfinal;

import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author soheil
 */
public class AntiPacmanFinal extends Application {
  
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Group root = new Group();
        Game game = new Game();
        Scene scene = game.launchGame(root);
        
        primaryStage.setTitle("Anti-Pacman v1.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
