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
public class Knight extends Piece{
    public Knight(COLOR color, int row, int col){
        super(color, row, col);
        type = TYPE.KNIGHT;
        sortRank = 4;
        materialWorth = 3;
    }
    
    public ArrayList<Square> getSquaresAttacking(Game game){
        int tempRow, tempCol;
        Square square;
        ArrayList<Square> squaresAttacking = new ArrayList<Square>();
        Square[][] board = game.getBoard();
        
        //up left
        tempRow = row + 2;
        tempCol = col - 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //left up
        tempRow = row + 1;
        tempCol = col - 2;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //up right
        tempRow = row + 2;
        tempCol = col + 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //right up
        tempRow = row + 1;
        tempCol = col + 2;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //down left
        tempRow = row - 2;
        tempCol = col - 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //left down
        tempRow = row - 1;
        tempCol = col - 2;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //down right
        tempRow = row - 2;
        tempCol = col + 1;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        
        //right down
        tempRow = row - 1;
        tempCol = col + 2;
        if(Game.isSquareInBounds(tempRow, tempCol)){
            square = board[tempRow][tempCol];
            if(square.getPiece() == null){
                squaresAttacking.add(square);
            }else if(square.getPiece().getColor() != color){
                squaresAttacking.add(square);
            }
        }
        return squaresAttacking;
    }
    
    @Override
    public String toString(){
        //return "N";
        switch(color)
        {
            case WHITE:
                return "WhiteKnight";
            case BLACK:
                return "BlackKnight";                        
        }
        return "";
    }
}
