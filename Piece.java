
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.net.*;

public class Piece {
	public static final int EMPTY = 0;
	public static final int PAWN = 1;
	public static final int BISHOP = 2;
	public static final int KNIGHT = 3;
	public static final int ROOK = 4;
	public static final int QUEEN = 5;
	public static final int KING = 6;
	
	public static final boolean WHITE = true;
	public static final boolean BLACK = false;
	
	public static Image[] whitePieces = new Image[7];
	public static Image[] blackPieces = new Image[7];

	public int type; // What piece this is? i.e. queen, king, pawn etc.
	public boolean color; // true= white, false= black
	
	public Piece() {
		type = EMPTY;
	}
	
	public Piece(int t, boolean c) {
		type = t;
		color = c;	
	}
	
	public Piece getCopy() {
		return new Piece(type,color);
	}
	public static void loadImages(String[] imageFiles) {
		for(int i=0;i<7;i++) {
			whitePieces[i] = Toolkit.getDefaultToolkit().createImage(imageFiles[i]);
			blackPieces[i] = Toolkit.getDefaultToolkit().createImage(imageFiles[i+7]);
		}
	}
	
	// Retrieves the icon associated with this piece (for graphical display)
	public static Image getIcon(Piece p) {
		if(p.color==WHITE) 
			return whitePieces[p.type];
		else 
			return blackPieces[p.type];
	}
}
