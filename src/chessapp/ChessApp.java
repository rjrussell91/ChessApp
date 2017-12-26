/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp;

import chessapp.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import chessapp.data.*;
import chessapp.gui.GUI;
import java.io.IOException;
/**
 *
 * @author Toothpaste666
 */
public class ChessApp extends Application {
    //GridPane root;
    Game game;
    Controller controller;
    GUI gui;
       
    @Override
    public void start(Stage primaryStage) throws IOException {       
        game = new Game(); 
        gui = new GUI(primaryStage);
        controller = new Controller(game, gui);
        gui.setController(controller);
        controller.handleLogin();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){            
        launch(args);
    }     
    
    @Override
    public void stop(){
        controller.closeApp();
    }
}