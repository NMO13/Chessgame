package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chess.Board;
import chess.Move;
import chess.Player;

public class NegaAlphaBetaThinker extends AbstractThinker {
	
	public NegaAlphaBetaThinker(Board board, Player player, int color, Random random) {
		super(board, player, color, random);
	}
	
	public void run() {
		levels = 1;
		ArrayList<Integer> bestMoveIndizes = new ArrayList<Integer>();
		while(!(Thread.currentThread().isInterrupted())) {
			int minIndex = 0;
			List<Move> moves = board.getValidMoves(color);
			int n = moves.size();
			double[] qualities = new double[n];
			for(int i = 0; i < n && !(Thread.currentThread().isInterrupted()); i++) {
				Board tmpBoard = board.CloneIncompletely();
				tmpBoard.setBoard(moves.get(i));
				double q = evaluateBoard(tmpBoard,  Board.FlipColor(color), levels-1, -Double.MAX_VALUE, Double.MAX_VALUE);
				qualities[i] = q;
				if(q < qualities[minIndex]) {
					minIndex = i;
					bestMoveIndizes.clear();
					bestMoveIndizes.add(i);
				}
				else if(q == qualities[minIndex]) {
					bestMoveIndizes.add(i);
				}
				
			}
			if(Thread.currentThread().isInterrupted()) {
				return;
			}
//			for(int i = 0; i < moves.size(); i++) {
//				System.out.println(moves.get(i) + " " + qualities[i]);
//			}
			
			bestMove = moves.get(bestMoveIndizes.get(random.nextInt(bestMoveIndizes.size())));
			bestMoveIndizes.clear();
//			System.out.println("Best move: " + bestMove);
//			System.out.println("Level: " + levels);
//			System.out.println("Evals: " + evaluationsCounter);
			levels++;
		}
	}
	
	private double evaluateBoard(Board board, int myColor, int level, double alpha, double beta) {
		evaluationsCounter++;
		int oppColor = Board.FlipColor(myColor);
		if(level == 0 && myColor == Board.BLACK) {
			double myFitness = player.getFitness(board, myColor);
			double opponentFitness = player.getFitness(board, oppColor);
			return myFitness - opponentFitness;
		}
		else {
			if(level == 0) { // horizon effect
				level = 1;
			}
			List<Move> moves = board.getValidMoves(myColor);
			double max = alpha;
			for(int i = 0; i < moves.size() && !(Thread.currentThread().isInterrupted()); i++) {
				 Board tmpBoard = board.CloneIncompletely();
				 tmpBoard.setBoard(moves.get(i));
				 double fitness = -evaluateBoard(tmpBoard, oppColor, level-1, -beta, -max);
				 if(fitness > max) {
					 max = fitness;
					 if(max >= beta)
						 return max;
					 
				 }
			}
			return max;
		}
	}
}
