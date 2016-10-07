// Interface template for interacting with the Game object
// The Game object will call these functions as events
public interface ChessInterface {
	
	// Game relays special message (computer is thinking)
	public void setMessage(String message);
	
	// Game notifies of last move made
	public void sendMove(Move move);
	
	// Game notifies of winner
	public void sendWinner(boolean who);
	
	// Game notifies of what opening was used
	public void sendOpening(String info);
}