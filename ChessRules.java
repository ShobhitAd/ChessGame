import java.util.Vector;
/** 
* Used to calculate the possible moves for chess pieces and see if a king is checked
* @author Shobhit
* @version 1.0
*/
public class ChessRules{
	//Reference to ChessGame
	ChessGame cgREF;

	//Color and type of the currently selected chesspieces
	char COLOR, TYPE;

	//Check variables
	boolean WHITE_CHECK, BLACK_CHECK;

	//Mode number of move checking (0:Normal, 1:Check)
	int MODE;

	/**
	* Constructor for Chess Rules
	* @param cg Reference to Chess Game
	*/
	public ChessRules(ChessGame cg){
		//Assign reference
		cgREF = cg;
		WHITE_CHECK = false;
		BLACK_CHECK = false;
		MODE = 0;	
	}

	/**
	* Calculate moves for selected chess piece
	* @param i X Block number
	* @param j Y Block number
	*/
	public void calcMoves(int i, int j){
		//System.out.println(cgREF.GRID[j][i]);
		
		//Get piece type and color
		TYPE = cgREF.getType(cgREF.GRID[j][i]);
		COLOR = cgREF.getColor(cgREF.GRID[j][i]);
		
		//Calculate moves based on chess piece type
		switch(TYPE){
			case 'P':// PAWN
				PawnRules(i, j);
				break;

			case 'N':// KNIGHT
				KnightRules(i, j);
				break;

			case 'B':// BISHOP
				DiagonalRules(i, j, 0);
				break;

			case 'R':// ROOK
				LineRules(i, j, 0);
				break;

			case 'Q':// QUEEN
				LineRules(i, j, 0);
				DiagonalRules(i, j, 0);
				break;

			case 'K':// KING
				KingRules(i, j);
				break;
					
		}
	}

	/**
	* Function used to enable cannmove or check
	* @param i  X Block number
	* @param j Y Block number
	*/
	public void markFlag(int i, int j){
		if(MODE == 0){//NORMAL MODE
			//Enable movement
			cgREF.CANMOVE[j][i] = 1;
		}
		else if (MODE == 1){//CHECK MODE
			//Toggle the corresponding check variable to true
			if(cgREF.getType(cgREF.GRID[j][i]) == 'K'){
				//WHITE CHECK
				if(cgREF.getColor(cgREF.GRID[j][i]) == 'W'){WHITE_CHECK = true;}
				//BLACK CHECK
				if(cgREF.getColor(cgREF.GRID[j][i]) == 'B'){BLACK_CHECK = true;}
			}		
		}
	}

	/**
	* Check if a particular square is empty
	* @param i X Block number
	* @param j Y Block number
	* @return true if the square is unoccupied
	*/
	public boolean isEmpty(int i, int j){
		//Invalid block number
		if(i < 0 || i >= 8){return false;}
		if(j < 0 || j >= 8){return false;}
		
		//Check if unoccupied
		return (cgREF.GRID[j][i] == 0);
	}

	/**
	* Check if a particular square is occupied by an enemy chesspiece
	* @param i X Block number
	* @param j Y Block number
	* @return true if the square is occupied by an enemy chesspiece
	*/
	public boolean isEnemy(int i, int j){
		//Invalid block number
		if(i < 0 || i >= 8){return false;}
		if(j < 0 || j >= 8){return false;}

		//Check if enemy
		return (cgREF.GRID[j][i] != 0 && cgREF.getColor(cgREF.GRID[j][i]) != COLOR);
	}

	/**
	* Check if a king of a certain color is checked
	*/
	public void isChecked(){
		//Reset to false
		BLACK_CHECK = false;
		WHITE_CHECK = false;

		//Toggle check mode
		MODE = 1;

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				//If the square is not unoccupied
				if(!isEmpty(i, j)){
					//Calculate all possible moves for the piece
					calcMoves (i, j);
				}
			}
		}

		//Toggle normal mode
		MODE = 0;
	}

	/**
	* Set of rules used to calculate the possible moves for a King
	* @param i X Block number
	* @param j Y Block number
	*/
	public void KingRules(int i, int j){
		
		if(isEmpty(i + 1, j) || isEnemy(i + 1, j)){ //RIGHT
			markFlag(i + 1, j);
		}
		if(isEmpty(i - 1, j) || isEnemy(i - 1, j)){ //LEFT
			markFlag(i - 1, j);
		}
		if(isEmpty(i, j + 1) || isEnemy(i, j + 1)){ //BELOW
			markFlag(i, j + 1);
		}
		if(isEmpty(i, j - 1) || isEnemy(i, j - 1)){ //ABOVE
			markFlag(i, j - 1);
		}
		if(isEmpty(i + 1, j + 1) || isEnemy(i + 1, j + 1)){ //BELOW-RIGHT
			markFlag(i + 1, j + 1);	
		}
		if(isEmpty(i + 1, j - 1) || isEnemy(i + 1, j - 1)){ //ABOVE-RIGHT
			markFlag(i + 1, j - 1);			
		}
		if(isEmpty(i - 1, j + 1) || isEnemy(i - 1, j + 1)){ //BELOW-LEFT
			markFlag(i - 1, j + 1);		
		}
		if(isEmpty(i - 1, j - 1) || isEnemy(i - 1, j - 1)){ //ABOVE-LEFT
			markFlag(i - 1, j - 1);			
		}
	}

	/**
	* Set of rules used to calculate the possible moves for a piece which can move in straight lines
	* @param i X Block number
	* @param j Y Block number
	* @param dir which direction to traverse
	*/
	public void LineRules(int i, int j, double dir){
		
		if(dir == 0){//At the first iteration
			//Go over every direction
				//TOP
			LineRules(i, j - 1, -1);
				//BOTTOM
			LineRules(i, j + 1, 1);
				//LEFT
			LineRules(i - 1, j, -0.5);
				//RIGHT
			LineRules(i + 1, j, 0.5);
		}else{//Not first iteration

			if(isEmpty(i, j) || isEnemy(i, j)){//If the square is unoccupied or occupied by enemy
				//Set can move to true 
				markFlag(i, j);
			}
			if(isEmpty(i, j)){// If can still progress in a direction
				//Continue in the selected direction
					//TOP
				if(dir == -1){LineRules(i, j - 1, -1);}
					//BOTTOM
				if(dir == 1){LineRules(i, j + 1, 1);}
					//LEFT
				if(dir == -0.5){LineRules(i - 1, j, -0.5);}
					//RIGHT
				if(dir == 0.5){LineRules(i + 1, j, 0.5);}	
			}
		}		
	}
	
	/**
	* Set of rules used to calculate the possible moves for a piece which can move diagonally
	* @param i X Block number
	* @param j Y Block number
	* @param dir which direction to traverse
	*/
	public void DiagonalRules(int i, int j, double dir){
		
		if(dir == 0){//At the first iteration
			//Go over every direction
				//TOP-LEFT
			DiagonalRules(i - 1, j - 1, -1);
				//BOT-RIGHT
			DiagonalRules(i + 1, j + 1, 1);
				//BOT-LEFT
			DiagonalRules(i - 1, j + 1, -0.5);
				//TOP-RIGHT
			DiagonalRules(i + 1, j - 1, 0.5);
		}else{//Not first iteration

			if(isEmpty(i, j) || isEnemy(i, j)){//If the square is unoccupied or occupied by enemy
				//Set can move to true
				markFlag(i, j);
			}
			if(isEmpty(i, j)){//If can still progress in a direction
				//Continue in the selected direction
					//TOP-LEFT
				if(dir == -1){DiagonalRules(i - 1, j - 1, -1);}
					//BOT-RIGHT
				if(dir == 1){DiagonalRules(i + 1, j + 1, 1);}
					//BOT-LEFT
				if(dir == -0.5){DiagonalRules(i - 1, j + 1, -0.5);}
					//TOP-RIGHT
				if(dir == 0.5){DiagonalRules(i + 1, j - 1, 0.5);}	
			}
		}
	}

	/**
	* Set of rules used to calculate the possible moves for a Knight
	* @param i X Block number
	* @param j Y Block number
	*/
	public void KnightRules(int i, int j){
		//Offset check
		int x = 0, y = 0;

		//Direction toggle
		for(int k = 0; k < 2; k++){
			//Swap position check depending on iteration
				//2 vertical and 1 horizontal
			if(k == 0){x = 1; y = 2;}
				//1 vetrical and 2 horizontal
			if(k == 1){x = 2; y = 1;}
			//Check for all 4 possible moves for particular orientation 
			for(int l = 0; l < 4; l++){
				//If the square is unoccupied or occupied by an enemy
				if(isEmpty(i + x, j + y) || isEnemy(i + x, j + y)){
					//Mark square
					markFlag(i + x, j + y);
				}
				//Swap direction
				x = -x;
				if(l % 2 == 1){y = -y;}
			}
		}
		
	}

	/**
	* Set of rules used to calculate the possible moves for a Pawn
	* @param i X Block number
	* @param j Y Block number
	*/
	public void PawnRules(int i, int j){
		//check = true;	
		switch(COLOR){

			case 'W'://WHITE PAWN
				//Move forward
				if(isEmpty(i, j - 1)){
					cgREF.CANMOVE[j - 1][i] = 1;
				}

				//Kill enemy on right
				if(isEnemy(i + 1,j - 1)){
					markFlag(i + 1, j - 1);	
				}

				//Kill enemy on left
				if(isEnemy(i - 1,j - 1)){
					markFlag(i - 1, j - 1);
				}

				//First move can move two steps
				if(j == 6 && isEmpty(i, j - 2)){
					cgREF.CANMOVE[j - 2][i] = 1;
				}
				break;

			case 'B':
				//Move forward
				if(isEmpty(i, j + 1)){
					cgREF.CANMOVE[j + 1][i] = 1;
				}

				//Kill enemy on right
				if(isEnemy(i + 1,j + 1)){
					markFlag(i + 1, j + 1);
				}

				//Kill enemy on left
				if(isEnemy(i - 1,j + 1)){
					markFlag(i - 1, j + 1);
				}

				//First move can move two steps
				if(j == 1 && isEmpty(i, j + 2)){
					cgREF.CANMOVE[j + 2][i] = 1;
				}
				break;	
		}
	}
}