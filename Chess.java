// Abstract implementation of the rules of Chess
// Static functions return allowable moves on a board
// Functions also determine legal moves and whether king is in check

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public final class Chess {
	
	// Returns true if Move move on ChessBoard board is a legal move
	// If allowProtects is true, capturing own pieces is valid (used for board scoring)
	static boolean isValidSpot(ChessBoard board, Move move, boolean allowProtects) {
		if(board.isEmpty(move.end_row,move.end_col))return true;
		if(board.getPiece(move.end_row,move.end_col).color!=move.piece.color)return true;
		if(allowProtects) {
			move.real = false;
			return true;
		}
		return false;
	}
	
	// Returns true if the king of the given color is safe on the board
	static boolean isKingSafe(ChessBoard board, boolean color) {
		int icolor = 0;
		if(!color)icolor = 1;
		int row = board.kingRow[icolor];
		int col = board.kingCol[icolor];
		
		// PAWN THREATS
		if(color) { // WHITE
			if(board.getPiece(row-1,col-1).color!=color&&board.getPiece(row-1,col-1).type==Piece.PAWN)return false;
			if(board.getPiece(row-1,col+1).color!=color&&board.getPiece(row-1,col+1).type==Piece.PAWN)return false;
		} else { // BLACK
			if(board.getPiece(row+1,col-1).color!=color&&board.getPiece(row+1,col-1).type==Piece.PAWN)return false;
			if(board.getPiece(row+1,col+1).color!=color&&board.getPiece(row+1,col+1).type==Piece.PAWN)return false;
		}
		
		// ADJACENT KING ILLEGALITY
		for(int i=-1;i<2;i++)
			for(int j=-1;j<2;j++) {
				if(i==0&&j==0)continue;
				if(board.getPiece(row+i,col+j).type==Piece.KING)return false;
			}
		
		// KNIGHT THREATS
		if(board.getPiece(row+1,col-2).color!=color&&board.getPiece(row+1,col-2).type==Piece.KNIGHT)return false;
		if(board.getPiece(row+1,col+2).color!=color&&board.getPiece(row+1,col+2).type==Piece.KNIGHT)return false;
		if(board.getPiece(row-1,col-2).color!=color&&board.getPiece(row-1,col-2).type==Piece.KNIGHT)return false;
		if(board.getPiece(row-1,col+2).color!=color&&board.getPiece(row-1,col+2).type==Piece.KNIGHT)return false;
		if(board.getPiece(row+2,col-1).color!=color&&board.getPiece(row+2,col-1).type==Piece.KNIGHT)return false;
		if(board.getPiece(row+2,col+1).color!=color&&board.getPiece(row+2,col+1).type==Piece.KNIGHT)return false;
		if(board.getPiece(row-2,col-1).color!=color&&board.getPiece(row-2,col-1).type==Piece.KNIGHT)return false;
		if(board.getPiece(row-2,col+1).color!=color&&board.getPiece(row-2,col+1).type==Piece.KNIGHT)return false;
		
		// LEFT HORIZONTAL THREATS
		if(board.getPiece(row-1,col).type==Piece.KING)return false;
		for(int i = row-1;i>=0;i--) {
			Piece p = board.getPiece(i,col);
			if(p.color!=color&&(p.type==Piece.ROOK||p.type==Piece.QUEEN))return false;
			else {
				if(!board.isEmpty(i,col))break;
			}
		}
		
		// RIGHT HORIZONTAL THREATS
		if(board.getPiece(row+1,col).type==Piece.KING)return false;
		for(int i = row+1;i<=7;i++) {
			Piece p = board.getPiece(i,col);
			if(p.color!=color&&(p.type==Piece.ROOK||p.type==Piece.QUEEN))return false;
			else {
				if(!board.isEmpty(i,col))break;
			}
		}
		
		// UPPER VERTICAL THREATS
		if(board.getPiece(row,col-1).type==Piece.KING)return false;
		for(int i = col-1;i>=0;i--) {
			Piece p = board.getPiece(row,i);
			if(p.color!=color&&(p.type==Piece.ROOK||p.type==Piece.QUEEN))return false;
			else {
				if(!board.isEmpty(row,i))break;
			}
		}
		
		// LOWER VERTICAL THREATS
		if(board.getPiece(row,col-1).type==Piece.KING)return false;
		for(int i = col+1;i<=7;i++) {
			Piece p = board.getPiece(row,i);
			if(p.color!=color&&(p.type==Piece.ROOK||p.type==Piece.QUEEN))return false;
			else {
				if(!board.isEmpty(row,i))break;
			}
		}
		
		// DIAGONAL THREATS
		for(int x=-1;x<2;x+=2)
			for(int y=-1;y<2;y+=2)
				if(!isDiagonalKingSafe(board, color, col, row, x, y))return false;
				
		return true;
	}
	
	// Returns true if the king of the given color is safe from diagonal threats
	static boolean isDiagonalKingSafe(ChessBoard board, boolean color, int col, int row, int ri, int ci) {
		if(board.getPiece(row+ri,col+ci).type==Piece.KING)return false;
		for(int i=1;i<=7;i++) {
			Piece p = board.getPiece(row+i*ri,col+i*ci);
			if(p.color!=color&&(p.type==Piece.BISHOP||p.type==Piece.QUEEN))return false;
			else {
				if(!board.isEmpty(row+i*ri,col+i*ci))break;
			}
		}
		return true;
	}
	
	// Determines is the given pawn move is legal on the board
	static boolean isPawnMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(move.piece.color==Piece.WHITE) { // White moves "up" the board
			if(move.start_col==move.end_col) { // Forward movement
				if(!board.isEmpty(move.start_row-1,move.start_col))return false;
				if(move.start_row-1==move.end_row)return true;
				if(move.start_row-2==move.end_row&&move.start_row==6) { // Initial move
					if(board.isEmpty(move.end_row,move.end_col))return true;
				} 
				return false;
			} else { // Diagonal attack 
				if(move.end_row!=move.start_row-1)return false;
				if(Math.abs(move.end_col-move.start_col)!=1)return false;
				if(board.isEmpty(move.end_row,move.end_col)){
					if(move.start_row==3) { // Possible en passant
						if(board.enpassant==move.end_col)return true;
						return false;
					}
				}
				else {
					if(board.getPiece(move.end_row,move.end_col).color!=move.piece.color)return true;
					if(allowProtects){
						move.real = false;
						return true;
					}
				}
			}
		} else { // Black moves "down" the board
			if(move.start_col==move.end_col) { // Forward movement
				if(!board.isEmpty(move.start_row+1,move.start_col))return false;
				if(move.start_row+1==move.end_row)return true;
				if(move.start_row+2==move.end_row&&move.start_row==1) { // Initial move
					if(board.isEmpty(move.end_row,move.end_col))return true;
				} 
				return false;
			} else { // Diagonal attack 
				if(move.end_row!=move.start_row+1)return false;
				if(Math.abs(move.end_col-move.start_col)!=1)return false;
				if(board.isEmpty(move.end_row,move.end_col)) {
					if(move.start_row==4) { // Possible en passant
						if(board.enpassant==move.end_col)return true;
						return false;
					}
				}
				else {
					if(board.getPiece(move.end_row,move.end_col).color!=move.piece.color)return true;
					if(allowProtects) {
						move.real = false;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Determines is the given bishop move is legal on the board
	static boolean isBishopMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(!isValidSpot(board, move, allowProtects))return false;
		if(Math.abs(move.start_row-move.end_row)!=Math.abs(move.start_col-move.end_col))return false;
		int rowSlope = 1;	
		if(move.end_row<move.start_row)rowSlope = -1;
		int colSlope = 1;
		int col = move.start_col;
		if(move.end_col<move.start_col)colSlope = -1;
		for(int row=move.start_row+rowSlope;row<7;row+=rowSlope) {
			col+=colSlope;
			if(col==move.end_col)break;
			if(!board.isEmpty(row,col))return false;
		}
		return true;
	}
	
	// Determines is the given knight move is legal on the board
	static boolean isKnightMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(!isValidSpot(board, move, allowProtects))return false;
		int rowd = Math.abs(move.start_row-move.end_row);
		int cold = Math.abs(move.start_col-move.end_col);
		if((rowd==2&&cold==1)||(rowd==1&&cold==2))return true;
		return false;
	}
	
	// Determines is the given rook move is legal on the board
	static boolean isRookMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(!isValidSpot(board, move, allowProtects))return false;
		if(move.start_row==move.end_row) {
			int colSlope = 1;
			if(move.end_col<move.start_col)colSlope = -1;
			for(int col=move.start_col+colSlope;col<7;col+=colSlope) {
				if(col==move.end_col)break;
				if(!board.isEmpty(move.start_row,col))return false;
			}
			return true;
		}
		else if(move.start_col==move.end_col) {
			int rowSlope = 1;
			if(move.end_row<move.start_row)rowSlope = -1;
			for(int row=move.start_row+rowSlope;row<7;row+=rowSlope) {
				if(row==move.end_row)break;
				if(!board.isEmpty(row,move.start_col))return false;
			}
			return true;		
		}
		return false;
	}
	
	// Determines is the given queen move is legal on the board
	static boolean isQueenMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(isBishopMoveLegal(board,move,allowProtects))return true;
		return isRookMoveLegal(board,move,allowProtects);
	}
	
	// Determines is the given king move is legal on the board
	static boolean isKingMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(Math.abs(move.start_row-move.end_row)>1)return false;
		if(move.start_col-move.end_col>1) {
			//if(move.piece.color&&!board.leftCastle[0])return false;
			//if(!move.piece.color&&!board.leftCastle[1])return false;
			if(move.start_col==move.end_col+2) {
				if(!board.isEmpty(move.end_row,3))return false;
				if(!board.isEmpty(move.end_row,2))return false;
				if(!board.isEmpty(move.end_row,1))return false;
				if(!isKingSafe(board,move.piece.color))return false;
				return true;	
			}
			return false;
		}
		if(move.start_col-move.end_col<-1) {
			if(move.piece.color&&!board.rightCastle[0])return false;
			if(!move.piece.color&&!board.rightCastle[1])return false;
			if(move.start_col==move.end_col-2) {
				if(!board.isEmpty(move.end_row,5))return false;
				if(!board.isEmpty(move.end_row,6))return false;
				if(!isKingSafe(board,move.piece.color))return false;
				return true;	
			}
		}
		return isValidSpot(board, move, allowProtects);			
	}
	
	// Returns the set of all legal moves of the piece sitting at the given row and column on the board
	public static ArrayList getAllSpotMoves(ChessBoard board, boolean color, int row, int col) {
		return getAllSpotMoves(board, color, row, col, false);
	}
	
	// Returns the set of all legal moves of the piece sitting at the given row and column on the board
	// If allowProtects is true, capturing own pieces is allowed (used for board scoring)
	public static ArrayList getAllSpotMoves(ChessBoard board, boolean color, int row, int col, boolean allowProtects) {
		ArrayList moves = new ArrayList();
		Piece p = board.getPiece(row,col);
		if(p.color==color) {
			Piece thisPiece = new Piece(p.type,p.color);
			Move test = null;
			switch(p.type) {
				case Piece.PAWN:
					if(color) { // WHITE
						test = new Move(row,col,row-1,col,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						test = new Move(row,col,row-1,col+1,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						test = new Move(row,col,row-1,col-1,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						if(row==6) {
							test = new Move(row,col,row-2,col,thisPiece);
							if(isMoveLegal(board,test,allowProtects))moves.add(test);
						}
					} else { // BLACK
						test = new Move(row,col,row+1,col,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						test = new Move(row,col,row+1,col+1,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						test = new Move(row,col,row+1,col-1,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						if(row==1) {
							test = new Move(row,col,row+2,col,thisPiece);
							if(isMoveLegal(board,test,allowProtects))moves.add(test);
						}
					}
					break;
				case Piece.BISHOP:
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row+i,col+i,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row-i,col+i,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row+i,col-i,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row-i,col-i,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					break;
				case Piece.KNIGHT:
					int[] rowOffsets = {2,-2,-2,2,-1,1,-1,1};
					int[] colOffsets = {-1,1,-1,1,-2,2,2,-2};
					for(int i=0;i<8;i++) {
						test = new Move(row,col,row+rowOffsets[i],col+colOffsets[i],thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
					}
					break;
				case Piece.ROOK:
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row+i,col,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row-i,col,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row,col+i,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					for(int i=1;i<8;i++) {
						test = new Move(row,col,row,col-i,thisPiece);
						if(isMoveLegal(board,test,allowProtects))moves.add(test);
						else break;
					}
					break;
				case Piece.QUEEN:
					for(int ri=-1;ri<2;ri++) {
						for(int ci=-1;ci<2;ci++) {
							for(int i=1;i<8;i++) {
								test = new Move(row,col,row+i*ri,col+i*ci,thisPiece);
								if(isMoveLegal(board,test,allowProtects))moves.add(test);
								else break;
							}
						}
					}
					break;
				case Piece.KING:
					for(int r=-1;r<2;r++)
						for(int c=-1;c<2;c++) {
							test = new Move(row,col,row+r,col+c,thisPiece);
							if(isMoveLegal(board,test,allowProtects))moves.add(test);
						}
					/* Castling below
					test = new Move(row,col,row,col-2,thisPiece);
					if(isMoveLegal(board,test,allowProtects))moves.add(test);
					test = new Move(row,col,row,col+2,thisPiece);
					if(isMoveLegal(board,test,allowProtects))moves.add(test);
					break;
                               */
			}
		}
		return moves;
	}
	
	public static ArrayList getAllMoves(ChessBoard board, boolean color) {
		return getAllMoves(board, color, false);
	}
	
	// Returns all moves by player with the given color on the board
	// If allowProtects is true, capturing own pieces is allowed (used for board scoring)
	public static ArrayList getAllMoves(ChessBoard board, boolean color, boolean allowProtects) {
		ArrayList moves = new ArrayList();
		for(int row=0;row<8;row++)
			for(int col=0;col<8;col++) {
				ArrayList spotMoves = getAllSpotMoves(board, color, row, col, allowProtects);	
				for(int i=0;i<spotMoves.size();i++)moves.add(spotMoves.get(i));
			}
		return moves;
	}
	
	// Determines if the given move is legal on the given board
	public static boolean isMoveLegal(ChessBoard board, Move move) {
		return isMoveLegal(board, move, false);
	}
	
	// Determines if the given move is legal on the given board
	// If allowProtects is true, capturing own pieces is allowed (used for board-scoring)
	public static boolean isMoveLegal(ChessBoard board, Move move, boolean allowProtects) {
		if(move.start_row==move.end_row&&move.start_col==move.end_col)return false;
		if(move.end_row<0||move.end_row>7||move.end_col<0||move.end_col>7)return false;
		boolean valid = false;
		switch(move.piece.type) {
			case Piece.PAWN:
				valid = isPawnMoveLegal(board, move, allowProtects);
				break;
			case Piece.BISHOP:
				valid = isBishopMoveLegal(board, move, allowProtects);
				break;
			case Piece.KNIGHT:
				valid = isKnightMoveLegal(board, move, allowProtects);
				break;
			case Piece.ROOK:
				valid = isRookMoveLegal(board, move, allowProtects);
				break;
			case Piece.QUEEN:
				valid = isQueenMoveLegal(board, move, allowProtects);
				break;
			case Piece.KING:
				valid = isKingMoveLegal(board, move, allowProtects);
		}
		if(!valid)return false;
		return true;
	}

	public static boolean isThereAMove(ChessBoard board, boolean color) {
		ArrayList moves = getAllMoves(board,color);
		boolean yes=false;
		if(moves==null)return false;
		for(int i=0;i<moves.size();i++) {
			ChessBoard nextBoard = board.getCopy();
			nextBoard.makeMove((Move)(moves.get(i)));
			if(isKingSafe(nextBoard,color))yes=true;
		}
		return yes;
	}
}
