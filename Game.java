// Implementation of a chess game being played
// Keeps track of the GameInterface that interacts with user
// Keeps track of ChessBoard for game, and all moves being made

public class Game {
	// The current player (whose move it is)
	boolean curPlayer;
	
	// Indicators of whether each player is computer or human
	boolean[] players;
	
	// The AI player instances
	AIPlayer[] aip = new AIPlayer[2];
	
	// The current state of the chessboard
	ChessBoard board;
	
	// The ChessInterface observing the game
	ChessInterface gi;
	
	// Array of the sequence of moves made in the game
	Move[] moves;
	
	// Statistics counters
	int moveCount;
	int maxMoves;
	double tottime=0;
	double totposit=0;
	
	// Flag of whether the player is still in an opening or not
	boolean inopening;
	
	// Name of the opening move database (if there is one)
	//String ecodbfile;
	
	// Creates a new game with the given playes, AI engines, and a GUI
	public Game(boolean[] pl, AIPlayer[] a, ChessInterface gamei) {
		setSettings(pl, a, gamei, 499);
	}
	
	// Creates a new game with the given playes, AI engines,  GUI, and maximum number of moves allowed
	public Game(boolean[] pl, AIPlayer[] a, ChessInterface gamei, int mm) {
		setSettings(pl, a, gamei, mm);
	}
	
	// Universal parameter setting for all the constructors above
	public void setSettings(boolean[] pl, AIPlayer[] a, ChessInterface gamei, int mm) {
		players = pl;
		aip = a;
		board = new ChessBoard();
		gi = gamei;
		maxMoves = mm;
		board.reset();
		inopening = true;
		//if(ed==null)inopening = false;
		//else ecodbfile = ed;
	}
	
	// Actually starts the game and demands the first move be made
	public void start() {
		moves = new Move[500];
		moveCount = 0;
		curPlayer = true;
		nextMove();
	}
	
	// The game is determined to be over and the observing ChessInterface is notified
	public void gameOver(boolean curPlayer) {
		gi.sendWinner(curPlayer);
	}
	
	// Retrieves a "deep" copy of the current chessboard
	public ChessBoard getBoard() {
		return board.getCopy();
	}

	public boolean pickWinner() {
		// Determines a winner if the game was not won outright with a check-mate
		// To do so, the board is scored by both sides and a consensus reached on who is ahead
		AIEngine aie = new AIEngine();
		double score1 = aie.scoreBoard(board.getCopy(),null,true,aip[0].sg);
		double score2 = aie.scoreBoard(board.getCopy(),null,false,aip[1].sg);
		//System.out.println(tottime / moveCount);
		//System.out.println(totposit/moveCount);
		if(score1-score2>0)return true;
		else return false;
	}
	
	// Makes the passed in move in the game - called by the AIEngine
	public void makeMove(Move move) {
		if(move==null) {
			gameOver(curPlayer);
			return;
		}
		System.out.print(" mjj ");
		tottime = tottime + move.time;
		totposit = totposit + move.evaluated;
		moves[moveCount] = move;
		moveCount++;
		board.makeMove(move);
                System.out.print(tottime + " c " + totposit + " b " + moveCount);
		gi.sendMove(move);
		if(moveCount>maxMoves) { // Game artificially halts when maxMoves is exceeded
			gameOver(pickWinner());
		} else {
			nextMove();
		}
	}
	
	// Takes back the last two moves - called by the observing GameInterface on user's request
	public void takeBack() {
		if(moveCount>1) {
			moveCount--;
			board.reverseMove(moves[moveCount]);
			moveCount--;
			board.reverseMove(moves[moveCount]);
			moves[moveCount] = null;
		}
	}
	
	// Returns the last move made
	public Move getLastMove() {
		if(moveCount==0)return null;
		return moves[moveCount-1];
	}
	
	// Attemmpts to generate the next move. Either from user or from the computer AI
	public void nextMove() {
		if(!Chess.isThereAMove(board.getCopy(),curPlayer)) {
			System.out.println("a");
			gameOver(!curPlayer);
			return;	
		}
		int cpn=1;
		if(curPlayer)cpn=0;
		if(players[cpn]){ // Computer must make move
			if(curPlayer)
				gi.setMessage("White computer is thinking...");
			else
				gi.setMessage("Black computer is thinking...");
			/*if(inopening) { // Attempt to get move from database
				EcoReader ecodb = new EcoReader(ecodbfile);
				Move opening = ecodb.getNextMove(moves);
                                System.out.print(" 8 ");
				if(opening==null){ // No more opening moves available
					// Opening phase has ended
					inopening=false;
					gi.sendOpening(ecodb.getOpeningInfo(moves));
                                        System.out.print( 9 );
				}
				else {
					// A new move from the opening database was found
					Piece p = board.getPiece(opening.start_row,opening.start_col);
					opening.piece = p;
					curPlayer = !curPlayer;
					makeMove(opening);
                                        System.out.print( 10 );
					return;
				}
			}*/
			// The AIEngine is called in a separate thread to find the next move
			CompThread comp = new CompThread(board.getCopy(),curPlayer,aip[cpn],this);
			comp.start();
		}
		curPlayer = !curPlayer;
	}	
	
}
