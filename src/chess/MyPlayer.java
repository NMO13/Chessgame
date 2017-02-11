package chess;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ai.*;


public class MyPlayer implements Player {

	public double getFitness(Board board, int color) {
		double fitness = 0;
		for(int row = 0; row < 8; row++)
			for(int col = 0; col < 8; col++) {
				short figureIndex = board.getFigures()[col][row];
				if(figureIndex > 0 && Figure.getColor(figureIndex) == color) {
					int type = Figure.getType(figureIndex);
					if(type == Figure.PAWN) {
						if(Figure.IsPartOfPawnChain(board, color, col, row))
							fitness += 0.4;
						if(Figure.IsPawnPartOfDoublePawn(board, color, col, row))
							fitness -= 0.4;
						fitness += 1;
					}
					if(type == Figure.ROOK) {
						if(Figure.IsConnectedRook(board, color, col, row))
							fitness += 0.3;
						fitness += 4.65;
					}
					else if(type == Figure.BISHOP) {
						fitness += 3.25;
						if(col > 0 && col < 7)
							fitness += 0.2;
						if(row > 0 && row < 7)
							fitness += 0.2;
					}
					else if(type == Figure.KNIGHT) {
						fitness += 2.75;
						if(col > 0 && col < 7)
							fitness += 0.4;
						if(row > 0 && row < 7)
							fitness += 0.4;
					}
					else if(type == Figure.QUEEN) {
						if((col == 3 || col == 4) && (row == 3 || row == 4))
							fitness += 0.3;
						fitness += 9;
					}
					else if(type == Figure.KING) {
						if(color == Board.WHITE) {
							if(row == 0 && (col == 0 || row == 7))
								fitness += 0.4;
						}
						else {
							if(row == 7 && (col == 0 || row == 7))
								fitness += 0.4;
						}
					}
				}
			}
		
		return fitness;
	}
	
	public Move chooseMove(Board board, int color, int milliSecondsLimit, Random random) {
		AbstractThinker thinker = new NegaAlphaBetaThinker(board, this, color, random);
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> task = executor.submit(thinker);
		try {
			task.get(milliSecondsLimit, TimeUnit.SECONDS);
		} 
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			if(e.getCause() instanceof AssertionError || e.getCause() instanceof ArrayIndexOutOfBoundsException)
				e.printStackTrace();
			task.cancel(true);
		}
		System.out.println("Levels: " + thinker.getLevelsCompleted());
		System.out.println("Evaluations: " + thinker.getNumEvaluationsCompleted());
		return thinker.getBestMove();
	}
	
	
}
