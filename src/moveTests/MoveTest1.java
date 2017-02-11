package moveTests;

import chess.Board;
import chess.Move;

public class MoveTest1 {

	public static void main(String[] args) {
//		test1();
//		longerGame();
//		test2();
		test3();
	}
	
	private static void test3() {
		Board board = new Board();
		makeMove("wP d2-d4", board);
		makeMove("bP e7-e6", board);
		makeMove("wP e2-e4", board);
		makeMove("bP b7-b6", board);
		makeMove("wB f1-d3", board);
		makeMove("bB c8-a6", board);
		makeMove("wB c1-e3", board);
		makeMove("bB a6xd3", board);
		makeMove("wQ d1xd3", board);
		makeMove("bP g7-g6", board);
		makeMove("wN b1-d2", board);
		makeMove("bB f8-h6", board);
		makeMove("wB e3xh6", board);
		makeMove("bN g8xh6", board);
		makeMove("wQ d3-h3", board);
		makeMove("bN h6-g8", board);
		makeMove("wK e1-c1", board);
		makeMove("bN b8-c6", board);
		makeMove("wN g1-f3", board);
		makeMove("bN c6-b4", board);
		makeMove("wP a2-a3", board);
		makeMove("bN b4-a2", board);
		makeMove("wK c1-b1", board);
		makeMove("bN a2-b4", board);
		makeMove("wP a3xb4", board);
		makeMove("bN g8-f6", board);
		makeMove("wP e4-e5", board);
		makeMove("bN f6-d5", board);
		makeMove("wN d2-e4", board);
		makeMove("bP f7-f5", board);
		makeMove("wP e5xf6", board);
	}

	public static void test2() {
		Board board = new Board();
		makeMove("wP d2-d4", board);
		makeMove("bP f7-f6", board);
		makeMove("wP e2-e4", board);
		makeMove("bP g7-g5", board);
		makeMove("wQ d1-h5", board);
		makeMove("bP a7-a6", board);
	}
	
	public static void test1() {
		Board board = new Board();
		System.out.println(board.toString());		
			// Queen test
			try {
				
				makeMove("bP a7-a5", board);
				makeMove("wP e2-e4", board);
				makeMove("wQ d1-h5", board);
				makeMove("wQ h5xf7", board);
				makeMove("wQ f7xe7", board);
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			
			// castling black queensite
			try {
				board = new Board();
				makeMove("bP a7-a5", board);
				makeMove("bP b7-b5", board);
				makeMove("bP c7-c5", board);
				makeMove("bN b8-c6", board);
				makeMove("bB c8-b7", board);
				makeMove("bQ d8-c7", board);
				makeMove("bK e8-c8", board);
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			
			// castling black kingsite
			try {
				board = new Board();
				makeMove("bP f7-f5", board);
				makeMove("bP g7-g5", board);
				makeMove("bN g8-h6", board);
				makeMove("bB f8-g7", board);
				makeMove("bK e8-g8", board);
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			
			// en passant
			try {
				board = new Board();
				makeMove("wP e2-e4", board);
				makeMove("wP e4-e5", board);
				makeMove("bP f7-f5", board);
				makeMove("wP e5xf6", board);
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			
			// is in check
			try {
				makeMove("bP e7xf6", board);
				makeMove("bQ d8-e7", board);
				assert(board.isInCheck(Board.BLACK) == false);
				assert(board.isInCheck(Board.WHITE) == true);
				makeMove("wQ d1-h5", board);
				assert(board.isInCheck(Board.BLACK) == true);
				makeMove("bP g7-g6", board);
				assert(board.isInCheck(Board.BLACK) == false);
				makeMove("wQ h5xg6", board);
				assert(board.isInCheck(Board.BLACK) == true);
				makeMove("bN g8-h6", board);
				makeMove("bB f8-g7", board);
				makeMove("wQ g6xg7", board);
				makeMove("bK e8-g8", board);
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
	}
	
	
	public static void matTests() {
		Board board;
		
		//is not mat
		try {
			board = new Board();
			makeMove("bP f7-f6", board);
			makeMove("wP e2-e3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wQ d1-h5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
		//is mat
		try {
			board = new Board();
			makeMove("wP f2-f4", board);
			makeMove("bP e7-e6", board);
			makeMove("wP g2-g4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ d8-h4", board);
			assert(board.isMat(Board.WHITE) == true);
			assert(board.isMat(Board.BLACK) == false);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
		//is not mat
		try {
			board = new Board();
			makeMove("wP f2-f4", board);
			makeMove("bP e7-e6", board);
			makeMove("wP g2-g3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ d8-h4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void longerGame() {
		try {
			Board board = new Board();
			makeMove("wP e2-e4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP c7-c6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP d2-d4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP d7-d5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN b1-c3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP g7-g6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN g1-f3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bB f8-g7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP e4-e5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bB c8-d7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wQ d1-e2", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP e7-e6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP b2-b4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ d8-b6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP a2-a3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bN g8-e7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP g2-g4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ b6-c7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wR a1-b1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP h7-h5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP h2-h3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP h5xg4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP h3xg4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bR h8xh1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN f3-g5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ c7-b6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wQ e2-f3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bR h1-h8", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN g5xf7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bR h8-g8", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN f7-d6", board);
			assert(board.isMat(Board.WHITE) == false);
//			assert(board.isMat(Board.BLACK) == false);
			makeMove("bK e8-d8", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wB c1-e3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bB g7xe5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP d4xe5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP d5-d4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN c3-a4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ b6-c7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wB e3xd4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bN b8-a6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wQ f3-f7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bN a6-b8", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wP b4-b5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP c6xb5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wR b1xb5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bB d7xb5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN d6xb5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wQ f7xe6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ c7-a5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wQ e6-d7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bN b8xd7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wB d4-b6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bP a7xb6", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("wN b5-c7", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			makeMove("bQ a5-b4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN a4-c3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == false);
			makeMove("bR a8xa3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == false);
			makeMove("bR a3-a1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ b4-b1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ b1-d1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ d1-c1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ c1-d1", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN c3-e4", board);
			assert(board.isMat(Board.WHITE) == true);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ d1-d3", board);
			assert(board.isMat(Board.WHITE) == true);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ d3-e3", board);
			assert(board.isMat(Board.WHITE) == true);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wP f2-f4", board);
			assert(board.isMat(Board.WHITE) == true);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("bQ e3-g3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN e4-c3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN c3-e2", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN c7-b5", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN b5-d4", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN d4-f3", board);
			assert(board.isMat(Board.WHITE) == false);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
			makeMove("wN f3-d2", board);
			assert(board.isMat(Board.WHITE) == true);
			assert(board.isMat(Board.BLACK) == false);
			assert(board.isInCheck(Board.WHITE) == true);
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
