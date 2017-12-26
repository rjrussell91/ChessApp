/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.data;
import chessapp.data.pieces.Piece;

/**
 *
 * @author Toothpaste666
 */

public class Square {
    private Piece piece;
    private boolean highlighted;
    private final int row;
    private final int col;
    
    public Square(Piece piece, int row, int col){
        this.piece = piece;
        highlighted = false;
        this.row = row;
        this.col = col;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public Piece getPiece(){
        return piece;
    }
    
    public void setPiece(Piece piece){
        this.piece = piece;
    }
    
    public boolean isHighlighted(){
        return highlighted;
    }
    
    public void setHighlighted(boolean highlighted){
        this.highlighted = highlighted;
    }
    
    @Override
    public String toString(){
        if(piece == null){
            return "";
        }else{
            return piece.toString();
        }
    }
}
