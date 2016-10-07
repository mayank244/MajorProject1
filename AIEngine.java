// The main artificial intelligence part of the program
// Implements move-making and board-scoring algorithms for the computer player

import java.util.*;

public class AIEngine {
	// Null constructor; a new instance is made and destroyed at each computer move
	public AIEngine() {}
	
	ScoringGenome baseScore;
	
	// The AI associated with this engine
	AIPlayer aip;
	
	// Keeps track of various time/evaluation statistics
	int counter;
	int ndeep;
	long compTime;
			
		
	// Weights a scored board based on the ScoringGenome the given AIPlayer posesses 
	public long weightScore(ScoringGenome sg) {
		long score = 0;
		for(int i=1;i<7;i++) {
			score+=baseScore.pieceOwn[i]*sg.pieceOwn[i] + baseScore.pieceMobility[i]*sg.pieceMobility[i];
			score+=baseScore.pieceThreats[i]*sg.pieceThreats[i];
			score+=baseScore.pieceProtects[i]*sg.pieceProtects[i];
		}
		score+=baseScore.pawnAdvancement*sg.pawnAdvancement;
		return score;
	}
	
	// Computes generic scores based on the given board, and then weights them based on the ScoringGenome
	// board is the current chessboard state
	// moves is the pre-computed set of moves that are possible; can be null
	// color is the player for whom the board is being scored
	// sg is the ScoringGenome to use as weights
	public long scoreBoard(ChessBoard board, ArrayList moves, boolean color, ScoringGenome sg) {
		counter++;
		boolean finalMove = false;
		
		if(moves==null){
			// If no pre-computed available moves are passed in, they must be determined now
			finalMove = true;
			moves = Chess.getAllMoves(board,color,true);
		}
		baseScore = new ScoringGenome(0);
		for(int row=0;row<8;row++)
			for(int col=0;col<8;col++) {
				// Scans the entire board and computes scores for each pawn
				if(board.isEmpty(row,col))continue;
				Piece thisPiece = board.getPiece(row,col);
				//System.out.println(thisPiece.type);
				if(thisPiece.color==color) {
					// Scores for your own pieces are positive
					if(thisPiece.type==Piece.PAWN) {
						if(color)baseScore.pawnAdvancement+=(6-row);
						else baseScore.pawnAdvancement+=(row-1);
					}
					baseScore.pieceOwn[thisPiece.type]++;
				}
				else {
					// Scores for enemy pieces are negative
					if(thisPiece.type==Piece.PAWN) {
						if(!color)baseScore.pawnAdvancement-=(6-row);
						else baseScore.pawnAdvancement-=(row-1);
					}
					baseScore.pieceOwn[thisPiece.type]--;
				}
			}
		if(moves!=null) {
			// Scans each move, and based on this computes mobility/threats/protects
			for(int i=0;i<moves.size();i++) {
				Move theMove = ((Move)(moves.get(i)));
				Piece thisPiece = theMove.piece;
				int er = theMove.end_row;
				int ec = theMove.end_col;
				Piece endPiece = board.getPiece(er,ec);
				if(board.isEmpty(er,ec));
				else if(endPiece.color!=color)baseScore.pieceThreats[endPiece.type]++;
				else baseScore.pieceProtects[endPiece.type]++;
				if(theMove.real){
					baseScore.pieceMobility[thisPiece.type]++;
				}
			}
		}
		return weightScore(sg);
	}
	
	// Computes the null move heuristic
	/*public long nullMoveScore(ChessBoard board, boolean color, AIPlayer a, int nullPly) {
		if(nullPly>2)nullPly = 2;
		if(nullPly<1)nullPly = 1;
		nullPly = 1;
		long curScore = scoreBoard(board,null,color,aip.sg);
		if(Math.abs(curScore)>100000)curScore = -scoreBoard(board,null,!color,aip.sg);
		Move best1 = getBestMove(board, !color, Chess.getAllMoves(board,!color), null, nullPly, nullPly, -1000000, 1000000, -curScore);
		board.makeMove(best1);
		// Makes first opponent move
		curScore = scoreBoard(board,null,color,aip.sg);
		if(Math.abs(curScore)>100000)curScore = -scoreBoard(board,null,!color,aip.sg);
		Move best2 = getBestMove(board, !color, Chess.getAllMoves(board,!color), null, nullPly, nullPly, -1000000, 1000000, -curScore);
		board.makeMove(best2);
		// Makes second opponent move
		curScore = scoreBoard(board,null,color,aip.sg);
		if(Math.abs(curScore)>100000)curScore = -scoreBoard(board,null,!color,aip.sg);
		board.reverseMove(best2);
		board.reverseMove(best1);
		return curScore;
	}*/
	
	// Top level getBestMove function, called by the Game. It calls the internal getBestMove
	// recursive function with all the necessary parameters and then notifies the Game of
	// the chosen mov.
	// board is the current state of the chessboard
	// color is the player about to make a move
	// a contains the parameters of the AI player
	public Move getBestMove(ChessBoard board, boolean color, AIPlayer a) {
		aip = a;
		ndeep = 0;
		// First computes the current score, for future comparisons
		double curScore = scoreBoard(board,null,color,aip.sg);
                System.out.println(13);
		//if(Math.abs(curScore)>100000)curScore = -scoreBoard(board,null,!color,aip.sg);
		long alphamin = -10000;
		//if(aip.ply>1)alphaMin = nullMoveScore(board,color,a,aip.ply-1);
		counter = 0;
		// Keeps time/evaluation statistics
		long startTime = System.currentTimeMillis();
		// Calls internal recursive min/max algorithm
		Move best = getBestMove(board, color, Chess.getAllMoves(board,color), null, aip.ply, aip.ply, alphamin, 100000, curScore);
		if(best==null)return null;
		long endTime = System.currentTimeMillis();
		compTime = endTime-startTime;
		best.time = compTime;
		best.evaluated = counter;
		// Returns the best move
		return best;
	}
	
		
	// Internal recursive min-max alpha-beta move generating function
	// board is the current state of the chessboard
	// color is the color of the player making the move
	// moves is the set of all available moves at this point in time
	// forbidden is a set of moves that was deemed unnecessary for examination during quiescence searching (it's null otherwise)
	// ply is the current ply depth the algorithm is at
	// top is the ply at which the AI is playing (what the algorithm was called with in the first place)
	// alpha and beta are the current values for alpha-beta pruning
	// curScore is the original board score used for comparison (for triggering quiescence)
	public Move getBestMove(ChessBoard board, boolean color, ArrayList moves, ArrayList forbidden, int ply, int top , double alpha, double beta, double curScore) {
		if(ply>0) { // A ply of 0 means we are only evaluating the board, so none of the below is necessary
			if(moves.size()==0)return null;
			Move bestMove=new Move(0,0,0,0,new Piece());
			bestMove.score = -1000000;
			int nmoves = 0;
			int total = moves.size();
			ArrayList nextMoves = new ArrayList(total);
			int realindex = 0;
			boolean existsMove = false;
			Move allowMove = null;
			if(forbidden!=null)allowMove = (Move)forbidden.get(forbidden.size()-1);
			for(;nmoves<total;nmoves++) {
				// Creates a list of legal moves from this position
				if(forbidden!=null) {
					// Ensures queiescence forbidden moves are not included
					Move theMove = (Move)(moves.get(nmoves));
					for(int j=0;j<forbidden.size()-1;j++) {
						Move forbidMove = (Move)forbidden.get(j);
						if(forbidMove.matches(theMove)) {
							if(!((allowMove.end_row==theMove.end_row&&allowMove.end_col==theMove.end_col)||(allowMove.end_row==theMove.start_row&&allowMove.end_col==theMove.start_col))) {
								theMove.real = false;
								break;
							}
						}
					}
				}
				if(((Move)(moves.get(nmoves))).real&&(ply>1||top==1)) {
					Move theMove = (Move)(moves.get(nmoves));
					board.makeMove(theMove);
					if(ply==top) {
						// Ensures a move is not illegal because the king will be under check
						if(!Chess.isKingSafe(board,color)) {
							theMove.real = false;
							board.reverseMove(theMove);
							continue;
						}
						existsMove = true;
					}
					nextMoves.add(Chess.getAllMoves(board,!color, true));
					theMove.nextIndex = realindex;
					// Estimates score of making this move (at 1 ply depth; used for alpha beta sorting)
					theMove.score = scoreBoard(board,(ArrayList)nextMoves.get(realindex),color,aip.sg);
					board.reverseMove(theMove);
					realindex++;
				} else if(ply==1){
					// Don't unnecessarily score future boards when we are at the final depth
					nextMoves.add(new ArrayList());
					((Move)(moves.get(nmoves))).nextIndex = realindex;
					realindex++;
				}
			}
			// Make sure there is at least one move
			if(ply==top&&!existsMove)return null;
			if(ply>1) {
				// Sorts moves for alpha beta
				// "quiescent" (stand pat) moves are placed first in the order
				Move[] test = new Move[moves.size()];
				int size = moves.size();
				for(int i=0;i<size;i++){
					test[i]=(Move)moves.get(i);
					test[i].score = Math.abs(test[i].score);
				}
				Arrays.sort(test);
				moves = new ArrayList(size);
				for(int i=0;i<size;i++)moves.add(test[size-i-1]);
			}
			// Now there are two choices at the depth 
			// Either it's your own move (MAXimizing)...
			// ...Or it's the opponent's move (MINimizing)
			if(ply%2!=top%2) { // opponent move
				bestMove.score = 1000000;
				int rnmoves = nmoves;
				for(int i=0;i<rnmoves;i++) { // Go through all possible branches
					Move realMove = (Move)(moves.get(i));
					if(!realMove.real)continue; // Skip fake moves (i.e. protects)
					board.makeMove(realMove);
					// Recursively go down into the branch
					Move nextMove = getBestMove(board,!color,(ArrayList)nextMoves.get((realMove).nextIndex),null,ply-1,top,alpha,beta,curScore);
					if(ply==1) {
						// If we are at the bottom, quiescence might be possible
						if(nextMove==null){
							board.reverseMove(realMove);
							realMove.score = -10000;
							return realMove;
						}
						if(Math.abs(nextMove.score-curScore)>aip.deepScore){
							if(top!=aip.deepPly&&ndeep<aip.deepScore) {	
								// Quiescence was triggered
								ndeep++;
								ArrayList forbidMoves = null;
								board.reverseMove(realMove);
								// Find out what moves shouldn't be looked at in the future
								forbidMoves = Chess.getAllMoves(board,!color);
								forbidMoves.add(realMove);
								board.makeMove(realMove);
								Move oldnextMove = nextMove.getCopy();
								// Call the quiescence search recursively
								nextMove = getBestMove(board,!color,Chess.getAllMoves(board,!color),forbidMoves,1,top+1,alpha,beta,nextMove.score);
								if(nextMove==null)nextMove = oldnextMove;
							}
						}
					}
					if(nextMove==null){ // No valid moves possible
						board.reverseMove(realMove);
						realMove.score = -10000;
						return realMove;
					}
					if(nextMove.score<alpha){ // Alpha cutoff lets us escape early from this iteration
						board.reverseMove(realMove);
						realMove.score = nextMove.score;
						return realMove;
					}
					if(nextMove.score<bestMove.score){ // A new best move
						realMove.score = nextMove.score;
						bestMove=realMove;
						board.reverseMove(realMove);
						if(nextMove.score<beta)beta = nextMove.score;
					} else {
						board.reverseMove(realMove);
					}
				}
			}
			else { // self move; analogous to the opponent's move case above
				int rnmoves = nmoves;
				for(int i=0;i<rnmoves;i++) {	
					Move realMove = (Move)(moves.get(i));
					if(!realMove.real)continue;
					board.makeMove(realMove);
					Move nextMove = getBestMove(board,!color,(ArrayList)nextMoves.get((realMove).nextIndex),null,ply-1,top,alpha,beta,curScore);
					if(ply==1) {
						// If we are at the bottom, quiescence might be possible
						if(nextMove==null){
							board.reverseMove(realMove);
							realMove.score = 10000;
							return realMove;
						}
						if(Math.abs(nextMove.score-curScore)>aip.deepScore){
							if(top!=aip.deepPly&&ndeep<aip.deepLimit) {
								// Quiescence was triggered
								ArrayList forbidMoves = null;
								board.reverseMove(realMove);
								// Find out what moves shouldn't be looked at in the future
								forbidMoves = Chess.getAllMoves(board,!color);
								forbidMoves.add(realMove);
								board.makeMove(realMove);
								ndeep++;
								Move oldnextMove = nextMove.getCopy();
								// Call the quiescence search recursively
								nextMove = getBestMove(board,!color,Chess.getAllMoves(board,!color),forbidMoves,1,top+1,alpha,beta,nextMove.score);
								if(nextMove==null)nextMove = oldnextMove;
							} 
						} 
					}
					if(nextMove==null){ // No legal moves possible
						board.reverseMove(realMove);
						realMove.score = 10000;
						return realMove;
					}
					if(nextMove.score>beta){ // Beta cutoff lets us escape early from this iteration
						board.reverseMove(realMove);
						realMove.score = nextMove.score;
						return realMove;
					}
					if(nextMove.score>bestMove.score){ // A new best move
						realMove.score = nextMove.score;
						bestMove=realMove;
						board.reverseMove(realMove);
						if(nextMove.score>alpha)alpha = nextMove.score;
					} else {
						board.reverseMove(realMove);
					}
				}
			}
			// Exceedingly large scores mean a check-mate was reached (since there is no individual detection for that state)
			if(Math.abs(bestMove.score)>100000)return null;
			return bestMove;
		}
		else { // The ply was 0
			// All that must be done here is for the board to be scored
			Move noMove = new Move(0,0,0,0,new Piece(0,!color));
			noMove.score = scoreBoard(board, null, !color, aip.sg);
			// For opponent's move, we need to invert the score (since from the player's
			// viewpoint, the opponent's high score is his own low score and vice versa)
			if(ply%2==top%2)noMove.score=-noMove.score;
			return noMove;
		}
	}
}
