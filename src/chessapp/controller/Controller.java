/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.controller;

import chessapp.ClientThread;
import chessapp.data.*;
import chessapp.data.pieces.COLOR;
import chessapp.data.pieces.Pawn;
import chessapp.data.pieces.Piece;
import chessapp.gui.GUI;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

/**
 *
 * @author Toothpaste666
 */
public class Controller {
    public Game game;
    public GUI gui;
    public Socket clientSocket;
    public DataOutputStream outToServer;
    public  BufferedReader inFromServer;
    ClientThread clientThread;
    
    public boolean inGame;
    
    
    public COLOR playerColor;
    public String enemyUsername;
    
    public Controller(Game game, GUI gui){
        this.game = game;
        this.gui = gui;
    }
    
    public String getPlayerColorString(){
        return playerColor.toString();
    }
    
    public String getEnemyUsername(){
        return enemyUsername;
    }
    
    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }
     
    public void writeToServer(String msg){
        try {
            outToServer.writeBytes(msg);
        } catch (IOException ex) {
        }
    }
    
    public void handleLogin(){
        //TODO : make a max length for username and disallow spaces
        
        boolean loggedIn = false;
        String userName = "";
        String serverIp = "";
        while(!loggedIn)
        {
            try{
                Pair<String, String> resultPair = gui.getLoginInfo();
                userName = resultPair.getKey();
                serverIp = resultPair.getValue();
                    
                clientSocket = new Socket(serverIp, 8000);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                loggedIn = true;
            }catch(Exception e){
                gui.displayLoginFailed();
            }
        }
                
        clientThread = new ClientThread(inFromServer, game, this);
        clientThread.start();
        writeToServer(userName + "\n");  
    }
    
    public String getPromotionChoice(){
        String choice = gui.getPawnPromotionInfo();
        return choice;
    } 
    
    void deselectCells(){  
        gui.setSelectedCell(null);
        
        for(int row= 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                game.getSquare(row, col).setHighlighted(false);               
            }
        }
        drawBoard();
    }
     
    public void closeApp(){
        try {
            //TODO: get other player to close out their socket when connection is lost            
            outToServer.writeBytes("EX");
            clientThread.join();
            
            clientSocket.close();
            inFromServer.close();
            outToServer.close();     
            
            System.exit(0);
        } catch (Exception ex) {           
        } 
    }
    
    public void initGame() throws IOException{        
        gui.setTitle("Chess App: White's Turn");           
        //game.getBoard()[0][3].setHighlighted(true);
        drawBoard(); 
            
        gui.removeWaitingBox();
        gui.addTopBar(enemyUsername);
        gui.addBottomBar();
        gui.addCapturedPieceDisplay();
        
        if(playerColor == COLOR.BLACK){
            gui.flipBoardDisplay();
            gui.startEnemyClock();
        }else{
            gui.startPlayerClock();
        }
        
        inGame = true;
    }
    
    public void resignButtonPressed()
    {
        writeToServer("RN");
        inGame = false;
        startNewGame();     
    }
    
    public void checkForGameOver(COLOR color)
    {
        gui.setTitle("Chess App: " + color.oppositeColor() + "'s Turn");
        if(game.isKingInCheckmate(color.oppositeColor())){ 
            inGame = false;
            gui.showCheckmateAlert(color.toString());            
            startNewGame();        
        }else if(game.isKingInStalemate(color.oppositeColor())){  
            inGame = false;
            gui.showStalemateAlert();                           
            startNewGame();
        }else if(game.insufficientMaterial()){ 
            inGame = false;
            gui.showInsufficientMaterialAlert();                       
            startNewGame();
        } 
    }
    
    public void startNewGame(){
        game = new Game();
        gui.initBoardDisplay();
        clientThread.setGame(game);    
        gui.goToLobby();
        gui.stopClocks();
    }
    
    public void drawBoard(){
        Square square;
        Piece piece;
        String fontColor;
        
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){                   
               square = game.getSquare(row, col);           
               piece = square.getPiece();
               fontColor = "";
               
               if(piece != null){
                   fontColor = piece.getColor().toString();
               }
                          
               gui.drawCell(row, col, square.toString(), square.isHighlighted(), fontColor);                      
            }
        }
        
        if(!inGame)
        {
            return;
        }
    
        gui.addCapturedPieceDisplay();
        Label label;
        int whiteCapturedMaterial = 0;
        for(Piece whitePiece : game.getCapturedWhitePieces()){
            label = new Label("" + whitePiece.toString().charAt(5));
            label.setStyle("-fx-text-fill: white; -fx-font-size: 12pt; -fx-font-weight: bold;"); 
            gui.getWhitesCapturedPieces().getChildren().add(label);
            whiteCapturedMaterial += whitePiece.getMaterialWorth();
        }
        
        int blackCapturedMaterial = 0;
        for(Piece blackPiece : game.getCapturedBlackPieces()){
            label = new Label("" + blackPiece.toString().charAt(5));
            label.setStyle("-fx-text-fill: black; -fx-font-size: 12pt; -fx-font-weight: bold;"); 
            gui.getBlacksCapturedPieces().getChildren().add(label); 
            blackCapturedMaterial += blackPiece.getMaterialWorth();
        }
        
        if(whiteCapturedMaterial > blackCapturedMaterial){
            label = new Label(" (+" + (whiteCapturedMaterial - blackCapturedMaterial) + ")");
            label.setStyle("-fx-text-fill: blue;");
            gui.getBlacksCapturedPieces().getChildren().add(label);
        }else if(blackCapturedMaterial > whiteCapturedMaterial){
            label = new Label(" (+" + (blackCapturedMaterial - whiteCapturedMaterial) + ")");
            label.setStyle("-fx-text-fill: blue;");
            gui.getWhitesCapturedPieces().getChildren().add(label);
        }
    }  
    
    public void selectCell(StackPane clickedCell){   
        if(game.getCurrentTurn() != playerColor){
            return;
        }
        
        Square clickedSquare = null;
        Square selectedSquare = null;
        
        StackPane selectedCell = gui.getSelectedCell();
        
        //get the row and col of clicked cell
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                if(clickedCell == gui.getCell(row, col)){
                    clickedSquare = game.getSquare(row, col);
                }
                if(selectedCell ==  gui.getCell(row, col)){
                    selectedSquare = game.getSquare(row, col);
                }
            }
        }
           
        if(selectedCell != null){ //there was already a square selected
            if(selectedCell == clickedCell){ //player clicked already selected square
                return;
            }else if(clickedSquare.isHighlighted()){ //player clicked highlighted square
                //clear pawn statuses
                game.clearPawnStatus(game.getCurrentTurn());
                
                Piece movingPiece = selectedSquare.getPiece();               
                String msg = movingPiece.movePiece(game, clickedSquare.getRow(), clickedSquare.getCol(), false);
                if(msg.startsWith("Promote")){
                   int oldRow = Integer.parseInt("" + msg.charAt(7));
                   int oldCol = Integer.parseInt("" + msg.charAt(8));
                   int newRow = Integer.parseInt("" + msg.charAt(9));
                   int newCol = Integer.parseInt("" + msg.charAt(10));
                   Pawn pawn = (Pawn)movingPiece;
                   String choice = gui.getPawnPromotionInfo();
                   msg = pawn.promotePawnChoice(choice, game, oldRow, oldCol, newRow, newCol);
                }
                //send move              
                writeToServer(msg);
                
                deselectCells();
                drawBoard();
                
                //timerstuff
                gui.pausePlayerClock();
                gui.startEnemyClock();
                               
                //check for mate
                COLOR color = movingPiece.getColor();
                checkForGameOver(color);
                
            }else{ //player clicked non highlighted square
                deselectCells();
                Piece piece = clickedSquare.getPiece();
                if (piece != null) {
                    //if not your turn do nothing
                    if(piece.getColor() != game.getCurrentTurn()){
                        return;
                    }
                    
                    clickedSquare.setHighlighted(true);
                    clickedSquare.getPiece().highlightValidMoves(game);
                    gui.setSelectedCell(clickedCell);
                    drawBoard();
                }
            }
        }else{ //there was no square selected
            Piece piece = clickedSquare.getPiece();
            if (piece != null) {
                //if not your turn do nothing
                if(piece.getColor() != game.getCurrentTurn()){
                    return;
                }
                               
                clickedSquare.setHighlighted(true);
                clickedSquare.getPiece().highlightValidMoves(game);
                gui.setSelectedCell(clickedCell);
                drawBoard();
            }
        }
    }
}
