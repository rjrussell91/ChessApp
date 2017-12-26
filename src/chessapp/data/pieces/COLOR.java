/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.data.pieces;

/**
 *
 * @author Toothpaste666
 */
public enum COLOR{
    WHITE("White"), BLACK("Black");
    
    private final String s;
    
    private COLOR(String s){
        this.s = s;
    }
    
    @Override
    public String toString(){
        return s;
    }
    
    public COLOR oppositeColor(){
        if(s.equals("White"))
            return BLACK;
        else
            return WHITE;
    }
}