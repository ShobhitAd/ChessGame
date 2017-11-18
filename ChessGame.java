import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
/** 
* The main class is used to enclose the entire chess game into one class
* @author Shobhit
* @version 1.0
*/
public class ChessGame implements MouseListener{
	//GUI Variables
	//Timer tmrANIM = new Timer(1, this);
	JPanel pnlREF;
	JLabel lblREF;
	JList<String> lstREF;
	
	Color BLACK = Color.black, WHITE = Color.white;
	//Chess variables
		//Grid contains information about chesspieces
	int[][] GRID = new int[8][8];
		//Can move contatins position that the selected piece can go to
	int[][] CANMOVE = new int[8][8];
		//Square size is used when drawing the board on screen
	int SQUARE_SIZE = 60;
		//Find which piece is selected using coordinates for the location
	int SELECTED[] = new int[2];
		//A set of chess rules used to decide where a selected piece can move
	ChessRules RULE_SET;
		//Used to translate chesspiece name to image urls
	HashMap <String, Image> imgs = new HashMap <String, Image>();
		//Move List for chess game
	Vector <String> MOVE_LIST = new Vector<String>();
		//Alphabet coordinate for the chess board
	String[] ALPHA = {"a", "b", "c", "d", "e", "f", "g", "h"};
		//Used to store the turn alphabet
	char TURN = 'W';	
		//Check If white or black has won
	boolean GAME_OVER = false;
		//Offset for drawing board on screen
	int xOff = 50, yOff = 50;
		//Which side the player sees the board from
	boolean PRESPECT = true;

	ClientServerSocket SOCKET;

	/** 
	* Constructor used to create an instance of a chess game
	* @param pnl Reference to the JPanel used in clsGame
	*/
	public ChessGame(JPanel pnl, JLabel lbl, JList<String> moves, boolean Pr, ClientServerSocket socket){
		//Assign reference
		pnlREF = pnl;
		lblREF = lbl;
		lstREF = moves;
		PRESPECT = Pr;
		SOCKET = socket;

		//Display
		lblREF.setText("WHITE PLAYS");

		//Inititalize game conditions
		resetCanMove();
		resetBoard();

		//Assign image urls for every piece
		try{
			imgs.put("WP", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/0/04/Chess_plt60.png")));
			imgs.put("BP", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/c/cd/Chess_pdt60.png")));
			imgs.put("WR", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/5/5c/Chess_rlt60.png")));
			imgs.put("BR", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/a/a0/Chess_rdt60.png")));
			imgs.put("WN", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/2/28/Chess_nlt60.png")));
			imgs.put("BN", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/f/f1/Chess_ndt60.png")));
			imgs.put("WB", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/9/9b/Chess_blt60.png")));
			imgs.put("BB", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/8/81/Chess_bdt60.png")));
			imgs.put("WQ", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/4/49/Chess_qlt60.png")));
			imgs.put("BQ", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/a/af/Chess_qdt60.png")));
			imgs.put("WK", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/3/3b/Chess_klt60.png")));
			imgs.put("BK", ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/e/e3/Chess_kdt60.png")));

		}catch(Exception e){}

		//No piece is selected intially
		SELECTED[0] = -1;
		SELECTED[1] = -1;

		//Initialize rule set
		RULE_SET = new ChessRules(this);
		
		//Redraw chess board
		update(pnlREF.getGraphics());
	}

	/** 
	* Function used to reset the canmove array when a new piece is selected
	*/
	public void resetCanMove(){
		//Reset the canmove array to 0
		CANMOVE = new int[][]{
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
		};
	}

	/** 
	* Function used to reset the board to its original state
	*/
	public void resetBoard(){
		//Reset board 
		GRID = new int[][]{
			{110, 120, 130, 140, 150, 131, 121, 111},
			{101, 102, 103, 104, 105, 106, 107, 108},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{1, 2, 3, 4, 5, 6, 7, 8},
			{10, 20, 30, 40, 50, 31, 21, 11}
		};
	}

	/** 
	* Function used to get the image of the chesspiece
	* @param col Color of the piece
	* @param type ChessPiece tpye (King, queen,pawn, etc)
	* @return Chess piece image
	*/
	public Image getImage(char col, char type){
		//Piece name
		String piece = col + "" + type ;
		//Get image name 
		return imgs.get(piece);
	}

	/** 
	* Get the color of a piece using its grid id
	* @param ID ID on grid
	* @return Color char 'W' or  'B'
	*/
	public char getColor(int ID){
		if(ID == 0){return '-';}
		return (ID >= 100) ? 'B' : 'W';
	}

	/** 
	* Get the type of a piece using its grid id
	* @param ID ID on grid
	* @return Type of the chess piece (P, R, K, etc.)
	*/
	public char getType(int ID){
		//Remove color hash
		if(ID >= 100){ID -= 100;}

		if(ID == 0){return '-';}		
		switch(ID / 10){
			case 0:
				return 'P';

			case 1:
				return 'R';

			case 2:
				return 'N';
			
			case 3:
				return 'B';
			
			case 4:
				return 'Q';
			
			case 5:
				return 'K';									
		}

		return '-';
	}

	/**
	* Function used to change the color scheme of the board tiles
	* @param w Color array that will be used to replace white
	* @param b Color array that will be used to replace black
	*/
	public void colorScheme(Color w, Color b){
		//Change color scheme
		WHITE = w;
		BLACK = b;

		//Redraw board
		update(pnlREF.getGraphics());
	}
	/**
	* Fucntion used to draw chessboard on the screen
	* @param g Graphics component where to draw the board
	* @param xOff Offset space from left of the panel
	* @param yOff Offset space from top of the panel
	*/
	public void drawBoard(Graphics g, int xOff, int yOff){
		//Used to change square color
		boolean blnAlt = !PRESPECT;
		//The color of the square
		Color blockCol = Color.black;

		for(int i = 0;i < GRID.length; i++){
			for(int j = 0;j < GRID[i].length; j++){
				int row = (PRESPECT)? i: 7-i; 
			//Pick color of square
				//Alternate color of square
				blockCol = (blnAlt) ? new Color(BLACK.getRed() , BLACK.getGreen(), BLACK.getBlue(), BLACK.getAlpha()) : new Color(WHITE.getRed(), WHITE.getGreen(), WHITE.getBlue());;
				//If piece is selected
				blockCol = (SELECTED[0] == j && SELECTED[1] == row) ? Color.yellow : blockCol;
				//If a piece can move to that certain block
				blockCol = (CANMOVE[row][j] == 1) ? ((GRID[row][j] == 0) ? Color.cyan : Color.red): blockCol;
				blockCol = (RULE_SET.BLACK_CHECK && getType(GRID[row][j]) == 'K' && getColor(GRID[row][j]) == 'B') ? Color.magenta.darker() : blockCol;
				blockCol = (RULE_SET.WHITE_CHECK && getType(GRID[row][j]) == 'K' && getColor(GRID[row][j]) == 'W') ? Color.magenta.darker() : blockCol;
				//Alternate
				blnAlt = !blnAlt;

			//Draw the square
				g.setColor(blockCol);
				g.fill3DRect(j * SQUARE_SIZE + xOff, i * SQUARE_SIZE + yOff,
					SQUARE_SIZE, SQUARE_SIZE, true);
				g.draw3DRect(j * SQUARE_SIZE + xOff, i * SQUARE_SIZE + yOff, 
					SQUARE_SIZE, SQUARE_SIZE, true);

			//Draw chesspiece on square
				if(GRID[i][j] != 0){
					try{
						g.drawImage(getImage(getColor(GRID[row][j]), getType(GRID[row][j])), j * SQUARE_SIZE + xOff, 
							i * SQUARE_SIZE + yOff, SQUARE_SIZE, SQUARE_SIZE, null);
					}catch(Exception e){
						System.out.println("Couldnt draw image");
					}
				}

			}
			//Alternate
			blnAlt = !blnAlt;
		}
	}


	/** 
	* Simulate a 'move' in a chess game
	* @param x Current x position
	* @param y Current y position
	* @param dx X Position we wish to move to
	* @param dy Y Position we wish to move to
	*/
	public void pieceMove(int x, int y, int dx, int dy){
		//System.out.println(x + ", " + y);
		String Sp = "       ";
		//Move piece
		int prev = GRID[dy][dx];

		MOVE_LIST.addElement(ALPHA[x] + (8 - y) + Sp + ALPHA[dx] + (8 - dy) 
			+ Sp + getColor(GRID[dy][dx]) + getType(GRID[dy][dx]) + GRID[dy][dx] % 10);
		lstREF.setListData(MOVE_LIST);

		GRID[dy][dx] = GRID[y][x];
		GRID[y][x] = 0;		
		
		RULE_SET.isChecked();

		//Reset movement vars
		resetCanMove();
		SELECTED[0] = -1;
		SELECTED[1] = -1;
		
		TURN = (TURN == 'W') ? 'B' : 'W';
		lblREF.setText((TURN == 'W') ? "WHITE PLAYS": "BLACK PLAYS");
		if(getType(prev) == 'K'){
			lblREF.setText((getColor(prev) == 'W') ? "BLACK WINS" : "WHITE WINS");
			GAME_OVER = true;
		}

		//Redraw board
		update(pnlREF.getGraphics());
		if(SOCKET != null){
			System.out.println("Online: Change turn " + TURN);
		}

	}

	/**
	* Function used the reset the game to its initial state
	*/
	public void resetGame(){

		GAME_OVER = false;
		//Reset the board
		resetBoard();
		//Check for check
		RULE_SET.isChecked();
		//Reset can move array
		resetCanMove();
		//Clear move list
		MOVE_LIST.clear();
		lstREF.setListData(MOVE_LIST);

		//Reset turn to white
		TURN = 'W';
		lblREF.setText((TURN == 'W') ? "WHITE PLAYS": "BLACK PLAYS");
		
		//Redraw board
		update(pnlREF.getGraphics());
	}

	/**
	* Function used to reverse a particular move (recorded in log form)
	* @param log Log entry for the move we need to reverse
	*/
	public void reverseMove(String log){
		//Undo game over if a particular player has already won
		GAME_OVER = false;
		//Seperator used for splitting
		String Sp = "       ";
		//Parse the log string
		String[] parts = log.split(Sp);
		
		//Coordinate variables
			//From
		int x = (int)(parts[0].charAt(0)) - (int)('a');
		int y = 8 - Integer.parseInt(parts[0].charAt(1) + "");
			//To
		int dx = (int)(parts[1].charAt(0)) - (int)('a');
		int dy = 8 - Integer.parseInt(parts[1].charAt(1) + "");
		
		//System.out.println(x + ", " + y);

		//Move the piece back to its place before the move
		GRID[y][x] = GRID[dy][dx];

		//Used to reinstante the chesspiece(If there was one) killed by the moving piece
		int prev = Integer.parseInt(parts[2].charAt(2) + "");
		switch(parts[2].charAt(1)){
			case 'R':
				prev += 10;
				break;
			case 'N':
				prev += 20;
				break;
			case 'B':
				prev += 30;
				break;
			case 'Q':
				prev += 40;
				break;
			case 'K':
				prev += 50;
				break;				
		}
		if(parts[2].charAt(0) == 'B'){prev += 100;}

		//Reinstate the piece
		GRID[dy][dx] = prev;

		//Change the turn
		TURN = (TURN == 'W') ? 'B' : 'W';
		lblREF.setText((TURN == 'W') ? "WHITE PLAYS": "BLACK PLAYS");

		//Check the check
		RULE_SET.isChecked();

		//Reset the can move array
		resetCanMove();

		//Redraw board
		update(pnlREF.getGraphics());
	}

	/**
	* Function used to undo the last move from the MOVE_LIST
	*/
	public void Undo(){
		//If there are no moves to be undone
		if(MOVE_LIST.size() == 0){
			//Print error message and exit the function
			JOptionPane.showMessageDialog(null, "There is no move to undo", "Cannot Undo", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Reverse the move using the reverseMove function
		reverseMove(MOVE_LIST.lastElement());

		//Remove the move entry from the List and Vector
		MOVE_LIST.removeElementAt(MOVE_LIST.size() - 1);
		lstREF.setListData(MOVE_LIST);
	}

	/**
	* Function used to save the move list to a save file
	* @param flGame Reference to the file to be written to
	*/
	public void saveGame(File flGame){
		//Commbine Move list into one String
		String Text = MOVE_LIST.elementAt(0) + "\n";
		for(int i = 1; i < MOVE_LIST.size(); i++){
			Text += MOVE_LIST.elementAt(i) + "\n";
		}

		//Write to file
		try{
			//Open file stream
			FileOutputStream out = new FileOutputStream(flGame);
			//Write output
			out.write(Text.getBytes());
			//Close File Stream
			out.close();
			//Output to the user
			JOptionPane.showMessageDialog(null, "Your game has been saved successfully", "Save Successful", JOptionPane.INFORMATION_MESSAGE);

		}catch(Exception e){}
		
	}

	/**
	* Function used to load a chess game from a file containing move log entries
	*/
	public void loadGame(File flGame){
		//Grid Coordinates
		int x = 0, y =0, dx =0, dy = 0;
		//Read type for each line in the file
		int type = 0;

		//Create a new game
		resetGame();

		//Replay all moves
		try{
			//File reader
			Scanner read = new Scanner(flGame);
			
			//Till EOF
			while(read.hasNext()){
				//Get stinf input
				String part = read.next();
				
				//First part of line
				if(type % 3 == 0){//Store positions
					//From
					x = (int)(part.charAt(0)) - (int)('a');
					y = 8 - Integer.parseInt(part.charAt(1) + "");
				}
				//Second part of line
				else if(type % 3 == 1){//Replicate move 
					//To
					dx = (int)(part.charAt(0)) - (int)('a');
					dy = 8 - Integer.parseInt(part.charAt(1) + "");

					//Move the piece
					pieceMove(x, y, dx, dy);
				}

				//Increment type counter
				type++;
				
				//System.out.println(read.next());

			}
		}catch(Exception e){}

		//Output success message to user
		JOptionPane.showMessageDialog(null, "Your game has been loaded successfully", "Load Successful", JOptionPane.INFORMATION_MESSAGE);
	}

	/** 
	* Mouse click event manager
	* @param event Mouse event
	*/
	public void mousePressed(MouseEvent event){
		if(GAME_OVER){return;}
		//Redraw board and pieces
		update(pnlREF.getGraphics());

		//Parse x and y coordinate of click
		int x = (event.getX() - xOff) / SQUARE_SIZE, y = (event.getY() - yOff) / SQUARE_SIZE;
		//If not within board assign -1
		if(x < 0 || x > 7){x = -1;}
		if(y < 0 || y > 7){y = -1;}

		//If invalid x or y block number
		if(x == -1 || y == -1){
			//Unselect and exit
			SELECTED[0] = -1;
			SELECTED[1] = -1;
			return;
		}
		
		if(!PRESPECT){y = 7 - y;}
		//Check can move
		if(CANMOVE[y][x] == 1){// BLUE MOVABLE SQUARE
			//Simulate chess move
			pieceMove(SELECTED[0], SELECTED[1], x, y);
			//Reset can move array
			resetCanMove();
			//Exit
			return;
		}else{// UNMOVABLE SQUARE
			if(getColor(GRID[y][x]) != TURN){return;}
			//Select square
			SELECTED[0] = x;
			SELECTED[1] = y;
			//Reset can move array
			resetCanMove();	
		}
		
		//System.out.println(x + ", " + y);
		
		//Calculate moves		
		if(GRID[y][x] == 0){// NO CHESS PIECE SELECTED
			//Unselect
			SELECTED[0] = -1;
			SELECTED[1] = -1;
			//Reset can move array
			resetCanMove();
		}else{// CHESS PIECE SELECTED
			//Calcuate moves
			RULE_SET.calcMoves(x, y);	
		}

		//ReDraw Board
		update(pnlREF.getGraphics());
	}

	public void mouseReleased(MouseEvent event){
		
	}

	public void mouseClicked(MouseEvent event){
		
	}

	public void mouseEntered(MouseEvent event){
		
	}

	public void mouseExited(MouseEvent event){
		
	}

	/** 
	* Draw things on the screen using double buffering
	* @param g
	*/
	public void update(Graphics g){
		//Double buffering
		Image dbImage = pnlREF.createImage(pnlREF.getWidth(), pnlREF.getHeight());
		Graphics dbg = dbImage.getGraphics();
		
		//What to draw
		//dbg.drawString("Linux draws just fine", 200, 200);	
		drawBoard(dbg, xOff, yOff);
		
		
		//Opacity and Delay
		int op = 10, del = 5;

		//Draw Board Border
		for(int i = 0; i < 50; i++){
			//Change opacity after delay
			if(i % del == 0){op += 10;}
			//Set the color 
			dbg.setColor(new Color(BLACK.darker().darker().getRed(), BLACK.darker().darker().getGreen(), BLACK.darker().darker().getBlue(), 255 - op));
			//Draw Board Borders
				//Left
			dbg.drawLine(xOff - i, yOff - i , xOff - i, yOff + SQUARE_SIZE * 8 + i );
				//Top
			dbg.drawLine(xOff - i, yOff - i , xOff + SQUARE_SIZE * 8 + i, yOff - i );
				//Right
			dbg.drawLine(xOff + SQUARE_SIZE * 8 + i, yOff - i , xOff + SQUARE_SIZE * 8 + i, yOff + SQUARE_SIZE * 8 + i );
				//Bottom
			dbg.drawLine(xOff - i, yOff + SQUARE_SIZE * 8 + i , xOff + SQUARE_SIZE * 8 + i, yOff + SQUARE_SIZE * 8 + i );
		}

		//Draw Coordinates
		if(PRESPECT){
			for(int i = 0; i < 8; i++){
				//Set Color
				dbg.setColor(new Color(WHITE.getRed(), WHITE.getGreen(), WHITE.getBlue()));
				//Draw coordinates
				dbg.drawString(ALPHA[i], SQUARE_SIZE * i + 75, SQUARE_SIZE * 8 + yOff + 30);
				dbg.drawString((8 - i) + " ", xOff - 30, SQUARE_SIZE * i + 80);
			}
		}else{
			for(int i = 7; i >= 0; i--){
				//Set Color
				dbg.setColor(new Color(WHITE.getRed(), WHITE.getGreen(), WHITE.getBlue()));
				//Draw coordinates
				dbg.drawString(ALPHA[i], SQUARE_SIZE * i + 75, SQUARE_SIZE * 8 + yOff + 30);
				dbg.drawString((i+1) + " ", xOff - 30, SQUARE_SIZE * i + 80);
			}
		}
		//Double buffering
		g.drawImage(dbImage, 0, 0, pnlREF);
		
	}


}