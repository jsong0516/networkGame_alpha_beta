/*
Name : BinYi LIU
ID : cs61b-anb
Project 2
Description: This is the class for BestMove, just have one more field score
*/

/**
 * This class is a data structure that holds a Move 
 * object and a score for that move object.
 */

package player;

public class Best {

	public double score;
	public Move move;

	public Best(){
		score =0.0;
		move =  new Move();
	}

}
	