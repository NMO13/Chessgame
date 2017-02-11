package chess;

import gui.HumanPlayerGUI;
import gui.HumanPlayerGUIAppWindow;

import java.util.Date;
import java.util.Random;

public class Game {

	public static void main(String[] args) {
			Board board = new Board();
			HumanPlayerGUIAppWindow window = new HumanPlayerGUIAppWindow(board, Board.WHITE);
			Player whitePlayer = new HumanPlayerGUI(window);
			Player blackPlayer = new MyPlayer();
			Random random = new Random((new Date()).getTime());
			
			board.addListener(window);
////			... = new RandomPlayer();
//			
			boolean isMat = false;
			boolean remis = false;
			
			int round = 1;
			int MAX_ROUNDS = 200;
			int MAX_TIME = 2;
			// maybe a bug: if computation time is really really short then bestMove in thinker possibly doesn't get a value
			// and stays null
			
			while(!isMat && !remis && round < MAX_ROUNDS) {
				Move whiteMove = whitePlayer.chooseMove(board, Board.WHITE, MAX_TIME, random);
				if(whiteMove == null) {
					remis = true;
					board.notifyListenersRemis();
					continue;
				}
				board.executeMove(whiteMove);
				if(board.isMat(Board.BLACK)) {
					board.notifyListenersMat(Board.BLACK);
					isMat = true;
					continue;
				}
				
				Move blackMove = blackPlayer.chooseMove(board, Board.BLACK, MAX_TIME, random);
				if(blackMove == null) {
					if(board.isMat(Board.BLACK)) {
						board.notifyListenersMat(Board.BLACK);
						isMat = true;
					}
					else {
						remis = true;
						board.notifyListenersRemis();
					}
				}
				else {
					board.executeMove(blackMove);
					if(board.isMat(Board.WHITE)) {
						board.notifyListenersMat(Board.WHITE);
						isMat = true;
					}
				}
				round++;
			}
	}
	


}
