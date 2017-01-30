package player;
import list.*;


public class GameBoard {
	
	public final static int DIMENSION = 8;//Board Dimension should be 8
	
	// -1 represent empty, 0 represent Black, 1 represent White
	public final static int BLACK = 0;
	public final static int WHITE = 1;
	public final static int EMPTY = -1;
	
	// initialize the game board and total chips that we can play
	public int blackChip = 10;
	public int whiteChip = 10;
	
	// Initialize the direction field this gonna help us to check if it has network
	// We Have total 8 direcitons: UP, DOWN, LEFT, RIGHT, DIAGONAL RIGHT UP,DIAGONAL RIGHT DOWN,DIAGONAL LEFT DOWN,DIAGONAL LEFT UP
	// We group have discussed that we need to use short name for the direction to make our program more concise and readable(for us when we program).
	public static final int UP = 12;   // UP 
	public static final int DRU= 1;    // DIAGONAL RIGHT UP
	public static final int RIGHT = 3; // RIGHT
	public static final int DRD = 5;   // DIAGONAL RIGHT DOWN
	public static final int DOWN = 6;  // DOWN 
	public static final int DLD = 7;   // DIAGONAL LEFT DOWN
	public static final int LEFT = 9;  // LEFT
	public static final int DLU = 11;  // DIAGONAL LEFT UP
	public static final int VISITED = 2;
	public static final int CONNECTION_TO_WIN = 5;
	public static final int MAX_X = 7;
	public static final int MAX_Y = 7;
	
	// current connections of white Chips
	protected int white_connection = 0;
	// current connections of black Chips
	protected int black_connection = 0;
	private int[][] board;//gameBoard board

	public GameBoard() {
		board = new int[DIMENSION][DIMENSION];//gameBoard board
		white_connection = 0;
		black_connection = 0;
		for(int i = 0 ; i < DIMENSION ; i++){
			for(int j = 0 ; j < DIMENSION ; j++){
				board[i][j]= EMPTY;// set the initial gameboard to Empty
			}
		}			
	}
	
	/**
	 * This constructor duplicates a board.
	 * It takes in a Board and duplicates its contents.
	 * This constructor is use for the game after all 10 chips are runned out 
     * @param temp is the current Board we are duplicating.
     */
    public GameBoard (GameBoard temp){
	board = new int[DIMENSION][DIMENSION];//gameBoard board - Duplicate but more efficient
    	for (int x = 0; x < DIMENSION; x++) {
    		for (int y =0; y < DIMENSION; y++) {
    			this.board[x][y] = temp.board[x][y];
    		}
    	}
        this.whiteChip = temp.whiteChip;
        this.blackChip = temp.blackChip;
    }
    
    //***********************************************************************************************************
    // Module 1																									*
    // The following methods check if the game has a validmove													*
    // GetChip method     : A method  can look up whether a cell has a black piece, white piece, or is empty 	*
    // Corner method      : A method to check if the position is the corner of the gameBoard					*
    // GoalState method   : A method to check the goal state for black and white								*
    // Neighbors method   : A method to find around neighbors of give chips position							*
    // isClustered method : Determines whether the move with create a cluster if a chip is moved there			*
    // Above are the helper methods to check if there is a validmove											*
    //***********************************************************************************************************
    
	/**
	 * A method  can look up whether a cell has a black piece, white piece, or is empty 
	 * by given the (x, y) coordinates of the cell.
	 * @param x the x coordinate of the spot
     * @param y the y coordinate of the spot
     * @return the contents of the board in that spot (-1, 0, 1 for empty, black, or white)
	 */
	protected int getChip(int x, int y) {
		if (x < 0 || x > MAX_X || y < 0 || y > MAX_Y) { 
			return EMPTY;
		} else {
		    return board[x][y];
	       }
	}
	
	/**
	 * A method to check if the position is the corner of the gameBoard
	 * Where corner is not allow to place chips
	 * [0][0], [0][7], [7][0],[7][7] should return false
	 * other should return true.
	 * @param x - The x coordinate of the position. 
     * @param y - The y coordinate of the position.
     * @return true is the position is on a corner, false otherwise
	 */
	private boolean corner(int x, int y) {
	//check the corners
		if ((x == 0 && y == 0) || (x == 0 && y == MAX_Y) || (x == MAX_X && y == 0) || (x == MAX_X && y == MAX_Y)) {
			return true;
		}
		return false;
	}
	
	/** 
	 * A method to check the goal state for black and white
	 * where [0][1....6],[7][1....6] should be white.
	 * [1...6][0], [1...6][7] whould be black
	 */
	
	private boolean goalState(Move m, int Color) {
		if (Color == BLACK) {
			if ((m.y1 == 0) || (m.y1 == MAX_Y)) {
				return true;			
			}
		}
		if (Color == WHITE) {
			if ((m.x1 == 0) || (m.x1 == MAX_Y)) {
				return true;			
			}
		}
		return false;
	}
	
	/** 
	 * A method to find around neighbors of give chips position
	 * This method is intend to help the isValidMove method
	 * return all the neighbors of the given position
	 * where neighbors[i][0] is the x coordinate,
	 * neighbors [i][1] is the y coordinate
	 * @param x - The x coordinate of the position. 
	 * @param y - The y coordinate of the position. 
	 * @return an int[] array containing the positions of each neighbors
	 */
	 private int[][] neighbors(int x,int y) {
		int[][] n = {{x,y+1}, {x,y-1}, {x+1,y+1}, {x+1,y-1}, {x+1,y},{x-1,y},{x-1,y-1},{x-1,y+1}};
		return n;
	 }
	
	/**
	 * Determines whether the move with create a cluster if a chip is moved there
	 * check the condition 2 of the legal move
	 * @param m the Move being considered 
	 * @param color the color of the chip being placed
	 * @return true if a cluster is created, false otherwise
	 */
	 private boolean isClustered(Move m, int color){
		 int count = 0;
		 int[][] n = neighbors(m.x1, m.y1);

		 if (m.moveKind == Move.ADD){// if game is not over, we can still add chip
			 for (int i = 0; i< n.length; i++) {
				if (getChip(n[i][0], n[i][1]) == color) {// if chip's neighbors have same color chip count plus 1
					count++;
	               			int[][] temp = neighbors(n[i][0], n[i][1]);// check neighbor's neighbor
					for (int j = 0; j< n.length; j++){
					      if(getChip(temp[j][0],temp[j][1]) == color) {// if neighbors's neighbors also have the same color chip
					    	  count++;
					      }
					}
				} 
			 }
			 if (count > 1){ // if there are more then two chips around means isClustered
				 return true;
			 }
			 
		 } else if (m.moveKind == Move.STEP) { // Edited
			 GameBoard temp = new GameBoard(this); // Could be inefficient BUT THIS way is better to make each function do exactly one job!
			 temp.board[m.x2][m.y2] = EMPTY; // reset the old position to empty
			 if (color == WHITE) {
				temp.whiteChip++;
			 } else {
				temp.blackChip++;
			 }
			 return temp.isClustered(new Move(m.x1, m.y1), color);
		 }
		 return false;
	 }	
	 
	
	
	
	/**
	 * Determines whether a move is valid or not
	 * it's should following the rules below
	 * 1. each connected line has to be the different direction.
	 * 2. if the chips are connected the around space is illegal.(opponent can put it on)
	 * 3. no 2 chips can play on the same location	
     * 4. position[0][0],[0][7],[7][0],[7][7] cannot put any chips
     * 5.no constructive 3 chips on a line.
     * we should have 5 methods to check each condition above
	 * @param m the move being evaluated
	 * @param playerColor the player being evaluated
	 * @return true if the move is valid, false otherwise
	 */
	protected boolean isValidMove(Move move, int color) {
		// if it's in the corner or the chip we gonna place is not empty return false
				// this should apply for both Step and Add
				if (getChip(move.x1, move.y1) != EMPTY) {
					return false;
				}

				if (corner(move.x1, move.y1)) {
					return false;
				}
				// Can't step a piece to the same spot
				if (move.moveKind == Move.STEP) {
					if(move.x1 == move.x2 && move.y1 == move.y2) {
						return false;
					}
				}
				if (onBoard(move)){ // if the move is on the board
				    if (color == BLACK){
				    	if (move.moveKind == Move.ADD && blackChip == 0){ // if we run of of chips
				    		return false;
				    	}
				    	if (goalState(move, WHITE) || isClustered(move, color)) {
				    		return false;
				    	}     
				    }else if (color == WHITE){
				            if (move.moveKind == Move.ADD && whiteChip ==0){
				            	return false;
				            }
				            if (goalState(move, BLACK) || isClustered(move, color)) {
				            	return false;
				            }
				    }
				}
				return true;
	}


	/**
	 * check if the location is the board
	 * if the location is onBoard return true,else return false
	 * Where only [0][0],[0][1]......[7][7] is on chip
	 * @param m the Move objet
	 * @return true if it is on the board
	 */
	private boolean onBoard(Move m) {
		if (m.x1 < 0 || m.x1 > MAX_X || m.y1 < 0 || m.y1 > MAX_Y) {
			return false;
		}
		return true;
	}
	
	/** 
	 * makeMove takes a legal move and an int color and changes the board. It can make
     * an add move or a step move and it labels the move as which it is. It adds the piece and needs to 
     * decrement the amount of pieces left for each player.
     * @param m - Takes in a move to make
     * @param color - Determines which player is making the move
     * @return void
	*/

	public void makeMove(Move m, int color) {
		if (m.moveKind == Move.ADD) {
		    board[m.x1][m.y1] = color;
		    if (color == GameBoard.WHITE) {
		        whiteChip--;
		    } if (color == GameBoard.BLACK) {
		        blackChip--;
		    }
		} else if (m.moveKind == Move.STEP) {
			board[m.x2][m.y2] = GameBoard.EMPTY; 
			board[m.x1][m.y1] = color; 
		}
	}
	
	
	/**
 	 * Uses the rules of the game and positions of the current chips
 	 * to generate a list of valid moves
 	 * @param playerColor the color of the player whose valid moves are being determiend
 	 * @return an array of Move objects which represent valid moves for the current game situation
 	 */
 	protected DList possibleMoves(int color){
 		DList possibleMoves = new DList();
 		int chips = (color == WHITE) ? whiteChip : blackChip;
 		
 		
 		if (chips > 0){ //if there are chips left,store all kinds of possiblemoves
 			for (int i = 0; i < DIMENSION; i++){
 				for (int j = 0 ; j < DIMENSION ;j++){
 					Move temp = new Move(i,j);
 					if(isValidMove(temp,color)){
 						possibleMoves.insertBack(temp);
 					}
 				}
 			}
 		} else {// generate valid step moves
 			DList positions = new DList();
 			for (int j = 0 ; j < DIMENSION ; j++){ //generate a list of positions where there are chips of the desired color
 				for (int i = 0; i < DIMENSION; i++){
					Move coordinate = new Move(); // This is quit but that is fine
 					if (getChip(i,j) == color){ 
 						coordinate.x1 = i;
 						coordinate.y1 = j;
 						positions.insertBack(coordinate);
 					}
 				}
 			}
 			for (int j = 0; j< DIMENSION;j++){
 				for (int i = 0 ;i< DIMENSION;i++){
 					DListNode currNode = (DListNode) positions.front();
 					try{
 						while (currNode.isValidNode()){
 							Move coords = (Move)currNode.item();
 							Move m = new Move(i,j,coords.x1, coords.y1); //try to move each chip in the list from its current position to 
 							if (isValidMove(m, color)) {            
 								possibleMoves.insertBack(m);
 							}
 	                        			currNode = (DListNode) currNode.next();
 						}
 					}catch (InvalidNodeException e){
 					}
 				}
 			}
 		}
 	    return possibleMoves;
 	}

 	//*****************************************************************************************************************
    // Module 2																								   		   *
    // The following methods check if the game has a Network														   *
    // currentConnection method  	: A method  can look up whether a cell has a black piece, white piece, or is empty *
    // hasNetwork method      		: A method to check if the position is the corner of the gameBoard				   *
    // isGoal method   				: A method to check the goal state for black and white							   *
    // isOppositeTeamStart method   : A method to find around neighbors of give chips position					       *
    // restConnection method 		: Determines whether the move with create a cluster if a chip is moved there	   *
    // Above are the helper methods to check if there is a Network in the current Board							       *
    //******************************************************************************************************************
    
     
 	

	/**
	 * A method to check the currentConnection that we have in the board
	 * This method can be help in the evaluator
	 * @param color the player's color either black of white
	 * @return currentConnection how many current connection that we have for now
	 */
	public int currentConnection(int color) {
		if (color == BLACK) {
			return black_connection;
		}
		return white_connection;
	}

	/**
	 * HasNetwork returns true if this board has a network with 6 or greater length. 
	 * A valid Connect win should have as least 5 lines connected
	 * And each line should have the different directions.
	 * two lines with the same directions should consider invalid connection
	 * Another winning condition is two goal state must connected in order to win
     * It takes in a player because it is called on the board,
     * It calls a recursive, private function restConnection that repeatedly 
     * @param color the color of the player whose network is being determined
     * @return true if this board has a network with 6 or greater length or false otherwise 
	 */

	public boolean hasNetwork(int color) {
		boolean network = false;
		
		if (color == BLACK) {
			black_connection = 0;
		} else {
			white_connection = 0;
		}


	
		slist connection = new slist();
		if(color == BLACK) // Black
			for(int x = 0; x <= MAX_X ; x++) {
				if(board[x][0] == color) {
					connection.insert(new Move(x, 0));
				}
			}
		else if(color == WHITE)
			for(int y = 0; y <= MAX_Y ; y++) {
				if(board[0][y] == color) {
					connection.insert(new Move(0, y));
				}
			}

		// Shortcut; if connection is size == 0, no starting point, therefore no network could be made
		if(connection.size() == 0) {
			return false;
		}

		// Set Beginning nodes to be visited
		slistNode node = connection.first();
		while(node != null) {
			Move position = (Move)node.item;
			board[position.x1][position.y1] = VISITED;
			node = node.next;
		}
		
		slistNode node2 = connection.first();

		// Check if there is network from starting points
		while(node2 != null) {
			Move first = (Move)node2.item;
			if(restConnection(first.x1, first.y1, color, EMPTY, 1)) {
				network = true;
				break;
			}
			node2 = node2.next;
		}
		// Set color back
		slistNode node3 = connection.first();
		while(node3 != null) {
			Move position = (Move)node3.item;
			board[position.x1][position.y1] = color;
			node3 = node3.next;
		}	
		
		return network;
	} 

	
	/*
	 * Check if the location is in the Goal Area
	 * @param x the x position of the given coordinate
	 * @param y the y position of the given coordinate
	 * @return true if the location is in Goal Area
	 */
	protected boolean isGoal(int x, int y) {
		return x == MAX_X ||  y == MAX_Y;
	}
	
	/*
	 * Check if the location is in the start Area
	 * @param x the x position of the given coordinate
	 * @param y the y position of the given coordinate
	 * @return true if the location is in start Area
	 */
	protected boolean isStart(int x, int y) {
		return x == 0 ||  y == 0;
	}



	/**
	 * Check if the position is the starting point of other color
	 * @param x the x position of the given coordinate
	 * @param y the y position of the given coordinate
	 * @param color represent the player in the given coordinate
	 * @return true if isOppositeTeamStart
	 */
	protected boolean isOppositeTeamStart(int x, int y, int color) {
		if(color == WHITE) {
			return  y == 0;
		} else {
			return  x == 0;
		}
	}
	
	/** Helper method for has Network. From has Network, it receives starting point and check if there is any connection to end(goal)
	 * This also updates the current connection which we will be using for evaluation.
	 * @param x the x position of the given coordinate
	 * @param y the y position of the given coordinate
	 * @param color the current color the player
	 * @param direction the current connectons
	 * @param chip_count how many chips that we have used
	 * @return true
	 */
	private boolean restConnection(int x, int y, int color, int direction, int chip_count) {
		//base case
		boolean end = false;
		if(!onBoard(new Move(x,y))) {
			return false;
		}

		if(isGoal(x, y) || isOppositeTeamStart(x, y, color)) {
			return board[x][y] == color && chip_count > CONNECTION_TO_WIN; 
		}		
	
		board[x][y] = VISITED;
		// Check Iterate through up
		if (!end && direction != UP) {
			for(int y1 = y - 1; y1 >= 0; y1 --) {
				int position = board[x][y1];
				if (position == color) {
					end = restConnection(x, y1, color, UP, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Diagonal Right up
		if (!end && direction != DRU) {
			int x1 = x + 1;
			for(int y1 = y - 1; x1 <= MAX_X && y1 >= 0; x1++, y1--) {
				int position = board[x1][y1];
				if (position == color) {
					end = restConnection(x1, y1, color, DRU, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Right
		if (!end && direction != RIGHT) {
			for(int x1 = x + 1; x1 <= MAX_X; x1++) {
				int position = board[x1][y];
				if (position == color) {
					end = restConnection(x1, y, color, RIGHT, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Diagonal Right down
		if (!end && direction != DRD) {
			int x1 = x + 1;
			for(int y1 = y + 1; y1 <= MAX_Y && x1 <= MAX_X ; x1++, y1++) {
				int position = board[x1][y1];
				if (position == color) {
					end = restConnection(x1, y1, color, DRD, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Down
		if (!end && direction != DOWN) {
			for(int y1 = y + 1; y1 <= MAX_Y; y1++) {
				int position = board[x][y1];
				if (position == color) {
					end = restConnection(x, y1, color, DOWN, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Diagonal Down Left
		if (!end && direction != DLD) {
			int x1 = x - 1;
			for(int y1 = y + 1; x1 >= 0 && y1 <= MAX_Y; x1--, y1++) {
				int position = board[x1][y1];
				if (position == color) {
					end = restConnection(x1, y1, color, DLD, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Left
		if (!end && direction != LEFT) {
			for(int x1 = x - 1 ; x1 >= 0; x1--) {
				int position = board[x1][y];
				if (position == color) {
					end = restConnection(x1, y, color, LEFT, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		// Check Diagonal Left up
		if (!end && direction != DLU) {
			int x1 = x - 1;
			for(int y1 = y - 1; y1 >= 0 && x1 >= 0; x1--, y1--) {
				int position = board[x1][y1];
				if (position == color) {
					end = restConnection(x1, y1, color, DLU, chip_count + 1);
					break;
				} else if(position != EMPTY) {  // No connection is found
					break;
				}
			}
		}
		if(end) {
			update_connection(color, chip_count);
		}
		// If not goal State, no connection made.

		board[x][y] = color;
		return end;
	}
	/**
	 * This function takes connection and color update accordingly.
	 * @param color - current color
	 * @param connection to be update
	 */
	private void update_connection(int color, int connection) {
		if(color == BLACK) {
			black_connection = black_connection > connection ? black_connection : connection;
		}
		else {
			white_connection = white_connection > connection ? white_connection : connection;
		}
	}
}
