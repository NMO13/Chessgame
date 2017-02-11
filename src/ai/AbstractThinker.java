package ai;

import java.util.Random;

import chess.Board;
import chess.Move;
import chess.Player;

public abstract class AbstractThinker implements Runnable {
	protected Player player;
	protected Move bestMove = null;
	protected Board board;
	protected int color;
	protected int levels = 1;
	protected int evaluationsCounter = 0;
	protected Random random;
	
	public AbstractThinker(Board board, Player player, int color, Random random) {
		this.board = board;
		this.color = color;
		this.player = player;
		this.random = random;
	}
	
	public Move getBestMove() {
		return bestMove;	
	}
	
	public int getLevelsCompleted() {
		return levels;
	}
	
	public int getNumEvaluationsCompleted() {
		return evaluationsCounter;
	}

}
