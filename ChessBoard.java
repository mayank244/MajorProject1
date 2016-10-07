// Abstact (i.e. non-graphical) implementation of the chess board
// Keeps track of where all the pieces are

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

public class ChessBoard {
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	
	// The board is an 8 by 8 array of pieces
	Piece[][] board = new Piece[8][8];
	
	// Is castling allowed for the given player?
	boolean[] leftCastle = new boolean[2];
	boolean[] rightCastle = new boolean[2];
	
	// Where is the king of each player?
	public int[] kingRow = new int[2];
	public int[] kingCol = new int[2];
	
	// Is enpassant allowed now? (which column?)
	int enpassant;
	
	// Has a piece been promoted? (where? needed for taking back moves)
	int promotePiece;
	
	// Constructor creates a blank board
	public ChessBoard() {
		clear();
	}
	
	// Creates a "deep" copy of this chessboard
	public ChessBoard getCopy()  {
		ChessBoard copy = new ChessBoard();
		copy.board = new Piece[8][8];
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				copy.board[i][j] = board[i][j];
		//copy.leftCastle = (boolean[])leftCastle.clone();
		//	copy.rightCastle = (boolean[])rightCastle.clone();
		//	copy.enpassant = enpassant;
		copy.promotePiece = promotePiece;
		copy.kingRow = (int[])kingRow.clone();
		copy.kingCol = (int[])kingCol.clone();		
		return copy;
	}
	
	// Clears the board completely
	public void clear() {
                //System.out.print(3);
		board = new Piece[8][8];
		for(int col=0;col<8;col++)
			for(int row=0;row<8;row++)
				board[col][row] = new Piece();
		leftCastle[BLACK] = true;
		rightCastle[BLACK] = true;
		leftCastle[WHITE] = true;
		rightCastle[WHITE] = true;
		enpassant = -1;
		promotePiece = Piece.QUEEN;
	}
	
	// Initializes the board to the state it should be at at the beginning of a chess game
	public void reset() {
		leftCastle[WHITE] = true;
		leftCastle[BLACK] = true;
		rightCastle[WHITE] = true;
		rightCastle[BLACK] = true;
		enpassant = -1;
		promotePiece = Piece.QUEEN;
		for(int i=0;i<8;i++) {
			board[i][1] = new Piece(Piece.PAWN, Piece.BLACK);
			board[i][6] = new Piece(Piece.PAWN, Piece.WHITE);
		}
                //System.out.print(4);
		boolean[] pieceColor = new boolean[8];
		pieceColor[0] = false;
		pieceColor[7] = true;
		for(int i=0;i<8;i+=7) {
			board[0][i] = new Piece(Piece.ROOK, pieceColor[i]);
			board[1][i] = new Piece(Piece.KNIGHT, pieceColor[i]);
			board[2][i] = new Piece(Piece.BISHOP, pieceColor[i]);
			board[3][i] = new Piece(Piece.QUEEN, pieceColor[i]);
			board[4][i] = new Piece(Piece.KING, pieceColor[i]);
			board[5][i] = new Piece(Piece.BISHOP, pieceColor[i]);
			board[6][i] = new Piece(Piece.KNIGHT, pieceColor[i]);
			board[7][i] = new Piece(Piece.ROOK, pieceColor[i]);		
		}	
		kingCol[WHITE] = 4;
		kingCol[BLACK] = 4;
		kingRow[WHITE] = 7;
		kingRow[BLACK] = 0;
	}
	
	// Takes back the given move on the board. Keeps careful track of 
	// the effect this has on castling and enpassant allowance, as well
	// as any dead pieces that might be returning to play
	public void reverseMove(Move move) {
		board[move.start_col][move.start_row] = move.piece;
		board[move.end_col][move.end_row] = move.killedPiece;
		enpassant = move.enpassant;
		leftCastle[BLACK] = move.leftCastle[BLACK];
		leftCastle[WHITE] = move.leftCastle[WHITE];
		rightCastle[BLACK] = move.rightCastle[BLACK];
		rightCastle[WHITE] = move.rightCastle[WHITE];
		if(move.piece.type==Piece.KING) {
			if(move.piece.color) {
				kingRow[WHITE] = move.start_row;
				kingCol[WHITE] = move.start_col;
			} else {
				kingRow[BLACK] = move.start_row;
				kingCol[BLACK] = move.start_col;
			}
			// Checks to see if it's a castling move being reversed
			if(move.start_col-move.end_col==2) {
				board[move.end_col+1][move.end_row] = new Piece();
				board[0][move.end_row] = new Piece(Piece.ROOK, move.piece.color);
			}
			else if(move.end_col-move.start_col==2) {
				board[move.end_col-1][move.end_row] = new Piece();
				board[7][move.end_row] = new Piece(Piece.ROOK, move.piece.color);
			}
		}
		else if(move.piece.type==Piece.PAWN) {
			if(move.end_col!=move.start_col) {
				if(move.killedPiece.type==0) {
					if(move.piece.color) {
						board[move.end_col][move.start_row-1] = new Piece();
						board[move.end_col][move.start_row] = new Piece(Piece.PAWN, !move.piece.color);
					} else {
						board[move.end_col][move.start_row+1] = new Piece();
						board[move.end_col][move.start_row] = new Piece(Piece.PAWN, !move.piece.color);
					}
				}
			}
		}
	}
	
	// Makes the given move on the board. Keeps track of what effect this has
	// on castling and enpassant, and records any killed pieces in case the
	// move is taken back later.
	public void makeMove(Move move) {
		/*move.enpassant = enpassant;
		move.leftCastle[BLACK] = leftCastle[BLACK];
		move.leftCastle[WHITE] = leftCastle[WHITE];
		move.rightCastle[BLACK] = rightCastle[BLACK];
		move.rightCastle[WHITE] = rightCastle[WHITE];*/
		move.killedPiece = board[move.end_col][move.end_row];
		//enpassant = -1;
		board[move.start_col][move.start_row] = new Piece();
		/*if(board[move.end_col][move.end_row].type==Piece.ROOK) {
			if(move.end_col==0&&move.end_row==0)leftCastle[BLACK]=false;
			if(move.end_col==0&&move.end_row==7)leftCastle[WHITE]=false;
			if(move.end_col==7&&move.end_row==0)rightCastle[BLACK]=false;
			if(move.end_col==7&&move.end_row==7)rightCastle[WHITE]=false;
		}*/
		if(move.piece.type==Piece.PAWN) {
			/*if(Math.abs(move.start_row-move.end_row)==2){
				// A pawn moves 2 forward so en passant is enabled for opponent
				enpassant = move.start_col;
			}*/
			if(isEmpty(move.end_row,move.end_col)&&move.start_col!=move.end_col) {
				// An en passant move
				if(move.piece.color)board[move.end_col][move.end_row+1] = new Piece();
				else board[move.end_col][move.end_row-1] = new Piece();
			}
			/*if(move.end_row==0||move.end_row==7) {
				// Pawn promotion
				board[move.end_col][move.end_row] = new Piece(promotePiece, move.piece.color);
				return;
			}*/	
		}
		/*else if(move.piece.type==Piece.ROOK) {
			// Castling for the rook is disabled
			if(move.start_col==0&&move.start_row==0)leftCastle[BLACK]=false;
			if(move.start_col==7&&move.start_row==0)rightCastle[BLACK]=false;
			if(move.start_col==0&&move.start_row==7)leftCastle[WHITE]=false;
			if(move.start_col==7&&move.start_row==7)rightCastle[WHITE]=false;
		}*/
		else if(move.piece.type==Piece.KING) {
			// Castling is disabled
			if(move.piece.color) {
				kingRow[WHITE] = move.end_row;
				kingCol[WHITE] = move.end_col;
				//leftCastle[WHITE] = false;
				//rightCastle[WHITE] = false;
			} else {
				kingRow[BLACK] = move.end_row;
				kingCol[BLACK] = move.end_col;
				//leftCastle[BLACK] = false;
				//rightCastle[BLACK] = false;
			}
			/*if(move.start_col-move.end_col==2) {
				// Castle to the left
				if(move.piece.color) {
					board[0][7] = new Piece();
					board[3][7] = new Piece(Piece.ROOK, move.piece.color);
				}
				else {
					board[0][0] = new Piece();
					board[3][0] = new Piece(Piece.ROOK, move.piece.color);
				}
			}
			if(move.start_col-move.end_col==-2) {
				// Castle to the right
				if(move.piece.color) {
					board[7][7] = new Piece();
					board[5][7] = new Piece(Piece.ROOK, move.piece.color);
				}
				else {
					board[7][0] = new Piece();
					board[5][0] = new Piece(Piece.ROOK, move.piece.color);
				}
			}*/
		}
		board[move.end_col][move.end_row] = move.piece;
	}
	
	// Returns true if the board at row r and column c is empty
	public boolean isEmpty(int r, int c) {
		if(r<0||r>7||c<0||c>7)return true;
		return board[c][r].type==Piece.EMPTY;
	}
	
	// Returns the piece sitting in column r and row c
	public Piece getPiece(int r, int c) {
		if(r<0||r>7||c<0||c>7)return new Piece();
		return board[c][r];
	}	
	
	// Places piece p at row r and column c	
	public void setPiece(int r, int c, Piece p) {
		board[c][r] = p;
	}
}
