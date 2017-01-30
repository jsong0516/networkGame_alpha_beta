/*
* Name : Byung Gon Song
* ID : cs61b-ayg
* Project 2
* Description: This is a class for board ADT. 
*/


package player;


public class Board {
	public static final int UP = 12;
	public static final int DRU = 1;
	public static final int RIGHT = 3;
	public static final int DRD = 5;
	public static final int DOWN = 6;
	public static final int DLD = 7;
	public static final int LEFT = 9;
	public static final int DLU = 11;
	public static final int INVALID_COLOR = -1;
	public static final int VISITED = 2;
	public static final int P1 = 0; // BLACK
	public static final int P2 = 1; // WHITE
	public static final int UNDETERMINED = -2;
	public static final int P1_WIN = 1; // BLACK_WIN
	public static final int P2_WIN = -1; // WHITE_WIN
	public static final int DRAW = 0;
	public static final int NUM_PLAYER = 2;
	public static final int NUM_MAX_MOVE = 10;
	public static final int[] INVALIDPOSITION = {0, 70, 7, 77};
	public static final int[] INVALID_P1 = {10, 20, 30, 40, 50, 60, 17, 27, 37, 47, 57, 67}; // white is p1, first. Read readme.pdf for more info
	public static final int[] INVALID_P2 = {1, 2, 3, 4, 5, 6, 71, 72, 73, 74, 75, 76}; // black is p2, second
	public static final int CONNECTION_TO_WIN = 5;
	public static final int MAX_X = 7;
	public static final int MAX_Y = 7;
	public static final int DIMENTION = 8;
}
