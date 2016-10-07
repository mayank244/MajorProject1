
public class AIChess {
	// Loads the images
	static public void main(String[] args) {
		String[] pieceImageFiles = new String[14];
		pieceImageFiles[0] = "wsquare.GIF";
		pieceImageFiles[1] = "wpawn.gif";
		pieceImageFiles[2] = "wbishop.gif";
		pieceImageFiles[3] = "wknight.gif";
		pieceImageFiles[4] = "wrook.gif";
		pieceImageFiles[5] = "wqueen.gif";
		pieceImageFiles[6] = "wking.gif";
		pieceImageFiles[7] = "bsquare.GIF";
		pieceImageFiles[8] = "bpawn.gif";
		pieceImageFiles[9] = "bbishop.gif";
		pieceImageFiles[10] = "bknight.gif";
		pieceImageFiles[11] = "brook.gif";
		pieceImageFiles[12] = "bqueen.gif";
		pieceImageFiles[13] = "bking.gif";
		Piece.loadImages(pieceImageFiles);
		GameInterface game = new GameInterface();
                /*System.out.print(0);
		if(args.length>0)
			{game.defaultEcoDB = args[0];System.out.print("00");}
		else
			{game.defaultEcoDB = "eco.pgn";System.out.print("11");}
*/	
}
}
