/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.data.pieces;

import chessapp.data.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Toothpaste666
 */

public abstract class Piece implements Comparable<Piece> {
    protected COLOR color;
    protected TYPE type;
    protected int row, col;
    private boolean hasMoved;
    int sortRank;
    int materialWorth;
        
    public Piece(COLOR color, int row, int col){
        this.color = color;
        this.row = row;
        this.col = col;
        hasMoved = false;
    }
    
    public int getMaterialWorth(){
        return materialWorth;
    }
    
    public int compareTo(Piece piece){
        if(this.sortRank == piece.sortRank){
            return 0;
        }else if(this.sortRank < piece.sortRank){
            return -1;
        }else
        {
            return 1;
        }
    }
    
    public COLOR getColor(){
        return color;
    }
    
    public TYPE getType(){
        return type;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public boolean hasMoved(){
        return hasMoved;
    }
    
    public String movePiece(Game game, int destRow, int destCol, boolean testMove){   
        Square[][] board = game.getBoard();
             
        int oldRow = this.row;
        int oldCol = this.col;
        board[this.row][this.col].setPiece(null);
        this.row = destRow;
        this.col = destCol;
        
        //remove any captured piece from the game
        Piece capturedPiece = board[destRow][destCol].getPiece();
        if(capturedPiece != null){
            switch(capturedPiece.getColor()){
                case WHITE:
                    game.getWhitePieces().remove(capturedPiece);
                    if(!testMove){
                        game.getCapturedWhitePieces().add(capturedPiece);
                        Collections.sort(game.getCapturedWhitePieces());
                    }
                    break;
                case BLACK:
                    game.getBlackPieces().remove(capturedPiece);
                    if(!testMove){
                        game.getCapturedBlackPieces().add(capturedPiece);
                        Collections.sort(game.getCapturedBlackPieces());
                    }
                    break;
            }
        }       
        board[destRow][destCol].setPiece(this);
        
        if(!hasMoved){
            hasMoved = true;
        }  
        
        COLOR otherPlayer = null;
        switch(color){
            case WHITE:
                otherPlayer = COLOR.BLACK;
                break;
            case BLACK:
                otherPlayer = COLOR.WHITE;
                break;
        }
        
        game.setCurrentTurn(otherPlayer);
        
        //return move in message form
        String msg = "MV" + oldRow + oldCol + destRow + destCol + "\n";
        return msg;
    }
    
    public void highlightValidMoves(Game game)
    {
        ArrayList<Square> validMoves = getValidMoves(game);
                  
        for(Square square : validMoves){
            square.setHighlighted(true);
        }
    }
    
    protected boolean moveAllowsCheck(Game game, Square targetSquare)
    {
        Square[][] board = game.getBoard();
        //store the current piece on target square, if any
        Piece currentPiece = targetSquare.getPiece();
        
        //movePiece changes this, we want to cancel that effect here
        boolean oldHasMoved = hasMoved;
        COLOR oldCurrentTurn = game.getCurrentTurn();
        
        boolean allowedCheck = false;
        
        //save old coordinates
        int oldRow = this.row;
        int oldCol = this.col;
        
        //try making the move and see if it puts king in check
        movePiece(game, targetSquare.getRow(), targetSquare.getCol(), true);
        if(game.isKingInCheck(color)){
            allowedCheck = true;
        }
        
        //undo stuff done by movePiece
        board[oldRow][oldCol].setPiece(this);
        this.row = oldRow;
        this.col = oldCol;
        
        if(currentPiece != null){
            switch(currentPiece.getColor()){
                case WHITE:
                    game.getWhitePieces().add(currentPiece);
                    break;
                case BLACK:
                    game.getBlackPieces().add(currentPiece);
                    break;
            }
        }
        
        targetSquare.setPiece(currentPiece);  
        hasMoved = oldHasMoved;
        game.setCurrentTurn(oldCurrentTurn);
        return allowedCheck;
    }
    
    public ArrayList<Square> getValidMoves(Game game){
        ArrayList<Square> validMoves = new ArrayList<Square>();
        ArrayList<Square> squaresAttacking = getSquaresAttacking(game);
        
        for(Square targetSquare : squaresAttacking){
            if(!moveAllowsCheck(game, targetSquare)){
                validMoves.add(targetSquare);
            }
        }
        return validMoves;
    }
    
    public abstract ArrayList<Square> getSquaresAttacking(Game game);
}
