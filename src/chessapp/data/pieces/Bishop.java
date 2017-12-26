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
public class Bishop extends Piece{
    public Bishop(COLOR color, int row, int col){
        super(color, row, col);
        type = TYPE.BISHOP;
        sortRank = 3;
        materialWorth = 3;
    } 
    
    public ArrayList<Square> getSquaresAttacking(Game game){
        int tempRow, tempCol;
        Square square;
        ArrayList<Square> squaresAttacking = new ArrayList<Square>(); 
        Square[][] board = game.getBoard();
        
        //up left
        tempRow = row + 1;
        tempCol = col - 1;
        while(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
                break;
            }else if(square.getPiece().getColor() == color){ //square is occupied by friend
                break;
            }
            
            tempRow++;
            tempCol--;
        }
        
        //up right
        tempRow = row + 1;
        tempCol = col + 1;
        while(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
                break;
            }else if(square.getPiece().getColor() == color){ //square is occupied by friend
                break;
            }
            
            tempRow++;
            tempCol++;
        }
        
        //down left
        tempRow = row - 1;
        tempCol = col - 1;
        while(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
                break;
            }else if(square.getPiece().getColor() == color){ //square is occupied by friend
                break;
            }
            
            tempCol--;
            tempRow--;
        }
        
        //down right
        tempRow = row - 1;
        tempCol = col + 1;
        while(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            
            if(square.getPiece() == null){ //square is empty
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){ //square is occupied by enemy
                squaresAttacking.add(square);
                break;
            }else if(square.getPiece().getColor() == color){ //square is occupied by friend
                break;
            }
            
            tempCol++;
            tempRow--;
        }
        return squaresAttacking;
   }
    
    @Override
    public String toString(){
        switch(color)
        {
            case WHITE:
                return "WhiteBishop";
            case BLACK:
                return "BlackBishop";                        
        }
        return "";
    }
}
