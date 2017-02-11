package chess;

import java.util.List;

public class Move {
	private int color;
	private int type;
	private int sourceCol;
	private int sourceRow;
	private int destCol;
	private int destRow;
	private int newFigureType;
	private boolean isHit;
	private boolean isKingSideCastling;
	private boolean isQueenSideCastling;
	
	public Move(int color, int type, int sourceCol, int sourceRow, 
			int destCol, int destRow, int newFigureType, boolean isHit) {
		this.color = color;
		this.type = type;
		this.sourceCol = sourceCol;
		this.sourceRow = sourceRow;
		this.destCol = destCol;
		this.destRow = destRow;
		this.isHit = isHit;
		this.isKingSideCastling = false;
		this.isQueenSideCastling = false;
		this.newFigureType = newFigureType;
	}
	
	public Move(int color, boolean isKingSideCastling, boolean isQueenSideCastling) {
		assert((isKingSideCastling ^ isQueenSideCastling) == true);
		this.color = color;
		this.isKingSideCastling = isKingSideCastling;
		this.isQueenSideCastling = isQueenSideCastling;
		this.isHit = false;
		this.type = Figure.KING;
		this.newFigureType = -1;
		
		if(color == Board.WHITE) {
			sourceCol = 4;
			sourceRow = 0;
			if(isKingSideCastling) {
				destCol = 6;
				sourceRow = 0;
			}
			else {
				destCol = 2;
				destRow = 0;
			}
		}
		else {
			sourceCol = 4;
			sourceRow = 7;
			if(isKingSideCastling) {
				destCol = 6;
				destRow = 7;
			}
			else {
				destCol = 2;
				destRow = 7;
			}			
		}
	}
	
	public int getSourceCol() {
		return sourceCol;
	}

	public int getColor() {
		return color;
	}

	public int getType() {
		return type;
	}

	public int getSourceRow() {
		return sourceRow;
	}

	public int getDestCol() {
		return destCol;
	}

	public int getDestRow() {
		return destRow;
	}

	public int getNewFigureType() {
		return newFigureType;
	}

	public boolean isHit() {
		return isHit;
	}

	public String toString() {
		String strIsHit = isHit == true ? "x" : "-";
		String promotion = (newFigureType != -1) ? " " + Figure.toString(newFigureType).charAt(1) : "";
	    return Figure.toString(color, type) + " " + Board.ColumnName(sourceCol) + (sourceRow+1) + strIsHit
	    		+ Board.ColumnName(destCol) + (destRow+1) + promotion;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null)
	            return false;
		if (obj == this)
	            return true;
    	if (!(obj instanceof Move))
	            return false;
    	
    	Move m = (Move) obj;
    	if(color == m.color && type == m.type && sourceCol == m.sourceCol && sourceRow == m.sourceRow && 
			destCol == m.destCol && destRow == m.destRow && newFigureType == m.newFigureType && isHit == m.isHit)
    		return true;
    	return false;
	}
	
	public static Move Import(String str, Board board) throws IllegalArgumentException {
		Move m = parseString(str, board);
		List<Move> list = board.getValidMoves(m.getColor());
		if(MoveListIncludesMove(list, m))
			return m;
		return null;
	}
	
	protected static Move parseString(String str, Board board) {
		String expr = "[bw][PKRNQB][ ][a-h][1-8][-x][a-h][1-8]([ ](Q|N|R|B))?";
		if(!str.matches(expr))
			throw new IllegalArgumentException(str + " is not a valid input");
		String[] tokens = str.split("(?<!^)");
		int color = tokens[0].equals("b") ? Board.BLACK : Board.WHITE;
		short type = Figure.fromString(tokens[1]);
		int srcCol = Board.colStringToInt(tokens[3]);
		int srcRow = Integer.parseInt(tokens[4]) - 1;
		boolean isHit = tokens[5].equals("-") ? false : true;
		int destCol = Board.colStringToInt(tokens[6]);
		int destRow = Integer.parseInt(tokens[7]) - 1;
		
		Move m = null;
		m = CreateCastlingMove(srcCol, srcRow, destCol, destRow, type, color);
		if(m == null) {
			short newType = tokens.length == 8 ? -1 : Figure.fromString(tokens[9]);
			m = new Move(color, type, srcCol, srcRow, destCol, destRow, newType, isHit);
		}
		
		return m;
	}

	public boolean getIsKingSideCastling() {
		return isKingSideCastling;
	}

	public boolean getisQueenSideCastling() {
		return isQueenSideCastling;
	}

	public static boolean MoveListIncludesMove(List<Move> validMoves, Move m) {
		for(Move validMove : validMoves) {
			if(m.equals(validMove))
				return true;
		}
		return false;
	}

	public static Move CreateCastlingMove(int srcCol, int srcRow,
			int destCol, int destRow, int type, int color) {
		int moveCount = srcCol - destCol;
		if(type == Figure.KING && Math.abs(moveCount) == 2) {
			if(srcRow != destRow || srcRow != 0 && srcRow != 7)
				return null;
			if(moveCount > 0)
				return new Move(color, false, true);
			else
				return new Move(color, true, false);
		}
		return null;
	}
}
