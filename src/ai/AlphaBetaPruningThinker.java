package ai;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import chess.Board;
import chess.Move;
import chess.Player;

public class AlphaBetaPruningThinker extends AbstractThinker {
	
	public AlphaBetaPruningThinker(Board board, Player player, int color, Random random) {
		super(board, player, color, random);
	}
	
	public void run() {
		levels = 1;
		while(!(Thread.currentThread().isInterrupted())) {
			int minIndex = 0;
			List<Move> moves = board.getValidMoves(color);
			int n = moves.size();
			double[] qualities = new double[n];
			for(int i = 0; i < n  && !(Thread.currentThread().isInterrupted()); i++) {
				Board tmpBoard = board.CloneIncompletely();
				tmpBoard.setBoard(moves.get(i));
				double q = evaluateBoard(tmpBoard, color, levels-1, -Double.MAX_VALUE, Double.MAX_VALUE);
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
	
	private double evaluateBoard(Board board, int myColor, int level, double alpha, double beta) {
		evaluationsCounter++;
		int oppColor = Board.FlipColor(myColor);
		List<Move> moves = board.getValidMoves(oppColor);
		if(myColor == Board.BLACK) {
			if(level == 0) {
				double myFitness = player.getFitness(board, myColor);
				double opponentFitness = player.getFitness(board, oppColor);
				return opponentFitness - myFitness;
			}
			else {
				for(int i = 0; i < moves.size() && !(Thread.currentThread().isInterrupted()); i++) {
					Board tmpBoard = board.CloneIncompletely();
					tmpBoard.setBoard(moves.get(i));
					double fitness = evaluateBoard(tmpBoard, oppColor, level-1, alpha, beta);
//					if(fitness >= beta)
//						return beta;
					if(fitness > alpha)
						alpha = fitness;
 					
				}
				return alpha;
			}
		}
		else {
			if(level == 0) {
				double myFitness = player.getFitness(board, myColor);
				double opponentFitness = player.getFitness(board, oppColor);
				return myFitness - opponentFitness;
			}
			else {
				for(int i = 0; i < moves.size() && !(Thread.currentThread().isInterrupted()); i++) {
					Board tmpBoard = board.CloneIncompletely();
					tmpBoard.setBoard(moves.get(i));
					double fitness = evaluateBoard(tmpBoard, oppColor, level-1, alpha, beta);
//					if(fitness <= alpha)
//						return alpha;
					if(fitness < beta)
						beta = fitness;
					
				}
				return beta;
			}
		}
	}
}
