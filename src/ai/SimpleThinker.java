package ai;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import chess.Board;
import chess.Move;
import chess.Player;

public class SimpleThinker extends AbstractThinker {
	
	public SimpleThinker(Board board, Player player, int color, Random random) {
		super(board, player, color, random);
	}
	
	public void run() {
		levels = 1;
		while(!(Thread.currentThread().isInterrupted())) {
			int minIndex = 0;
			List<Move> moves = board.getValidMoves(color);
			int n = moves.size();
			double[] qualities = new double[n];
			for(int i = 0; i < n && !(Thread.currentThread().isInterrupted()); i++) {
				Board tmpBoard = board.CloneIncompletely();
				tmpBoard.setBoard(moves.get(i));
				double q = evaluateBoard(tmpBoard,  Board.FlipColor(color), levels-1);
				qualities[i] = q;
				if(q < qualities[minIndex])
					minIndex = i;
			}
			
			if(Thread.currentThread().isInterrupted()) {
				return;
			}
			for(int i = 0; i < moves.size(); i++) {
				System.out.println(moves.get(i) + " " + qualities[i]);
			}
			bestMove = moves.get(minIndex);
			System.out.println("Best move: " + bestMove);
			System.out.println("Level: " + levels);
			System.out.println("Evals: " + evaluationsCounter);
			levels++;
		}
	}
	
	private double evaluateBoard(Board board, int myColor, int level) {
		evaluationsCounter++;
		int oppColor = Board.FlipColor(myColor);
		if(level == 0) {
			double myFitness = player.getFitness(board, myColor);
			double opponentFitness = player.getFitness(board, oppColor);
			return myFitness - opponentFitness;
		}
		else {
			List<Move> moves = board.getValidMoves(myColor);
			double min = Double.MAX_VALUE;
			for(int i = 0; i < moves.size() && !(Thread.currentThread().isInterrupted()); i++) {
				 Board tmpBoard = board.CloneIncompletely();
				 tmpBoard.setBoard(moves.get(i));
				 double fitness = evaluateBoard(tmpBoard, oppColor, level-1);
				 if(fitness<min)
					 min = fitness;
			}
			return -min;
		}
	}
}
