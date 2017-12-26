/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp;

import chessapp.controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import chessapp.data.*;
import chessapp.data.pieces.*;
import javafx.application.Platform;

/**
 *
 * @author Toothpaste666
 */
public class ClientThread extends Thread{
    BufferedReader inFromServer;
    Square[][] board;
    Game game;
    Controller controller;
    boolean running;
    
    public ClientThread(BufferedReader inFromServer, Game game, Controller controller){
        super();
        this.inFromServer = inFromServer;
        this.controller = controller;
        this.game = game;
        this.running = true;
    }
    
    public void setGame(Game game){
        this.game = game;
    }
       
    public void run(){
        while(running){
            String msg = "";
            try {
                msg = inFromServer.readLine();                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            if(msg.equals(""))
                continue;
            if(msg.substring(0, 2).equals("WH")){             
                controller.playerColor = COLOR.WHITE; 
                controller.enemyUsername = msg.substring(2);
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            //app.enemyUsername = inFromServer.readLine();
                            controller.initGame();
                        } catch (IOException ex) {                            
                        }
                    }
                });
            }else if(msg.substring(0, 2).equals("BL")){
                controller.playerColor = COLOR.BLACK;
                controller.enemyUsername = msg.substring(2);
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            //app.enemyUsername = inFromServer.readLine();
                            controller.initGame();
                        } catch (IOException ex) {                            
                        }
                    }
                });
            }else if(msg.substring(0, 2).equals("MV")){
                board =  game.getBoard();
                int pieceRow = Integer.parseInt("" + msg.charAt(2));
                int pieceCol = Integer.parseInt("" + msg.charAt(3));
                int destRow = Integer.parseInt("" + msg.charAt(4));
                int destCol = Integer.parseInt("" + msg.charAt(5));
                
                //clear pawn statuses
                game.clearPawnStatus(game.getCurrentTurn());
                
                Piece piece = board[pieceRow][pieceCol].getPiece(); 
                String s = piece.movePiece(game, destRow, destCol, false);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.drawBoard();
                        
                        //timer
                        controller.gui.pauseEnemyClock();
                        controller.gui.startPlayerClock();
                        
                        //check for mate
                        COLOR color = piece.getColor();
                        controller.checkForGameOver(color);
                    }
                });
            }else if(msg.substring(0, 2).equals("PR")){
                board =  game.getBoard();
                int pieceRow = Integer.parseInt("" + msg.charAt(2));
                int pieceCol = Integer.parseInt("" + msg.charAt(3));
                int destRow = Integer.parseInt("" + msg.charAt(4));
                int destCol = Integer.parseInt("" + msg.charAt(5));
                String choice = "" + msg.charAt(6);
                Pawn piece = (Pawn)board[pieceRow][pieceCol].getPiece(); 
                piece.promotionMove(game, destRow, destCol, choice);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.drawBoard();
                        
                        //timer
                        controller.gui.pauseEnemyClock();
                        controller.gui.startPlayerClock();
                        
                        //check for mate
                        COLOR color = piece.getColor();
                        controller.checkForGameOver(color);
                    }
                });
            }else if(msg.substring(0, 2).equals("EX")){
                this.running = false;
            }else if(msg.substring(0, 2).equals("RN")){
                controller.inGame = false;
                
                Platform.runLater(new Runnable(){              
                    @Override
                    public void run(){                                          
                        controller.gui.showResignAlert();
                        controller.startNewGame();
                    }
                });
            }else if(msg.substring(0, 2).equals("AD")){
                controller.inGame = false;
                
                Platform.runLater(new Runnable(){              
                    @Override
                    public void run(){
                        controller.gui.displayDrawAccepted();
                        controller.gui.showDrawAgreedAlert();
                        controller.startNewGame();
                    }
                });
            }else if(msg.substring(0, 2).equals("OD")){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        controller.gui.displayDrawOffer();
                    }
                });
            }else if(msg.substring(0, 2).equals("DD")){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        controller.gui.displayDrawDeclined();
                    }
                });
            }else if(msg.substring(0, 2).equals("DC")){ 
                if(!controller.inGame){
                    continue;
                }
                controller.inGame = false;
                
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        controller.gui.showDisconnectAlert();
                        controller.startNewGame();
                    }
                });
            }
        }
    }    
}
