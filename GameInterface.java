//GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class GameInterface extends java.awt.Frame implements ChessInterface {
	// The Game associated with this interface
	Game theGame;
	ScoringGenome[] masterSG = new ScoringGenome[2];
	
	// The default opening move database to use
	//public static String defaultEcoDB;
	int curSG;
	
	public GameInterface() {
		initComponents();
	}
	
	public ChessBoardGUI graphicBoard = new ChessBoardGUI(60);
	CheckboxGroup whitePlayer = new CheckboxGroup();
	CheckboxGroup blackPlayer = new CheckboxGroup();
	/*//java.awt.TextField[] plyField = new java.awt.TextField[2];
	//java.awt.Label plyLabel = new java.awt.Label();
	//java.awt.TextField[] deepPlyField = new java.awt.TextField[2];
	//java.awt.Label deepPlyLabel = new java.awt.Label();
	//java.awt.TextField[] deepScoreField = new java.awt.TextField[2];
	//java.awt.Label deepScoreLabel = new java.awt.Label();
	//java.awt.TextField[] deepLimitField = new java.awt.TextField[2];
	//java.awt.Label deepLimitLabel = new java.awt.Label();
	//java.awt.Label whiteLabel = new java.awt.Label();
	//java.awt.Label blackLabel = new java.awt.Label();
	
	java.awt.Checkbox openDB = new java.awt.Checkbox("",true);
	
	java.awt.Button takeBackButton = new java.awt.Button();
	java.awt.Button[] evalButton = new java.awt.Button[2];
	
	//java.awt.TextField[][] evalField = new java.awt.TextField[3][7];
	java.awt.Label mobilityLabel = new java.awt.Label();
	java.awt.Label threatsLabel = new java.awt.Label();
	java.awt.Label protectsLabel = new java.awt.Label();
	java.awt.Label piecesLabel = new java.awt.Label();
	java.awt.Label currentLabel = new java.awt.Label();
	java.awt.Label pawnAdvLabel = new java.awt.Label();
	java.awt.TextField pawnAdvField = new java.awt.TextField();*/
	java.awt.TextArea openingLabel = new java.awt.TextArea("",4,30,java.awt.TextArea.SCROLLBARS_NONE);
	int evalField[][] = new int[3][7];
	java.awt.Button startGameButton = new java.awt.Button();
	java.awt.Label messageLabel = new java.awt.Label();
	int pawnAdvField =1;
    int plyField[] = new int[2];
    int deepPlyField[] = new int[2];
    double deepScoreField[] = new double[2];
    int deepLimitField[] = new int[2];
    java.awt.Checkbox whiteCompPlayer = new java.awt.Checkbox("",whitePlayer,false);
	java.awt.Checkbox whiteHumanPlayer = new java.awt.Checkbox("",whitePlayer,true);
	java.awt.Checkbox blackCompPlayer = new java.awt.Checkbox("",blackPlayer,true);
	java.awt.Checkbox blackHumanPlayer = new java.awt.Checkbox("",blackPlayer,false);
	
	public void initComponents() {
        System.out.print(2);
		setLocation(new java.awt.Point(50, 50));
		//setLayout(null);
		setSize(new java.awt.Dimension(600, 600));
		setTitle("Chess");
		
		graphicBoard.setBackground(Color.white);
	    graphicBoard.setVisible(true);
        graphicBoard.setLocation(new java.awt.Point(150, 100));
	    graphicBoard.setSize(new java.awt.Dimension(600, 600));
	    
	    /*for(int i=0;i<3;i++) {
			for(int j=1;j<7;j++) {
				evalField[i][j] = new java.awt.TextField();
				evalField[i][j].setLocation(new java.awt.Point(600 + j*40, 400+i*30));
				evalField[i][j].setVisible(false);
				evalField[i][j].setText("0");
				evalField[i][j].setSize(new java.awt.Dimension(30, 30));	
			}
		}*/
/*evalField[0][1].setText("0");
evalField[0][2].setText("0");
evalField[0][3].setText("0");
evalField[0][4].setText("0");
evalField[0][5].setText("1");
evalField[0][6].setText("0");
evalField[1][1].setText("0");
evalField[1][2].setText("1");
evalField[1][3].setText("1");
evalField[1][4].setText("2");
evalField[1][5].setText("5");
evalField[1][6].setText("4");
evalField[2][1].setText("0");
evalField[2][2].setText("1");
evalField[2][3].setText("1");
evalField[2][4].setText("0");
evalField[2][5].setText("0");
evalField[2][6].setText("0");
pawnAdvField.setText("1");
plyField[0].setText("3");
plyField[1].setText("3");
deepPlyField[0].setText("5");
deepPlyField[1].setText("5");
deepScoreField[0].setText("99");
deepScoreField[1].setText("99");
deepLimitField[0].setText("500");
deepLimitField[1].setText("500");*/
evalField[0][1] = 0;
evalField[0][2]=0;
evalField[0][3]=0;
evalField[0][4]=0;
evalField[0][5]=1;
evalField[0][6]=0;
evalField[1][1]=0;
evalField[1][2]=1;
evalField[1][3]=1;
evalField[1][4]=2;
evalField[1][5]=5;
evalField[1][6]=4;
evalField[2][1]=0;
evalField[2][2]=1;
evalField[2][3]=1;
evalField[2][4]=0;
evalField[2][5]=0;
evalField[2][6]=0;
pawnAdvField =1;
plyField[0] = 3;
plyField[1]=3;
deepPlyField[0]=5;
deepPlyField[1]=5;
deepScoreField[0]=99;
deepScoreField[1]=99;
deepLimitField[0]=500;
deepLimitField[1]=500;

		
		/*mobilityLabel.setLocation(new java.awt.Point(580, 400));
		mobilityLabel.setVisible(false);
		mobilityLabel.setText("Mobility");
		mobilityLabel.setSize(new java.awt.Dimension(150, 30));
		
		threatsLabel.setLocation(new java.awt.Point(580, 430));
		threatsLabel.setVisible(false);
		threatsLabel.setText("Threats");
		threatsLabel.setSize(new java.awt.Dimension(150, 30));
		
		protectsLabel.setLocation(new java.awt.Point(580, 460));
		protectsLabel.setVisible(false);
		protectsLabel.setText("Protects");
		protectsLabel.setSize(new java.awt.Dimension(150, 30));
		
		currentLabel.setLocation(new java.awt.Point(640, 355));
		currentLabel.setVisible(false);
		currentLabel.setText("Static board evaluation function");
		currentLabel.setSize(new java.awt.Dimension(220, 30));
		
		pawnAdvLabel.setLocation(new java.awt.Point(580, 500));
		pawnAdvLabel.setVisible(false);
		pawnAdvLabel.setText("Pawn advancement");
		pawnAdvLabel.setSize(new java.awt.Dimension(120, 30));
		
		pawnAdvField.setLocation(new java.awt.Point(710,500));
		pawnAdvField.setVisible(false);
		pawnAdvField.setText("1");
		pawnAdvField.setSize(new java.awt.Dimension(30, 30));	
		
		openingLabel.setLocation(new java.awt.Point(550,540));
		openingLabel.setVisible(false);
		openingLabel.setText("");
		openingLabel.setSize(new java.awt.Dimension(300, 70));	
				
		piecesLabel.setLocation(new java.awt.Point(650, 380));
		piecesLabel.setVisible(false);
		piecesLabel.setText("P          N        B        R       Q       K");
		piecesLabel.setSize(new java.awt.Dimension(250, 30));
		
	    plyField[0] = new java.awt.TextField();
	    plyField[0].setLocation(new java.awt.Point(790, 120));
		plyField[0].setVisible(false);
		plyField[0].setText("3");
		plyField[0].setSize(new java.awt.Dimension(50, 30));
		
		plyField[1] = new java.awt.TextField();
		plyField[1].setLocation(new java.awt.Point(700, 120));
		plyField[1].setVisible(false);
		plyField[1].setText("3");
		plyField[1].setSize(new java.awt.Dimension(50, 30));
		
		plyLabel.setLocation(new java.awt.Point(550, 120));
		plyLabel.setVisible(false);
		plyLabel.setText("Ply");
		plyLabel.setSize(new java.awt.Dimension(90, 30));
		
		deepPlyField[0] = new java.awt.TextField();
		deepPlyField[0].setLocation(new java.awt.Point(790, 160));
		deepPlyField[0].setVisible(false);
		deepPlyField[0].setText("5");
		deepPlyField[0].setSize(new java.awt.Dimension(50, 30));
		
		deepPlyField[1] = new java.awt.TextField();
		deepPlyField[1].setLocation(new java.awt.Point(700, 160));
		deepPlyField[1].setVisible(false);
		deepPlyField[1].setText("5");
		deepPlyField[1].setSize(new java.awt.Dimension(50, 30));
		
		deepPlyLabel.setLocation(new java.awt.Point(550, 160));
		deepPlyLabel.setVisible(false);
		deepPlyLabel.setText("Quiescence Ply");
		deepPlyLabel.setSize(new java.awt.Dimension(90, 30));
		
		
		deepScoreField[0] = new java.awt.TextField();
		deepScoreField[0].setLocation(new java.awt.Point(790, 200));
		deepScoreField[0].setVisible(false);
		deepScoreField[0].setText("99");
		deepScoreField[0].setSize(new java.awt.Dimension(50, 30));
		
		deepScoreField[1] = new java.awt.TextField();
		deepScoreField[1].setLocation(new java.awt.Point(700, 200));
		deepScoreField[1].setVisible(false);
		deepScoreField[1].setText("99");
		deepScoreField[1].setSize(new java.awt.Dimension(50, 30));
		
		deepScoreLabel.setLocation(new java.awt.Point(550, 200));
		deepScoreLabel.setVisible(false);
		deepScoreLabel.setText("Quiescence Trigger");
		deepScoreLabel.setSize(new java.awt.Dimension(110, 30));
		
		deepLimitField[0] = new java.awt.TextField();
		deepLimitField[0].setLocation(new java.awt.Point(790, 240));
		deepLimitField[0].setVisible(false);
		deepLimitField[0].setText("500");
		deepLimitField[0].setSize(new java.awt.Dimension(50, 30));
		
		deepLimitField[1] = new java.awt.TextField();
		deepLimitField[1].setLocation(new java.awt.Point(700, 240));
		deepLimitField[1].setVisible(false);
		deepLimitField[1].setText("500");
		deepLimitField[1].setSize(new java.awt.Dimension(50, 30));
		
		deepLimitLabel.setLocation(new java.awt.Point(550, 240));
		deepLimitLabel.setVisible(false);
		deepLimitLabel.setText("Max Quiescences");
		deepLimitLabel.setSize(new java.awt.Dimension(110, 30));
		
		openDB.setLocation(new java.awt.Point(550, 280));
		openDB.setVisible(false);
		openDB.setLabel("Use opening database");
		openDB.setSize(new java.awt.Dimension(150, 30));
		
		whiteLabel.setLocation(new java.awt.Point(650, 40));
		whiteLabel.setVisible(false);
		whiteLabel.setText("WHITE");
		whiteLabel.setSize(new java.awt.Dimension(110, 30));
		
		blackLabel.setLocation(new java.awt.Point(650, 80));
		blackLabel.setVisible(false);
		blackLabel.setText("BLACK");
		blackLabel.setSize(new java.awt.Dimension(110, 30));
		
		
		*/
		startGameButton.setLocation(new java.awt.Point(160, 565));
		startGameButton.setVisible(true);
		startGameButton.setLabel("Start Game");
		startGameButton.setSize(new java.awt.Dimension(110, 30));
		whiteCompPlayer.setLocation(new java.awt.Point(700, 40));
		whiteCompPlayer.setVisible(false);
		whiteCompPlayer.setLabel("Computer");
		whiteCompPlayer.setSize(new java.awt.Dimension(90, 30));
				
		whiteHumanPlayer.setLocation(new java.awt.Point(790, 40));
		whiteHumanPlayer.setVisible(false);
		whiteHumanPlayer.setLabel("Human");
		whiteHumanPlayer.setSize(new java.awt.Dimension(90, 30));
				
		blackCompPlayer.setLocation(new java.awt.Point(700, 80));
		blackCompPlayer.setVisible(false);
		blackCompPlayer.setLabel("Computer");
		blackCompPlayer.setSize(new java.awt.Dimension(90, 30));
				
		blackHumanPlayer.setLocation(new java.awt.Point(790, 80));
		blackHumanPlayer.setVisible(false);
		blackHumanPlayer.setLabel("Human");
		blackHumanPlayer.setSize(new java.awt.Dimension(90, 30));
		/*
		takeBackButton.setLocation(new java.awt.Point(20, 565));
		takeBackButton.setVisible(false);
		takeBackButton.setLabel("Take Back");
		takeBackButton.setSize(new java.awt.Dimension(110, 30));*/
		
		/*for(int i=0;i<2;i++) {
			evalButton[i] = new java.awt.Button();
			evalButton[i].setLocation(new java.awt.Point(700+i*90, 280));
			evalButton[i].setVisible(false);
			evalButton[i].setLabel("Eval");
			evalButton[i].setSize(new java.awt.Dimension(50, 30));
			final int k = i;
			evalButton[k].addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				evalButtonActionPerformed(e,k);
			}
		});
		}*/

		
		messageLabel.setLocation(new java.awt.Point(20, 530));
		messageLabel.setVisible(true);
		messageLabel.setText("");
		messageLabel.setBackground(Color.cyan.brighter());
		messageLabel.setSize(new java.awt.Dimension(500, 30));
		
		startGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				startGameButtonActionPerformed(e);
			}
		});
		
		/*takeBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				takeBackButtonActionPerformed(e);
			}
		});
		*/
	        addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				thisWindowClosing(e);
			}
		});
	    
	    //for(int i=0;i<3;i++)
		//	for(int j=1;j<7;j++)
		//		add(evalField[i][j]);
		/*add(openingLabel);
		add(pawnAdvLabel);
		add(pawnAdvField);
		add(currentLabel);
		add(mobilityLabel);
		add(threatsLabel);
		add(protectsLabel);
		add(piecesLabel);
	    add(plyField[0]);
	    add(plyField[1]);
	    add(plyLabel);
	    add(deepPlyField[0]);
	    add(deepPlyField[1]);
	    add(deepPlyLabel);
	    add(deepScoreField[0]);
	    add(deepScoreField[1]);
	    add(deepScoreLabel);
	    add(deepLimitField[0]);
	    add(deepLimitField[1]);
	    add(deepLimitLabel);
	    add(openDB);
	    
	    add(whiteLabel);
	    add(blackLabel);
	    
	    add(takeBackButton);
	    
	    //add(evalButton[0]);
	    //add(evalButton[1]);
            */
	    add(messageLabel);
            add(startGameButton);
            add(whiteCompPlayer);
	    add(whiteHumanPlayer);
	    add(blackCompPlayer);
	    add(blackHumanPlayer);
	    add(graphicBoard);
	    setVisible(true);// visibility of complete board
	    ChessBoard temp = new ChessBoard();
	    temp.reset();// visibility of the pieces
	    graphicBoard.setBoard(temp);  //visibility of black and white pieces
	    graphicBoard.repaint();
	    masterSG[0] = new ScoringGenome();
	    masterSG[1] = new ScoringGenome();
	    System.out.print(masterSG[0] + " " + masterSG[1] + " ");
	    curSG = 1;
	    //displayGenome();
	}
	
	/*// Displays the scoring genome in the editable boxes for the user
	public void displayGenome() {
		pawnAdvField.setText(""+masterSG[curSG].pawnAdvancement);
		for(int i=0;i<3;i++) {
			for(int j=1;j<7;j++) {
				switch(i) {
					case 0:
						evalField[i][j].setText(""+masterSG[curSG].pieceMobility[j]);
						break;
					case 1:
						evalField[i][j].setText(""+masterSG[curSG].pieceThreats[j]);
						break;
					case 2:
						evalField[i][j].setText(""+masterSG[curSG].pieceProtects[j]);
						break;
				}
			}
		}
            for(int i=0;i<3;i++)
              for(int j=1;j<7;j++)
                System.out.println(evalField[i][j]);
	}
	*/
	
	// Reads the scoring genome from the editable text boxes entered in by the user
	public void readGenome() {
		masterSG[curSG].pawnAdvancement = pawnAdvField;
		for(int i=0;i<3;i++) {
			for(int j=1;j<7;j++) {
				switch(i) {
					case 0:
						masterSG[curSG].pieceMobility[j] = evalField[i][j];
						break;
					case 1:
						masterSG[curSG].pieceThreats[j] = evalField[i][j];
						break;
					case 2:
						masterSG[curSG].pieceProtects[j] = evalField[i][j];
						break;
				}
			}
		}
	}
	
	
	// The Game triggers this event when it determines what opening was used
	public void sendOpening(String info) {
		//openingLabel.setVisible(true);
		//openingLabel.setText(info);
	}
		
	// Called by the Game to display a message about the last move made
	public void setMessage(String msg) {
		messageLabel.setText(msg);
	}
	
	// Called by the Game to indicate the game is over, and who won
	public void sendWinner(boolean who) {
		if(who)setMessage("White wins!");
		else setMessage("Black wins!");
	}
	
	// Called by the game with the move to be made on the graphical board
	// The GameInterface passes it on to the graphicBoard for display
	public void sendMove(Move move) {
		System.out.print("hell");
		graphicBoard.curPlayer = !move.piece.color;
		graphicBoard.setBoard(theGame.getBoard());
		graphicBoard.disabled = false;
		graphicBoard.repaint();
		setMessage(theGame.getLastMove().getInfo());
	}
		
	// Executes when user click "Start game" button. Launches the Game object
	// with parameters specifed by user in GUI
	public void startGameButtonActionPerformed(java.awt.event.ActionEvent e) {	
		//takeBackButton.setVisible(false);
		boolean[] players = new boolean[2];
		AIPlayer[] aip = new AIPlayer[2];
		players[0] = whiteCompPlayer.getState();
		players[1] = blackCompPlayer.getState();
                System.out.print(players[0]+""+players[1]);
		if(players[0])graphicBoard.flip = true;
		graphicBoard.disabled = false;
		readGenome();
		// Reads in paramters typed in by the user for the AIPlayer(s)
		for(int i=0;i<2;i++) {
			int ply = plyField[1-i];
			int deepPly = deepPlyField[1-i];
			double deepScore = deepScoreField[1-i];
			int deepLimit = deepLimitField[1-i];
                        System.out.println(ply+" "+deepPly+" "+deepScore+" "+deepLimit+" ");
			aip[1-i] = new AIPlayer(masterSG[1-i],ply,deepPly,deepScore,deepLimit);
		}
		theGame = new Game(players, aip, this);
		graphicBoard.game = theGame;
		graphicBoard.setBoard(theGame.getBoard());
		graphicBoard.repaint();
		theGame.start(); // Launches the game
	}
	
	// Executes when the user clicks the "Take back" button. The Game object
	// is notified that the last 2 moves should be taken back (program's and user's)
	/*public void takeBackButtonActionPerformed(java.awt.event.ActionEvent e) {	
		theGame.takeBack();
		graphicBoard.setBoard(theGame.getBoard());
		graphicBoard.repaint();
		Move lastMove = theGame.getLastMove();
		if(lastMove!=null)setMessage(lastMove.getInfo());
	}
	
	
	// Toggles between the white and black's players' board evaluation function editing modes
	public void evalButtonActionPerformed(java.awt.event.ActionEvent e, int i) {
		readGenome();
		curSG = 1-i;
		if(curSG==1) {
			currentLabel.setText("BLACK static board evaluation function");
			currentLabel.setBackground(Color.white);
			currentLabel.setForeground(Color.black);
		} else {
			currentLabel.setText("WHITE static board evaluation function");
			currentLabel.setBackground(Color.black);
			currentLabel.setForeground(Color.white);
		}
		//displayGenome();
	}	
*/	
	// Close the window when the close box is clicked (ends the program)
	void thisWindowClosing(java.awt.event.WindowEvent e) {
		setVisible(false);
		dispose();
		System.exit(0);
	}
}
