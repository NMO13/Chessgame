package chess;

import java.util.Random;

public class HumanPlayer implements Player {

	@Override
	public double getFitness(Board board, int myColor) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Move chooseMove(Board board, int color, int milliSeconds,
			Random random) {
		System.out.print("Choose move: ");
		String str = Keyboard.readString();
        System.out.println(str);      
		Move m = Move.Import(str, board);
		return m;
	}

}
