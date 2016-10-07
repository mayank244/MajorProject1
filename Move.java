/** The Move class represent moves on a chessboard by storing the 
    initial and ending coordinates of the move, as well as the piece
	being moved **/
	
public class Move implements Comparable{
	// The start/end coordinates of the move
	public int start_row;
	public int start_col;
	public int end_row;
	public int end_col;
	
	// The piece making the move
	public Piece piece; 
	
	// The piece (if any) killed during the move
	public Piece killedPiece;
	
	// Statistics
	int nextIndex;
	double score;
	long time;
	long evaluated;
	
	// Flag whether this is an actual move or a "protects" move used for board scoring
	boolean real;
	
	// Chess notation representation of the move
	String notation;
	
	// Flags of whether this move is a castling one
	boolean[] leftCastle = new boolean[2];
	boolean[] rightCastle = new boolean[2];
	
	// Flag of whether this move is an en passant capture move
	int enpassant;
	
	// Creates a new Move with starting coordinates (sr,sc) and ending coordinates (er,ec)
	// and piece p being moved
	public Move(int sr, int sc, int er, int ec, Piece p) {
		start_row = sr;
		start_col = sc;
		end_row = er;
		end_col = ec;
		piece = p;
		real = true;
	}
	
	// Creates a "deep" copy of this Move
	public Move getCopy() {
		Move copy = new Move(start_row, start_col, end_row, end_col, piece);
		copy.real = real;
		copy.score = score;
		copy.nextIndex = nextIndex;
		copy.killedPiece = killedPiece;
		//System.out.print(6);
		return copy;
	}
	
	// Returns whether this Move is equivalent to the passed in move
	// If any of the passed in parameters is -1, it's a wildcard and
	// so that cooridinate need not match
	public boolean matches(int sr, int sc, int er, int ec, Piece p) {
		if(p.type!=piece.type)return false;
		if(sr!=start_row&&sr!=-1)return false;
		if(sc!=start_col&&sc!=-1)return false;
		if(er!=end_row&&er!=-1)return false;
		if(ec!=end_col&&ec!=-1)return false;
		return true;
	} 
	
	// Returns true if the given move matches this move (see above function)
	public boolean matches(Move m) {
		//System.out.print(7);
		return matches(m.start_row,m.start_col,m.end_row,m.end_col,m.piece);
	}
	
	// Compares the score of this move to move m (after scores were calculated) 
	public int compareTo(Object m) {
		return (int)(((Move)(m)).score*1000-1000*score);
	}
	
	// Returns a chess notation of the move
	public String toString() {
		notation = "";
		//System.out.print(8);
		switch(piece.type) {
			case Piece.ROOK:
				notation = "R";
				break;
			case Piece.QUEEN:
				notation = "Q";
				break;
			case Piece.KING:
				if(start_col-end_col==2)return "O-0-O";
				if(end_col-start_col==2)return "O-O";
				notation = "K";
				break;
			case Piece.BISHOP:
				notation = "B";
				break;
			case Piece.KNIGHT:
				notation = "N";
				break;
		}
		String[] column = {"a","b","c","d","e","f","g","h"};
		String connector = " - ";
		if(killedPiece!=null)if(killedPiece.type!=Piece.EMPTY)connector = " x ";
		notation = notation + column[start_col] + (8-start_row) + connector + column[end_col] + (8-end_row); 
		return notation;
	}
	
	// Returns move time/space statistics after move was calculated in the process of finding the next best move
	public String getInfo() {
		//System.out.print(9);
		return toString() + " in " + time + " ms and evaluating " + evaluated + " positions";
	}
}