package chess;

public class Position {
	public Position(int col, int row) {
		Col = col;
		Row = row;
	}
	public Position(int col, int row, short figureIndex) {
		this(col, row);
		FigureIndex = figureIndex;
	}
	public int Col;
	public int Row;
	
	public short FigureIndex = -1;
	
	public Position clone() {
		return new Position(Col, Row);
	}
}
