/* MachinePlayer.java */

package player;

import list.DList;
import list.DListNode;
import list.InvalidNodeException;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
 
  protected int color;
  protected int depth;
  protected int opp_color;
  protected GameBoard board;
  public static final boolean COMPUTER = true;
  private static final int DEFAULTDEPTH = 3;
  public final static int MYWIN = 100;
  public final static int OPPONENTWIN = -100;
  
  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
	  this.color = color;
	  this.depth = DEFAULTDEPTH;
	  board = new GameBoard();
	  if(color == GameBoard.BLACK)
		opp_color = GameBoard.WHITE;
	  else
		opp_color = GameBoard.BLACK;
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
	    this(color);
	    this.depth = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
	  Best best = bestMove(color);
	  board.makeMove(best.move, color);
      return best.move;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
	  if (board.isValidMove(m, opp_color)){
	    	board.makeMove(m, opp_color);
	    	return true;
	    }
	    return false;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
	  if (board.isValidMove(m, color)) {
		  board.makeMove(m,color);
		  return true;
	  }
	  return false;
  }
  
  /**
   * A protected method to call alphaBetaPruing class
   * we will  store the best move and best score into the Best class
   * In the Choose Move we will call this method to return the optimal move for MachinePlayer
   * @param color represent which player is playing the game
   * @return bestMove the Best class that contains the best class and best score
   */
  protected Best bestMove(int color){
	  int alpha = OPPONENTWIN-100000;
	  int beta = MYWIN + 100000;
	  boolean side = (this.color == color) ? true: false;
	  Best bestMove = alphaBetaPruning(side, depth, alpha, beta, this.board, true);
	  return bestMove;
  }
  
  
  /** 
   * Assigns a score to a board on the game tree. It is part of the Game Tree Search Module. It
   *  contains two important functions. It primarily checks if there is a network on the current board (by calling the and 
   *  then assigns a score based on recursively evaluating the next boards to a certain depth in order to 
   *  choose the next best possible move. It also needs to check if it is more advantageous to move forward for itself 
   *  or to ruin the opponent's network in progress. 
   *  @param side is the color of the player who's moves we are currently looking for.
   *  @param depth is how many more turns the algorithm can look ahead for a network or to evaluate a score.
   *  @param alpha is the score the computer knows with certainty it can achieve.
   *  @param beta is the score the opponent knows with certainty it can achieve.
   *  @param board to represent the chess board
   *  @param isFirst Check who plays the first black or white
   *  @return a Move which holds the highest scoring move. This is known as the BestMove.
   */
  private Best alphaBetaPruning(boolean side, int depth, double alpha, double beta, GameBoard board, boolean isFirst){
	  Best myBest =  new Best(); // my new best 
	  Best reply; 		     // Opponent best
	  int current = side ? color : opp_color;  	  
/*	///Base
	  if (board.hasNetwork(color) || board.hasNetwork(opp_color) || depth == 0){
		  myBest.score = Evaluator.CurrentScore(board, color, depth);
		  return  myBest;
	  }
*/
	  if (side){
		  myBest.score = alpha;
	  }else{
		  myBest.score = beta;
	  }
	 
	  try {
		  //find all possible moves for current chip storing in a DList
		  //DListNode.item stores move object
		  DList moves = (DList) board.possibleMoves(current);
		  
		  //pointer for each possible move
		  DListNode currNode = (DListNode) moves.front();
		  
		  while (currNode.isValidNode()){

			  GameBoard temp_board = new GameBoard(board);
			  temp_board.makeMove((Move)currNode.item(), current); 

			// Can this be here?
			//
			
			  if (temp_board.hasNetwork(color) || temp_board.hasNetwork(opp_color) || depth == 0){
				  myBest.score = Evaluator.CurrentScore(temp_board , color, depth);
				  myBest.move = (Move)currNode.item(); // No need
				  return  myBest;
			  }
			//



			  //recursive call
			  	reply = alphaBetaPruning(!side, depth - 1, alpha, beta, temp_board, false);




				if(reply.move.moveKind != Move.QUIT && reply.score > 0){
				System.out.println("SCORE : " + reply.score);
				System.out.println("MOVE  : " + reply.move);
				}


			  if (side && (reply.score > myBest.score)) {
				  myBest.move = (Move) currNode.item();
				  myBest.score = reply.score;
				  alpha = reply.score;

		          // Opponent turn, get minimum
			  }else if (!side && (reply.score < myBest.score)) {
				  myBest.move = (Move) currNode.item();
				  myBest.score = reply.score;
				  beta = reply.score;
			  }
			  
			  if (alpha >= beta && !isFirst){	   
				  return myBest;
			  }
			 //update currentNode
			  currNode = (DListNode) currNode.next();
		  }
	  }catch (InvalidNodeException e){
		System.out.println("ERRROR");
	  }
	  return myBest;
  }
}
