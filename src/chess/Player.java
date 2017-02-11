package chess;

public interface Player {

	double getFitness(Board board, int myColor);
	Move chooseMove(Board board, int color, int milliSeconds, 
			java.util.Random random); 

}
