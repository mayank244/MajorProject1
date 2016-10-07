
public class AIPlayer {
	// The static board evaluation function used by this player
	ScoringGenome sg;
	
	// The regular ply depth to be searched
	int ply;
	
	// The ply depth to be searched during quiescence
	int deepPly;
	
	// The quiescence search trigger score
	double deepScore;
	
	// The maximum number of quiescence extensions allowed
	int deepLimit;
	
	// Constructor creates a new instance of the AIPlayer, with the given parameters
	public AIPlayer(ScoringGenome s, int p, int dp, double ds, int dl) {
		sg = s;
		ply = p;
		deepPly = dp;
		deepScore = ds;
		deepLimit = dl;
	}
}