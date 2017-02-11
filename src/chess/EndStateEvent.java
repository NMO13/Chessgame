package chess;

public class EndStateEvent {

	public boolean mat;
	public boolean remis;
	public int color;

	public EndStateEvent(boolean mat, boolean remis, int color) {
		this.mat = mat;
		this.remis = remis;
		this.color = color;
	}

}
