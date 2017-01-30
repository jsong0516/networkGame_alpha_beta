package player;

public class Evaluator {

	private Evaluator() {}
	
	/**
	 * 
	 * 1.the color has more pairs assign a higher score;
	 * 2.establish at least one chip in each goal early in the game assign a higher score ;
	 * 
	 * @param board
	 * @param color
	 * @return computerScore-humanScore
	 */
	
	//suppose black is computer; white is human;
	public static int CurrentScore(GameBoard board, int color, int depth){ //suppose color is machinePlayer color
		int opponent_color = color == GameBoard.WHITE ? GameBoard.BLACK : GameBoard.WHITE;

		int score;

		int myGoal1 = 0; //number of computer chips in upper or left area;
		int myGoal2 = 0; //number of computer chips in 
		int opGoal1 = 0;
		int opGoal2 = 0;

		// Computer Color wins
		if(board.hasNetwork(color)){
			//return current color wins
			return MachinePlayer.MYWIN + depth;
		}
		if(board.hasNetwork(opponent_color)){
			//return opponent color wins
			return MachinePlayer.OPPONENTWIN - depth;
		}
		
		//condition 1:
		//the color has more pairs assign a higher score
		score = board.currentConnection(color) - board.currentConnection(opponent_color);
		
		
		//condition 2:
		//establish at least one chip in each goal early in the game assign a higher score

		
		// Get number of chips
		for(int i = 0 ; i < GameBoard.DIMENSION ; i++){
			if(board.getChip(0,i) == color || board.getChip(i, 0) == color){
				myGoal1++;
			}
			if(board.getChip(0,i) == opponent_color || board.getChip(i, 0) == opponent_color){
				opGoal1++;
			}
			if(board.getChip(GameBoard.DIMENSION - 1, i) == color || board.getChip(i, GameBoard.DIMENSION - 1) == color){
				myGoal2++;
			}
			if(board.getChip(GameBoard.DIMENSION - 1, i) == opponent_color || board.getChip(i, GameBoard.DIMENSION - 1) == opponent_color){
				opGoal2++;
			}
		}
		
	
		if(myGoal1==1 && myGoal2==1){
			score = score + 1;
		}else{
			score = score - 1;
		}
		if(opGoal1==1 && opGoal2==1){
			score = score + 1;
		}else{
			score = score - 1;
		}
		
		
		//condition3:
		//first step goes to the center of board if possible;
		
		
		if(board.getChip(3,3) == color || board.getChip(3,4) == color || board.getChip(4,3) == color || board.getChip(4,4) == color){
			score = score + 2;
		}
				
		
		
		
		return score;
		
		}
	
	
}
