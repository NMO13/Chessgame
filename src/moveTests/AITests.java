package moveTests;

import java.util.Date;
import java.util.Random;

import chess.Board;
import chess.Move;
import chess.MyPlayer;
import chess.Player;

public class AITests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//test1();
		test2();
	}
	
	static void test2() {
		Board board = new Board();
		makeMove("wP c2-c4", board);
		makeMove("bP f7-f6", board);
		makeMove("wP e2-e4", board);
		makeMove("bP a7-a6", board);
		makeMove("wP d2-d4", board);
		makeMove("bP c7-c6", board);
		makeMove("wB f1-d3", board);
		makeMove("bP g7-g6", board);
		makeMove("wB c1-f4", board);
		makeMove("bB f8-h6", board);
		makeMove("wB f4xh6", board);
		makeMove("bN g8xh6", board);
		makeMove("wN g1-f3", board);
		makeMove("bN h6-f7", board);
		makeMove("wK e1-g1", board);
		makeMove("bP d7-d6", board);
		makeMove("wN b1-c3", board);
		makeMove("bB c8-g4", board);
		makeMove("wP h2-h3", board);
		makeMove("bB g4-e6", board);
		makeMove("wP b2-b4", board);
		makeMove("bR a8-a7", board);
		makeMove("wP d4-d5", board);
		makeMove("bP c6xd5", board);
		makeMove("wP c4xd5", board);
		makeMove("bB e6-d7", board);
		makeMove("wN f3-d4", board);
		makeMove("bN f7-e5", board);
		makeMove("wP f2-f4", board);
		makeMove("bN e5xd3", board);
		makeMove("wQ d1xd3", board);
		makeMove("bP a6-a5", board);
		makeMove("wP b4xa5", board);
		makeMove("bP g6-g5", board);
		makeMove("wN d4-b3", board);
		makeMove("bP h7-h6", board);
		makeMove("wQ d3-d4", board);
		makeMove("bR a7-a6", board);
		makeMove("wP f4xg5", board);
		makeMove("bP h6xg5", board);
		makeMove("wR f1-f3", board);
		makeMove("bR h8-h7", board);
		makeMove("wQ d4-b4", board);
		makeMove("bK e8-g8", board);
	}
	
	static void test1() {
		Board board = new Board();
		System.out.println(board.toString());
			try {
				makeMove("wP d2-d3", board);
				makeMove("bP a7-a6", board);
				makeMove("wP d3-d4", board);
				makeMove("bP a6-a5", board);
				makeMove("wP d4-d5", board);
				makeMove("bP a5-a4", board);
				makeMove("wP e2-e3", board);
				makeMove("bP a4-a3", board);
				
				makeMove("wP e3-e4", board);
				makeMove("bP a3xb2", board);
				
				makeMove("wN b1-c3", board);
				//makeMove("bP b2xa1 R", board);
				
				Player blackPlayer = new MyPlayer();
				
				int MAX_ROUNDS = 200;
				int MAX_TIME = 1;
				Random random = new Random((new Date()).getTime());
				Move blackMove = blackPlayer.chooseMove(board, Board.BLACK, MAX_TIME, random);
				boolean remis;
				if(blackMove == null)
					remis = true;
				else {
					board.executeMove(blackMove);
					System.out.println(board.toString());
				}
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
	}
	
	static void makeMove(String str, Board board) {
		Move m = Move.Import(str, board);
		board.executeMove(m);
		System.out.println(board.toString());
	}

}
