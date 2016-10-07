// Class for running the AIEngine in a separate thread
// so as not to interfere with the rest of the GUI in the GameInterface

public class CompThread extends Thread {
	ChessBoard board;
	boolean curPlayer;
	Game parent;
	AIPlayer aip;
	
	// Constructor simply takes in parameters needed to run the AIEngine
	public CompThread(ChessBoard b, boolean p, AIPlayer a, Game game) {
		board = b;
		curPlayer = p;
		aip = a;
		parent = game;
	}
	
	public void run() {
		AIEngine aie = new AIEngine();
		Move compMove = aie.getBestMove(board,curPlayer,aip);
		parent.makeMove(compMove);
	}
}