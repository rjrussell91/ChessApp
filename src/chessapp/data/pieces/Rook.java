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
public class Rook extends Piece{
    
    public Rook(COLOR color, int row, int col){
        super(color, row, col);
        type = TYPE.ROOK;
        sortRank = 2;
        materialWorth = 5;
    }
         
    @Override
    public ArrayList<Square> getSquaresAttacking(Game game){
        int tempRow, tempCol;
        Square square;
        ArrayList<Square> squaresAttacking = new ArrayList<Square>();
        Square[][] board = game.getBoard();
        
        //up
        tempRow = row + 1;
        tempCol = col;
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
        }
        
        //down
        tempRow = row - 1;
        tempCol = col;
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
            
            tempRow--;
        }
        
        //left
        tempRow = row;
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
        }
        
        //right
        tempRow = row;
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
        }
        
        return squaresAttacking;
    }
    
        @Override
    public String toString(){
        switch(color)
        {
            case WHITE:
                return "WhiteRook";
            case BLACK:
                return "BlackRook";                        
        }
        return "";
    }
}
