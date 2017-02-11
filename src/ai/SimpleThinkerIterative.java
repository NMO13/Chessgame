package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chess.Board;
import chess.Move;
import chess.Player;

public class SimpleThinkerIterative extends AbstractThinker {

	public SimpleThinkerIterative(Board board, Player player, int color,
			Random random) {
		super(board, player, color, random);
	}

	@Override
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
		List<Board> fringe = new ArrayList<Board>();
		fringe.add(board);
		board.level = level;
		double min = Double.MAX_VALUE;
		while(fringe.size() > 0 && !(Thread.currentThread().isInterrupted())) {
			Board b = fringe.remove(0);
			evaluationsCounter++;
			List<Move> moves = b.getValidMoves(myColor);
			if(b.level > 0) {
				for(int i = 0; i < moves.size() && !(Thread.currentThread().isInterrupted()); i++) {
					Board tmpBoard = b.CloneIncompletely();
					tmpBoard.level = b.level - 1;
					tmpBoard.setBoard(moves.get(i));
					fringe.add(tmpBoard);
				}
			}
			else {
				double myFitness = player.getFitness(b, myColor);
				double opponentFitness = player.getFitness(board, Board.FlipColor(myColor));
				double fitness = myFitness - opponentFitness;
				if(fitness < min)
					min = fitness;
			}
			myColor = Board.FlipColor(myColor);
		}
		return 0;
	}
}
