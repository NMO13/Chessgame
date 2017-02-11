package chess;

public interface Listener {

	void nextMove(MoveEvent m);

	void endState(EndStateEvent endStateEvent);
}
