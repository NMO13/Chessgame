package chess;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {

	@Override
	public double getFitness(Board board, int myColor) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Move chooseMove(Board board, int color, int milliSeconds,
			Random random) {
		List<Move> moves = board.getValidMoves(color);
		int index = random.nextInt(moves.size());
		return moves.get(index);
	}

}
