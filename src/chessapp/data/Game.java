/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.data;
import chessapp.data.pieces.*;
import java.util.ArrayList;

/**
 *
 * @author Toothpaste666
 */

public class Game {
    private Square[][] board;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private King whiteKing;
    private King blackKing;
    private Rook whiteKingSideRook;
    private Rook whiteQueenSideRook;
    private Rook blackKingSideRook;
    private Rook blackQueenSideRook;
    private COLOR currentTurn;
    
    private ArrayList<Piece> capturedWhitePieces;
    private ArrayList<Piece> capturedBlackPieces;
    
    public Game()
    {
        whitePieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();
        capturedWhitePieces = new ArrayList<Piece>();
        capturedBlackPieces = new ArrayList<Piece>();
        
        whiteQueenSideRook = new Rook(COLOR.WHITE, 7 ,0);
        Knight whiteKnight1 = new Knight(COLOR.WHITE, 7, 1);
        Bishop whiteBishop1 = new Bishop(COLOR.WHITE, 7, 2);
        Queen whiteQueen = new Queen(COLOR.WHITE, 7, 3);
        whiteKing = new King(COLOR.WHITE, 7, 4);
        Bishop whiteBishop2 = new Bishop(COLOR.WHITE, 7, 5);
        Knight whiteKnight2 = new Knight(COLOR.WHITE, 7, 6);
        whiteKingSideRook = new Rook(COLOR.WHITE, 7, 7);
        
        whitePieces.add(whiteQueenSideRook);
        whitePieces.add(whiteKnight1);
        whitePieces.add(whiteBishop1);
        whitePieces.add(whiteQueen);
        whitePieces.add(whiteKing);
        whitePieces.add(whiteBishop2);
        whitePieces.add(whiteKnight2);
        whitePieces.add(whiteKingSideRook);
        
        blackQueenSideRook = new Rook(COLOR.BLACK, 0 ,0);
        Knight blackKnight1 = new Knight(COLOR.BLACK, 0, 1);
        Bishop blackBishop1 = new Bishop(COLOR.BLACK, 0, 2);
        Queen blackQueen = new Queen(COLOR.BLACK, 0, 3);
        blackKing = new King(COLOR.BLACK, 0, 4);
        Bishop blackBishop2 = new Bishop(COLOR.BLACK, 0, 5);
        Knight blackKnight2 = new Knight(COLOR.BLACK, 0, 6);
        blackKingSideRook = new Rook(COLOR.BLACK, 0, 7);
        
        blackPieces.add(blackQueenSideRook);
        blackPieces.add(blackKnight1);
        blackPieces.add(blackBishop1);
        blackPieces.add(blackQueen);
        blackPieces.add(blackKing);
        blackPieces.add(blackBishop2);
        blackPieces.add(blackKnight2);
        blackPieces.add(blackKingSideRook);
        
        Pawn[] whitePawns = new Pawn[8];
        Pawn[] blackPawns = new Pawn[8];
               
        for(int i = 0; i < 8; i++){
           blackPawns[i] = new Pawn(COLOR.BLACK, 1, i);
           whitePawns[i] = new Pawn(COLOR.WHITE, 6, i);
           blackPieces.add(blackPawns[i]);
           whitePieces.add(whitePawns[i]);
        }
        
                
        board = new Square[8][8];
        for(int row = 2; row < 6; row++){
            for(int col = 0; col < 8; col++){
                board[row][col] = new Square(null, row, col);
            }
        }
        
        for(int i = 0; i < 8; i++)
            board[6][i] = new Square(whitePawns[i], 6, i);
        
        board[7][0] = new Square(whiteQueenSideRook, 7, 0);
        board[7][1] = new Square(whiteKnight1, 7, 1);
        board[7][2] = new Square(whiteBishop1, 7, 2);
        board[7][3] = new Square(whiteQueen, 7, 3);
        board[7][4] = new Square(whiteKing, 7, 4);
        board[7][5] = new Square(whiteBishop2, 7, 5);
        board[7][6] = new Square(whiteKnight2, 7, 6);
        board[7][7] = new Square(whiteKingSideRook, 7, 7);
        
        for(int i = 0; i < 8; i++)
            board[1][i] = new Square(blackPawns[i], 1, i);
        
        board[0][0] = new Square(blackQueenSideRook, 0, 0);
        board[0][1] = new Square(blackKnight1, 0, 1);
        board[0][2] = new Square(blackBishop1, 0, 2);
        board[0][3] = new Square(blackQueen, 0, 3);
        board[0][4] = new Square(blackKing, 0, 4);
        board[0][5] = new Square(blackBishop2, 0, 5);
        board[0][6] = new Square(blackKnight2, 0, 6);
        board[0][7] = new Square(blackKingSideRook, 0, 7);
        
        currentTurn = COLOR.WHITE;
    }
    
    public COLOR getCurrentTurn(){
        return currentTurn;
    }
    
    public void setCurrentTurn(COLOR color){
        currentTurn = color;
    }
    
    public Rook getQueenSideRook(COLOR color){
        if(color == COLOR.WHITE){
            return whiteQueenSideRook;
        }
        return blackQueenSideRook;
    }
    
    public Rook getKingSideRook(COLOR color){
        if(color == COLOR.WHITE){
            return whiteKingSideRook;
        }
        return blackKingSideRook;
    }
    
    public Square[][] getBoard(){
        return board;
    }
    
    public Square getSquare(int row, int col)
    {
        return board[row][col];
    }
    
    public ArrayList<Piece> getWhitePieces(){
        return whitePieces;
    }
    
    public ArrayList<Piece> getBlackPieces(){
        return blackPieces;
    }
    
    public ArrayList<Piece> getCapturedWhitePieces(){
        return capturedWhitePieces;
    }
    
    public ArrayList<Piece> getCapturedBlackPieces(){
        return capturedBlackPieces;
    }
    
    public static boolean isSquareInBounds(int row, int col){
        if(row > 7 || row < 0)
            return false;
        if(col > 7 || col < 0)
            return false;
        return true;
    }
    
    public boolean isSquareUnderAttack(COLOR attackingColor, int row, int col){
        Square square = board[row][col];
        ArrayList<Piece> attackingPieces = null;
        
        switch(attackingColor){
            case WHITE:
                attackingPieces = whitePieces;
                break;
            case BLACK:
                attackingPieces = blackPieces;
                break;
        }
        
        ArrayList<Square> attackedSquares;
        for(Piece piece : attackingPieces){
            attackedSquares = piece.getSquaresAttacking(this);
            for(Square attackedSquare : attackedSquares){
                if(attackedSquare == square){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isKingInCheck(COLOR color){
        King king = null;
        COLOR attackingColor = null;
        
        switch(color){
            case WHITE:
                king = whiteKing;
                attackingColor = COLOR.BLACK;
                break;
            case BLACK:
                king = blackKing;
                attackingColor = COLOR.WHITE;
                break;
        }
        
        return isSquareUnderAttack(attackingColor, king.getRow(), king.getCol());
    }
    
    private boolean hasNoLegalMoves(COLOR color){
        ArrayList<Piece> pieces = null;
        ArrayList<Square> validMoves = null;
        
        switch(color){
            case WHITE:
                pieces = whitePieces;
                break;
            case BLACK:
                pieces = blackPieces;
                break;
        }
        
        for(Piece piece : pieces){
            validMoves = piece.getValidMoves(this);
            if(validMoves.size() != 0){
                return false;
            }            
        }
        
        return true;
    }
    
    public boolean isKingInCheckmate(COLOR color){
        if(isKingInCheck(color) && hasNoLegalMoves(color)){
            return true;
        }
        return false;
    }
    
    public boolean isKingInStalemate(COLOR color){
        if(!isKingInCheck(color) && hasNoLegalMoves(color)){
            return true;
        }
        return false;
    }
    
    public  boolean insufficientMaterial(){
        //king vs king
        if(whitePieces.size() == 1 && blackPieces.size() == 1){
            return true;
        }
        
        //white has only king
        if(whitePieces.size() == 1){
            //black has one piece besides his king
            if(blackPieces.size() == 2){
                for(Piece piece : blackPieces){
                    if(piece.getType() == TYPE.BISHOP){
                        return true; //the other piece is a bishop
                    }else if(piece.getType() == TYPE.KNIGHT){
                        return true; //the other piece is a knight
                    }
                }
                
                
            }
        }
        
        //black has only king
        if(blackPieces.size() == 1){
            //white has one piece besides his king
            if(whitePieces.size() == 2){
                for(Piece piece : whitePieces){
                    if(piece.getType() == TYPE.BISHOP){
                        return true; //the other piece is a bishop
                    }else if(piece.getType() == TYPE.KNIGHT){
                        return true; //the other piece is a knight
                    }
                }
                
                
            }
        }
        
        //TODO:: kings and same color bishops
        
        return false;
    }
    
    public void clearPawnStatus(COLOR color){
        ArrayList<Piece> pieces = null;
        Pawn pawn = null;
        
        switch(color){
            case WHITE:
                pieces = getWhitePieces();
                break;
            case BLACK:
                pieces = getBlackPieces();
                break;
        }
        
        for(Piece piece : pieces){
            if(piece.getType() == TYPE.PAWN){
                pawn = (Pawn)piece;
                pawn.setDoubleMovedLastTurn(false);
            }
        }
    }
}
