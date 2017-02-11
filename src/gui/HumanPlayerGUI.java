package gui;

import java.util.Random;

import chess.Board;
import chess.Move;
import chess.Player;

public class HumanPlayerGUI implements Player {

	HumanPlayerGUIAppWindow window;
	public HumanPlayerGUI(HumanPlayerGUIAppWindow window) {
		//window.setBounds(10, 10, 420, 180);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		this.window = window;
		this.window.pack();
		this.window.setVisible(true);
	}
	
	@Override
	public double getFitness(Board board, int myColor) {
		return 0;
	}

	@Override
	public Move chooseMove(Board board, int color, int milliSeconds,
			Random random) {
		window.move = null;
		window.canMove = true;
		while(window.move == null) {
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				
			}
		}
		window.canMove = false;
		return window.move;
	}

}
