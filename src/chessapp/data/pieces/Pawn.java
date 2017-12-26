/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.data.pieces;

import chessapp.data.*;
import java.util.ArrayList;

/**
 *
 * @author Toothpaste666
 * 
 * TODO : FIX ENPASSANT SHIT BRO
 */
public class Pawn extends Piece{ 
    private boolean doubleMovedLastTurn;
    
    public Pawn(COLOR color, int row, int col){
        super(color, row, col);
        type = TYPE.PAWN;
        doubleMovedLastTurn = false;
        sortRank = 5;
        materialWorth = 1;
    }
     
    @Override
    public String movePiece(Game game, int row, int col, boolean testMove){
        //en passant
        if(canEnPassantLeft(game)){
            if(col < this.col){
                return enPassantLeft(game, testMove);
            }
        }
        
        if(canEnPassantRight(game)){
            if(col > this.col){
                return enPassantRight(game, testMove);
            }
        }
        
        switch(color){
            case WHITE:
                if(this.row == 6 && row == 4){
                    doubleMovedLastTurn = true;
                }else{
                    doubleMovedLastTurn = false;
                }            
                break;
            case BLACK:
                if(this.row == 1 && row == 3){
                    doubleMovedLastTurn = true;
                }else{
                    doubleMovedLastTurn = false;
                }
                break;
        }
        int oldRow = this.row;
        int oldCol = this.col;
        String ret = super.movePiece(game, row, col, testMove);
           
        Square[][] board = game.getBoard();
        
        if(testMove){
            return "";
        }
        
        //piece promotion
        boolean promoting = false;
        
        if(color == COLOR.BLACK && row == 7){
            promoting = true;
        }
        if(color == COLOR.WHITE && row == 0){
            promoting = true;
        }
        if(!promoting){
            return ret;
        }
         
        //return promotePawn(game, oldRow, oldCol, row, col);
        return "Promote" + oldRow + oldCol + row + col;
    }
    
    public void promotionMove(Game game, int row, int col, String choice){
        int oldRow = this.row;
        int oldCol = this.col;
        String ret = super.movePiece(game, row, col, false);
        ret = promotePawn(choice, game, oldRow, oldCol, row, col);
    }
    
    
    public String promotePawnChoice(String choice, Game game, int oldRow, int oldCol, int row, int col){
        Square[][] board = game.getBoard();
       
        Piece promotedPawn = null;

        String ret = "";
  
            switch(choice){
                case "Queen":
                    promotedPawn = new Queen(color, row, col);
                    ret = "PR" + oldRow + oldCol + row + col + "Q\n";
                    break;
                case "Rook":
                    promotedPawn = new Rook(color, row, col);
                    ret = "PR" + oldRow + oldCol + row + col + "R\n";
                    break;
                case "Bishop":
                    promotedPawn = new Bishop(color, row, col);
                    ret = ret = "PR" + oldRow + oldCol + row + col + "B\n";
                    break;
                case "Knight":
                    promotedPawn = new Knight(color, row, col);
                    ret = "PR" + oldRow + oldCol + row + col + "N\n";
                    break;
            }
        
    
        if(color == COLOR.BLACK){ 
            game.getBlackPieces().remove(this);
            game.getBlackPieces().add(promotedPawn);
            board[row][col].setPiece(promotedPawn);
        }else if(color == COLOR.WHITE){
            game.getWhitePieces().remove(this);
            game.getWhitePieces().add(promotedPawn);
            board[row][col].setPiece(promotedPawn);
        }  
        
        return ret;
    }
    
    public String promotePawn(String choice, Game game, int oldRow, int oldCol, int row, int col){
        Square[][] board = game.getBoard();
        Piece promotedPawn = null;
        switch(choice){
            case "Q":
                promotedPawn = new Queen(color, row, col);
                break;
            case "R":
                promotedPawn = new Rook(color, row, col);
                break;
            case "B":
                promotedPawn = new Bishop(color, row, col);
                break;
            case "N":
                promotedPawn = new Knight(color, row, col);
                break;
        }
        
        if(color == COLOR.BLACK){ 
            game.getBlackPieces().remove(this);
            game.getBlackPieces().add(promotedPawn);
            board[row][col].setPiece(promotedPawn);
        }else if(color == COLOR.WHITE){
            game.getWhitePieces().remove(this);
            game.getWhitePieces().add(promotedPawn);
            board[row][col].setPiece(promotedPawn);
        }  
        
        return "";
    }
    
    public boolean doubleMovedLastTurn(){
        return doubleMovedLastTurn;
    }
    
    public void setDoubleMovedLastTurn(boolean val){
        doubleMovedLastTurn = false;
    }
    
    public boolean canEnPassantLeft(Game game){
        int validRow = 0;
        Square[][] board = game.getBoard();
        Piece piece = null;
        Pawn pawn = null;
        
        if(col == 0){
            return false;
        }
        
        //valid row
        switch(color){
            case WHITE:
                validRow = 3;
                break;
            case BLACK:
                validRow = 4;
                break;
        }
        
        if(row != validRow){
            return false;
        }
                    
        //is there a piece to the left
        piece = board[row][col-1].getPiece();
        if(piece == null){
            return false;
        }
       
        //is it a pawn
        if(piece.getType() == TYPE.PAWN){
            pawn = (Pawn)piece;
            //did the pawn double move last turn
            if(pawn.doubleMovedLastTurn()){
                return true;
            }
        }else{
            return false;
        }
          
        return false;
    }
    
    public String enPassantLeft(Game game, boolean testMove){
        Square[][] board = game.getBoard();
        Square leftSquare = null;
        Square destSquare = null;
        Pawn pawn = null;
        ArrayList<Piece> enemyPieces = null;
        
        switch(color){
            case WHITE:
                enemyPieces = game.getBlackPieces();
                destSquare = board[row-1][col-1];
                break;
                
            case BLACK:
                enemyPieces = game.getWhitePieces();
                destSquare = board[row+1][col-1];
                break;
        }
        
        //get and remove pawn to the left
        leftSquare = board[row][col-1];
        pawn = (Pawn)leftSquare.getPiece();
        enemyPieces.remove(pawn);
        
        switch(pawn.getColor()){
            case WHITE:
                game.getCapturedWhitePieces().add(pawn);
                break;
            case BLACK:
                game.getCapturedBlackPieces().add(pawn);
                break;
        }
        
        leftSquare.setPiece(null);
        
        return super.movePiece(game, destSquare.getRow(), destSquare.getCol(), testMove);      
    }
    
    public boolean canEnPassantRight(Game game){
        int validRow = 0;
        Square[][] board = game.getBoard();
        Piece piece = null;
        Pawn pawn = null;
        
        if(col == 7){
            return false;
        }
        
        //valid row
        switch(color){
            case WHITE:
                validRow = 3;
                break;
            case BLACK:
                validRow = 4;
                break;
        }
        
        if(row != validRow){
            return false;
        }
                    
        //is there a piece to the right
        piece = board[row][col+1].getPiece();
        if(piece == null){
            return false;
        }
       
        //is it a pawn
        if(piece.getType() == TYPE.PAWN){
            pawn = (Pawn)piece;
            //did the pawn double move last turn
            if(pawn.doubleMovedLastTurn()){
                return true;
            }
        }else{
            return false;
        }
          
        return false; 
    }
      
    public String enPassantRight(Game game, boolean testMove){
        Square[][] board = game.getBoard();
        Square rightSquare = null;
        Square destSquare = null;
        Pawn pawn = null;
        ArrayList<Piece> enemyPieces = null;
        
        switch(color){
            case WHITE:
                enemyPieces = game.getBlackPieces();
                destSquare = board[row-1][col + 1];
                break;
                
            case BLACK:
                enemyPieces = game.getWhitePieces();
                destSquare = board[row + 1][col + 1];
                break;
        }
        
        //get and remove pawn to the right
        rightSquare = board[row][col+1];
        pawn = (Pawn)rightSquare.getPiece();
        enemyPieces.remove(pawn);
        
        switch(pawn.getColor()){
            case WHITE:
                game.getCapturedWhitePieces().add(pawn);
                break;
            case BLACK:
                game.getCapturedBlackPieces().add(pawn);
                break;
        }
        
        rightSquare.setPiece(null);
        
        return super.movePiece(game, destSquare.getRow(), destSquare.getCol(), testMove);      
    }
    
    @Override
   public ArrayList<Square> getSquaresAttacking(Game game){
        ArrayList<Square> squaresAttacking = new ArrayList<Square>();
        Square[][] board = game.getBoard();
        //code to check if king is in check
        if(color == COLOR.BLACK){
            if(!hasMoved() && Game.isSquareInBounds(row+2, col)){
                Square square = board[row+2][col];
                if(square.getPiece() == null){
                    Square tempSquare = board[row+1][col];
                    if(tempSquare.getPiece() == null)
                        squaresAttacking.add(square);
                }
            }
       
            if(Game.isSquareInBounds(row+1, col)){
                Square square = board[row+1][col];
                if(square.getPiece() == null){
                    squaresAttacking.add(square);
                }
            }
       
            if(Game.isSquareInBounds(row+1, col-1)){
                Square square = board[row+1][col-1];
                if(square.getPiece() != null && square.getPiece().getColor() != color){
                    squaresAttacking.add(square);
                }
            }
       
            if(Game.isSquareInBounds(row+1, col+1)){
                Square square = board[row+1][col+1];
                if(square.getPiece() != null && square.getPiece().getColor() != color){
                    squaresAttacking.add(square);
                }
            }
       }else if(color == COLOR.WHITE){
           if(!hasMoved() && Game.isSquareInBounds(row-2, col)){
                Square square = board[row-2][col];
                if(square.getPiece() == null){
                    Square tempSquare = board[row-1][col];
                    if(tempSquare.getPiece() == null)
                        squaresAttacking.add(square);
                }
            }
       
            if(Game.isSquareInBounds(row-1, col)){
                Square square = board[row-1][col];
                if(square.getPiece() == null){
                    squaresAttacking.add(square);
                }
            }
       
            if(Game.isSquareInBounds(row-1, col-1)){
                Square square = board[row-1][col-1];
                if(square.getPiece() != null && square.getPiece().getColor() != color){
                    squaresAttacking.add(square);
                }
            }
       
            if(Game.isSquareInBounds(row-1, col+1)){
                Square square = board[row-1][col+1];
                if(square.getPiece() != null && square.getPiece().getColor() != color){
                    squaresAttacking.add(square);
                }
            }
       }   
        return squaresAttacking;
   }
   
    @Override
   public ArrayList<Square> getValidMoves(Game game){
        ArrayList<Square> validMoves = super.getValidMoves(game);
        Square[][] board = game.getBoard();
        Square destSquare = null;
            
        if(canEnPassantLeft(game)){                  
            switch(color){
                case WHITE:
                    destSquare = board[row-1][col-1];
                    break;
                case BLACK:
                    destSquare = board[row+1][col-1];
                    break;
            }
            
            validMoves.add(destSquare);         
        }
        
        if(canEnPassantRight(game)){
            switch(color){
                case WHITE:
                    destSquare = board[row-1][col+1];
                    break;
                case BLACK:
                    destSquare = board[row+1][col+1];
                    break;                 
            }
            
            validMoves.add(destSquare);
        }
        
        return validMoves;
   }
   
   @Override
   protected boolean moveAllowsCheck(Game game, Square targetSquare){
       boolean oldDoubleMovedLastTurn = doubleMovedLastTurn;
       
       boolean moveAllowsCheck = super.moveAllowsCheck(game, targetSquare);
       
       doubleMovedLastTurn = oldDoubleMovedLastTurn;
       
       return moveAllowsCheck;
   }

    @Override
    public String toString(){
        switch(color)
        {
            case WHITE:
                return "WhitePawn";
            case BLACK:
                return "BlackPawn";                        
        }
        return "";
    }
}
