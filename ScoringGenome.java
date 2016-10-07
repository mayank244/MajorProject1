
import java.io.*;
public class ScoringGenome {
	// Piece possession scores
	int[] pieceOwn = new int[7];
	
	// Piece mobility scores
	int[] pieceMobility = new int[7];
	
	// Piece threats scores
	int[] pieceThreats = new int[7];
	
	// Piece protects scores
	int[] pieceProtects = new int[7];
	
	// Pawn advancement score
	int pawnAdvancement;
	
	// Creates a blank ScoringGenome
	public ScoringGenome(int id) {}
	
	// Creates a scoring genome with the given parameters, and hardcodes the posession params
	public ScoringGenome(int pa, int[] pm, int[] pt, int[] pp) {
		pawnAdvancement = pa;
		pieceMobility = pm;
		pieceThreats = pt;
		pieceProtects = pp;
		initOwn();		
	}
	
	// Generates an array of intelligently random scoring genomes
	// n is the numeber to generate
	public static ScoringGenome[] generate(int n) {
		ScoringGenome[] sg = new ScoringGenome[n];
		for(int i=0;i<n;i++) {
			sg[i] = new ScoringGenome();
			sg[i].pawnAdvancement = (int)(Math.random()*5.0);
			// Each parameter can be a value between 0 and 5
			for(int j=1;j<7;j++) {
				sg[i].pieceMobility[j] = (int)(Math.random()*5.0);
				sg[i].pieceThreats[j] = (int)(Math.random()*5.0);
				sg[i].pieceProtects[j] = (int)(Math.random()*5.0);
			}
		}
		return sg;
	}
	
	// Creates a "default" scoring genome with preset weights for the parameters
	public ScoringGenome(){
		for(int i=0;i<7;i++) {
			pieceMobility[i] = 0;
			pieceThreats[i] = 0;
			pieceProtects[i] = 0;
		}
		pawnAdvancement = 1;
		pieceMobility[1] = 0;
		pieceThreats[1] = 0;
		pieceProtects[1] = 0;
		pieceMobility[2] = 0;
		pieceThreats[2] = 1;
		pieceProtects[2] = 1;
		pieceMobility[3] = 0;
		pieceThreats[3] = 1;
		pieceProtects[3] = 1;
		pieceMobility[4] = 0;
		pieceThreats[4] = 2;
		pieceProtects[4] = 0;
		pieceMobility[5] = 1;
		pieceThreats[5] = 5;
		pieceProtects[5] = 0;
		pieceMobility[6] = 0;
		pieceThreats[6] = 4;
		pieceProtects[6] = 0;
		initOwn();
	}
	
	// Initializes the piece possession weights
	public void initOwn() {
		pieceOwn[1] = 100;
		pieceOwn[2] = 300;
		pieceOwn[3] = 300;
		pieceOwn[4] = 500;
		pieceOwn[5] = 900;
		pieceOwn[6] = 9999999;
	}
	
	// Displays the scoring genome in user-readable format to the console
	public void showScores() {
		System.out.println("OWN MOBILITY THREATS PROTECTS");
		for(int i=1;i<7;i++)
			System.out.println(pieceOwn[i] + " " + pieceMobility[i] + " " + pieceThreats[i] + " " + pieceProtects[i]);
		System.out.println(pawnAdvancement);
	}
	
	// Writes the scoring genome in user-readable format to a file
	public void writeScores(FileWriter fw) {
		try {
			fw.write("OWN MOBILITY THREATS PROTECTS\n");
			for(int i=1;i<7;i++)
				fw.write(pieceOwn[i] + " " + pieceMobility[i] + " " + pieceThreats[i] + " " + pieceProtects[i] + "\n");
		fw.write("pa: " + pawnAdvancement + "\n");
		fw.flush();	
		} catch(Exception exc) {}
	}
}
