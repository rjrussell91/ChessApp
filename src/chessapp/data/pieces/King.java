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
 */
public class King extends Piece{
    public King(COLOR color, int row, int col){
        super(color, row, col);
        type = TYPE.KING;
    }
    
    @Override
    public String movePiece(Game game, int destRow, int destCol, boolean testMove){
        if(canCastleKingSide(game)){
            if(destCol == 6){
               return castleKingSide(game, testMove);
            }          
        }
        
        if(canCastleQueenSide(game)){
            if(destCol == 2){
                return castleQueenSide(game, testMove);
            }
        }
        
        return super.movePiece(game, destRow, destCol, testMove);
    }
    
    public ArrayList<Square> getSquaresAttacking(Game game){
        int tempRow, tempCol;
        Square square;
        ArrayList<Square> squaresAttacking = new ArrayList<Square>();
        Square[][] board = game.getBoard();
          
        //up
        tempRow = row + 1;
        tempCol = col;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];          
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);   
            }
        }
        
        //down
        tempRow = row - 1;
        tempCol = col;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];            
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        //left
        tempRow = row;
        tempCol = col - 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];       
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        //right
        tempRow = row;
        tempCol = col + 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];   
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        //up left
        tempRow = row + 1;
        tempCol = col - 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];            
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        //up right
        tempRow = row + 1;
        tempCol = col + 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        //down left
        tempRow = row - 1;
        tempCol = col - 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        //down right
        tempRow = row - 1;
        tempCol = col + 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol]; 
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
            }
        }
        
        return squaresAttacking;
    }
    
    public boolean canCastleKingSide(Game game){
        Rook kingSideRook = game.getKingSideRook(color);
        Square[][] board = game.getBoard();
        
        COLOR enemyColor = null;    
        switch(color){
            case WHITE:
                if(!game.getWhitePieces().contains(kingSideRook)){
                    return false;   
                }
                
                enemyColor = COLOR.BLACK;
                if(board[7][5].getPiece() != null || game.isSquareUnderAttack(enemyColor, 7, 5)){
                    return false;
                }
                if(board[7][6].getPiece() != null || game.isSquareUnderAttack(enemyColor, 7, 6)){
                    return false;
                }  
                break;
            case BLACK:
                if(!game.getBlackPieces().contains(kingSideRook)){
                    return false;
                }
                
                enemyColor = COLOR.WHITE;
                if(board[0][5].getPiece() != null || game.isSquareUnderAttack(enemyColor, 0, 5)){
                    return false;
                }
                if(board[0][6].getPiece() != null || game.isSquareUnderAttack(enemyColor, 0, 6)){
                    return false;
                }                 
                break;
        }
        
        if(hasMoved()){
            return false;
        }
        
        if(kingSideRook.hasMoved()){
            return false;
        }
        
        if(game.isKingInCheck(color)){
            return false;
        }
        
        return true;
    }
    
    public boolean canCastleQueenSide(Game game){
        Rook queenSideRook = game.getQueenSideRook(color);
        Square[][] board = game.getBoard();
        
        COLOR enemyColor = null;    
        switch(color){
            case WHITE:
                if(!game.getWhitePieces().contains(queenSideRook)){
                    return false;
                }
                enemyColor = COLOR.BLACK;
                if(board[7][3].getPiece() != null || game.isSquareUnderAttack(enemyColor, 7, 3)){
                    return false;
                }
                if(board[7][2].getPiece() != null || game.isSquareUnderAttack(enemyColor, 7, 2)){
                    return false;
                } 
                if(board[7][1].getPiece() != null){
                    return false;
                }
                break;
            case BLACK:
                if(!game.getBlackPieces().contains(queenSideRook)){
                    return false;
                }
                enemyColor = COLOR.WHITE;
                if(board[0][3].getPiece() != null || game.isSquareUnderAttack(enemyColor, 0, 3)){
                    return false;
                }
                if(board[0][2].getPiece() != null || game.isSquareUnderAttack(enemyColor, 0, 2)){
                    return false;
                }     
                if(board[0][1].getPiece() != null){
                    return false;
                }
                break;
        }
        
        if(hasMoved()){
            return false;
        }
        
        if(queenSideRook.hasMoved()){
            return false;
        }
        
        if(game.isKingInCheck(color)){
            return false;
        }
        
        return true;
    }
    
    public String castleKingSide(Game game, boolean testMove){
        Square[][] board = game.getBoard();

        Rook kingSideRook = game.getKingSideRook(color);       
        String ret = super.movePiece(game, getRow(), 6, testMove);
        kingSideRook.movePiece(game, getRow(), 5, testMove);
        return ret;
    }
    
    public String castleQueenSide(Game game, boolean testMove){
        Square[][] board = game.getBoard();

        Rook queenSideRook = game.getQueenSideRook(color);       
        String ret = super.movePiece(game, getRow(), 2, testMove);
        queenSideRook.movePiece(game, getRow(), 3, testMove);
        return ret;
    }
    
    
    @Override
    public ArrayList<Square> getValidMoves(Game game){         
        ArrayList<Square> validMoves = super.getValidMoves(game);
        Square[][] board = game.getBoard();
        
        if(canCastleKingSide(game)){          
            Square square = board[getRow()][6];
            validMoves.add(square);         
        }
        
        if(canCastleQueenSide(game)){
            Square square = board[getRow()][2];
            validMoves.add(square);
        }
        
        return validMoves;
    }
    
    @Override
    public String toString(){
        switch(color)
        {
            case WHITE:
                return "WhiteKing";
            case BLACK:
                return "BlackKing";                        
        }
        return "";
    }
}
