import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;

// Class that implements a graphical interface for the board
// Also implements user interaction via the mouse
// Tied to a ChessInterface for communication

public class ChessBoardGUI extends Canvas {
	// The abstract chessboard representation corresponding to the visual one
	ChessBoard board;
	
	// The pixel width/height of each board square
	int size=60;
	
	// Indicates which, if any, of the squares is hilighted
	int hilightx = -1;
	int hilighty = -1;
	
	// Indicates the board orientation
	public boolean flip = false;
	
	// Current player (white=true)
	boolean curPlayer=true;
	
	// Background square colors
	Color whiteColor = Color.white;
	Color blackColor = Color.gray;
	
	// Flag indicating whether user is allowed to click on the board (i.e. it's his move)
	boolean disabled=true;
	
	// The observing game
	Game game;
	
	// Null constructor
	public ChessBoardGUI() {}
	
	// Returns the row depandant on board orientation
	int getRow(int x) {
		if(flip)return 7-x/size;
		return x/size;
	}
	
	// Returns the column dependant on board orientation
	int getCol(int y) {
		y-=20;
		return y/size;
	}
	
	// Processes user clicks on the board (user moving pieces)
	public void boardClicked(MouseEvent e) {
		if(disabled)return;
		requestFocus();
        int cType = e.getModifiers();
        int x = e.getX();
        int y = e.getY();
        int col = getCol(x);
        int row = getRow(y);
        if(hilightx!=-1) { // No piece is hilighted, user clicks for first time
			if(!flip&&board.getPiece(row,col).color==curPlayer&&!board.isEmpty(row,col)) {
				hilightx = col;
				hilighty = row;
			}
			else if(flip&&board.getPiece(row,7-col).color==curPlayer&&!board.isEmpty(row,7-col)) {
				hilightx = col;
				hilighty = row;
			}
			else {
				// User clicked twice, so a move has theoretically been made
				Move newMove = new Move(hilighty,hilightx,row,col,board.getPiece(hilighty,hilightx));
				if(flip)newMove = new Move(hilighty,7-hilightx,row,7-col,board.getPiece(hilighty,7-hilightx));
				if(Chess.isMoveLegal(board,newMove)) { // Check if move is legal
					// If move is legal, transmit to the observing game that it has been made
					ChessBoard nextBoard = board.getCopy();
					nextBoard.makeMove(newMove);
					if(Chess.isKingSafe(nextBoard,curPlayer)) {
						if(game!=null)game.makeMove(newMove);
						else board.makeMove(newMove);
						hilightx = -1;
						hilighty = -1;
					}
				}
			}
        }
        else if(!flip&&board.getPiece(row,col).color==curPlayer&&!board.isEmpty(row,col)) {
			hilightx = col;
			hilighty = row;
		}
		else if(flip&&board.getPiece(row,col).color==curPlayer&&!board.isEmpty(row,7-col)) {
			hilightx = col;
			hilighty = row;
		}
		repaint();
	}
	
	// Initializes the graphical board, whose size is s
	public ChessBoardGUI(int s) {
		size = s;
		final ChessBoardGUI self = this;
		addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
				boardClicked(e);
        }});
	}
	
	// Gets the background color of the given board square
	Color getBGColor(int row, int col) {
		if((row+col)%2==0)return whiteColor; 
		else return blackColor;
	}
	
	// Sets the chessboard to be the newBoard
	public void setBoard(ChessBoard newBoard) {
		board = newBoard;
	}
	
	// Returns the abstract chessboard associated with the visual board
	public ChessBoard getBoard() {
		return board;
	}
	
	// Clears the board
	public void clearBoard() {
		board = new ChessBoard();
	}
	
	// Draws the board and the pieces on it, as well as row and column labels
	public void paint(Graphics g) {
		super.paint(g);
		if(board==null)return;
		for(int row=0;row<8;row++) {
			int drow = row;
			if(flip)drow=7-row;
			for(int col=0;col<8;col++) {
				int dcol=col;
				if(flip)dcol=7-col;
				// Draws appropriate sprite for each piece
				if(!board.isEmpty(row,col))g.drawImage(Piece.getIcon(board.getPiece(row,col)),20+dcol*size,drow*size,size,size,getBGColor(row,col),this);
				else {
					// Draws the background squares
					g.setColor(getBGColor(row,col));
					g.fillRect(20+dcol*size,drow*size,size,size);
				}
			}
		}
		if(hilightx!=-1) { 
			// Hilights the appropriate square
			g.setColor(Color.green);
			if(flip)g.drawRect(20+hilightx*size,(7-hilighty)*size,size,size);
			else g.drawRect(20+hilightx*size,hilighty*size,size,size);
		}
		String[] letter = {"A","B","C","D","E","F","G","H"};
		for(int i=0;i<8;i++) {
			// Writes out the row and columns numbers/letters at the edges of the board
			g.setColor(Color.black);
			if(flip) {
				g.drawString((i+1) + "", 10,i*size+size/2);
				g.drawString(letter[7-i], 18+i*size+size/2,8*size + 12);
			}
			else {
				g.drawString((8-i) + "", 10,i*size+size/2);
				g.drawString(letter[i], 18+i*size+size/2,8*size + 12);
			}
		}
	}
}