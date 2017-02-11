package chess;

import java.util.ArrayList;
import java.util.List;

public class Board {

	protected ArrayList<Listener> listeners = new ArrayList<Listener>();
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final short EMPTY = 0;
	
	private short[][] figures;
	private History history = new History();
	
	// save current king position
	private Position whiteKingPos = new Position(4, 0, (short)(Figure.KING+Figure.WHITE_OFFSET));
	private Position blackKingPos = new Position(4, 7, (short)(Figure.KING+Figure.BLACK_OFFSET));
	
	public void addListener(Listener ml) {
		listeners.add(ml);
	}
	public Board(short[][] figures) {
		this.figures = figures;
	}
	
	public Board() {
		figures = new short[8][8];
		Reset();
	}
	
	public void Reset() {
		figures[0][0] = Figure.WHITE_OFFSET + Figure.ROOK;
		figures[1][0] = Figure.WHITE_OFFSET + Figure.KNIGHT;
		figures[2][0] = Figure.WHITE_OFFSET + Figure.BISHOP;
		figures[3][0] = Figure.WHITE_OFFSET + Figure.QUEEN;
		figures[4][0] = Figure.WHITE_OFFSET + Figure.KING;
		figures[5][0] = Figure.WHITE_OFFSET + Figure.BISHOP;
		figures[6][0] = Figure.WHITE_OFFSET + Figure.KNIGHT;
		figures[7][0] = Figure.WHITE_OFFSET + Figure.ROOK;
		
		for(int i = 0; i < 8; i++)
			figures[i][1] = Figure.WHITE_OFFSET + Figure.PAWN;

		figures[0][7] = Figure.BLACK_OFFSET + Figure.ROOK;
		figures[1][7] = Figure.BLACK_OFFSET + Figure.KNIGHT;
		figures[2][7] = Figure.BLACK_OFFSET + Figure.BISHOP;
		figures[3][7] = Figure.BLACK_OFFSET + Figure.QUEEN;
		figures[4][7] = Figure.BLACK_OFFSET + Figure.KING;
		figures[5][7] = Figure.BLACK_OFFSET + Figure.BISHOP;
		figures[6][7] = Figure.BLACK_OFFSET + Figure.KNIGHT;
		figures[7][7] = Figure.BLACK_OFFSET + Figure.ROOK;
		
		for(int i = 0; i < 8; i++)
			figures[i][6] = Figure.BLACK_OFFSET + Figure.PAWN;

	}
	
	public void setBoard(Move move) {
		history.prevMove = move;
		int figType = move.getType();
		int color = move.getColor();
		int srcCol = move.getSourceCol();
		int destCol = move.getDestCol();
		int srcRow = move.getSourceRow();
		int destRow = move.getDestRow();
		int newFigureType = move.getNewFigureType();
		boolean isKingSideCastling = move.getIsKingSideCastling();
		boolean isQueenSideCastling = move.getisQueenSideCastling();
		
		if((srcCol == 0 && srcRow == 0) || (destCol == 0 && destRow == 0))
			history.LWhiteRookMovedOrCaptured = true;
		else if((srcCol == 7 && srcRow == 0) || (destCol == 7 && destRow == 0))
			history.RWhiteRookMovedOrCaptured = true;
		if((srcCol == 0 && srcRow == 7) || (destCol == 0 && destRow == 7))
			history.RBlackRookMovedOrCaptured = true;
		else if((srcCol == 7 && srcRow == 7) || (destCol == 7 && destRow == 7))
			history.LBlackRookMovedOrCaptured = true;
		
		if(color == WHITE) {
			if(move.isHit()) {
				if(figures[destCol][destRow] == EMPTY) { // en passant
					assert(figType == Figure.PAWN);
					assert(figures[destCol][destRow-1] == Figure.PAWN + Figure.BLACK_OFFSET);
					setFigure(destCol, destRow, (short)(Figure.PAWN + Figure.WHITE_OFFSET));
					setFigure(destCol, destRow-1, EMPTY);
				}
				else{
					if(destRow == 7 && figType == Figure.PAWN) { // pawn promotion
						assert(figures[destCol][destRow] != EMPTY);
						setFigure(destCol, destRow, (short) (newFigureType + Figure.WHITE_OFFSET));
					}
					else { // "normal" hit
						setFigure(destCol, destRow, (short) (figType + Figure.WHITE_OFFSET));
					}
				}
				figures[srcCol][srcRow] = EMPTY;
			}
			else { // no hit
				// castling
				if(isKingSideCastling) {
					setFigure(6, 0, (short)(Figure.KING + Figure.WHITE_OFFSET));
					setFigure(4, 0, EMPTY);
					setFigure(5, 0, (short)(Figure.ROOK + Figure.WHITE_OFFSET));
					setFigure(7, 0, EMPTY);
					history.WhiteKingMoved = true;
					history.RWhiteRookMovedOrCaptured = true;
				}
				else if(isQueenSideCastling) {
					setFigure(2, 0, (short)(Figure.KING + Figure.WHITE_OFFSET));
					setFigure(4, 0, EMPTY);
					setFigure(3, 0, (short)(Figure.ROOK + Figure.WHITE_OFFSET));
					setFigure(0, 0, EMPTY);
					history.WhiteKingMoved = true;
					history.LWhiteRookMovedOrCaptured = true;
				}
				else {
					if(destRow == 7 && figType == Figure.PAWN) { // pawn promotion
						assert(figures[destCol][destRow] == EMPTY);
						setFigure(destCol, destRow, (short) (newFigureType + Figure.WHITE_OFFSET));
					}
					else
						setFigure(destCol, destRow, (short) (figType + Figure.WHITE_OFFSET));
					
					setFigure(srcCol, srcRow, EMPTY);
				}
			}
			if(figType == Figure.KING) {
				whiteKingPos.Col = destCol;
				whiteKingPos.Row = destRow;
				history.WhiteKingMoved = true;
			}
		}
		else { //color == BLACK
			if(move.isHit()) {
				if(figures[destCol][destRow] == EMPTY) { // en passant
					assert(figType == Figure.PAWN);
					assert(figures[destCol][destRow+1] == Figure.PAWN + Figure.WHITE_OFFSET);
					setFigure(destCol, destRow, (short)(Figure.PAWN + Figure.BLACK_OFFSET));
					setFigure(destCol, destRow+1, EMPTY);
				}
				else{
					if(destRow == 0 && figType == Figure.PAWN) { // pawn promotion
						assert(figures[destCol][destRow] != EMPTY);
						setFigure(destCol, destRow, (short) (newFigureType + Figure.BLACK_OFFSET));
					}
					else { // "normal" hit
						setFigure(destCol, destRow, (short) (figType + Figure.BLACK_OFFSET));
					}
				}
				figures[srcCol][srcRow] = EMPTY;
			}
			else { // no hit
				if(isKingSideCastling) {
					setFigure(6, 7, (short)(Figure.KING + Figure.BLACK_OFFSET));
					setFigure(4, 7, EMPTY);
					setFigure(5, 7, (short)(Figure.ROOK + Figure.BLACK_OFFSET));
					setFigure(7, 7, EMPTY);
					history.BlackKingMoved = true;
					history.LBlackRookMovedOrCaptured = true;
				}
				else if(isQueenSideCastling) {
					setFigure(2, 7, (short)(Figure.KING + Figure.BLACK_OFFSET));
					setFigure(4, 7, EMPTY);
					setFigure(3, 7, (short)(Figure.ROOK + Figure.BLACK_OFFSET));
					setFigure(0, 7, EMPTY);
					history.BlackKingMoved = true;
					history.RBlackRookMovedOrCaptured = true;
				}
				else {
					if(destRow == 0 && figType == Figure.PAWN) { // pawn promotion
						assert(figures[destCol][destRow] == EMPTY);
						setFigure(destCol, destRow, (short) (newFigureType + Figure.BLACK_OFFSET));
					}
					else
						setFigure(destCol, destRow, (short) (figType + Figure.BLACK_OFFSET));
					
					setFigure(srcCol, srcRow, EMPTY);
				}
			}
			if(figType == Figure.KING) {
				blackKingPos.Col = destCol;
				blackKingPos.Row = destRow;
				history.BlackKingMoved = true;
			}
		}
	}
	
	public void executeMove(Move move) {
		if(move == null)
			throw new IllegalArgumentException("Invalid move");
		setBoard(move);
		notifyListeners(move);
	}

	public List<Move> getValidMoves(int color) {
		ArrayList<Move> moves = new ArrayList<Move>();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				short figureIndex = figures[i][j];
				int figureColor = Figure.getColor(figureIndex);
				if(figureIndex != Board.EMPTY && figureColor == color) {
					List<Move> ml = Figure.getValidMoves(this, i, j);
					for(Move m : ml) {
						Board tmpBoard = this.CloneIncompletely();
						tmpBoard.setBoard(m);
						if(!tmpBoard.isInCheck(figureColor))
							moves.add(m);
					}
				}
			}
		}
		
		return moves;
	}
	
	public short[][] getFigures() {
		return figures;
	}
	
	public void setFigure(int col, int row, short figure) {
		figures[col][row] = figure;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("  |=======================================|\n");
		
		for(int i = 7; i >= 0; i--) {
			b.append(i+1);
			b.append(" ");
			b.append("|");
			for(int j = 0; j <= 7; j++) {
				b.append(" " + Figure.toString(figures[j][i]) + " ");
				b.append("|");
			}
			b.append("\n  |=======================================|\n");
		}
		b.append("     a    b    c    d    e    f    g    h ");
		return b.toString();
	}
	
	public Board CloneIncompletely() {
		short[][] figs = new short[8][8];
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				figs[i][j] = figures[i][j];
		Board board = new Board(figs);
		board.history = history.clone();
		board.whiteKingPos = this.whiteKingPos.clone();
		board.blackKingPos = this.blackKingPos.clone();
		return board;
	}
	
	public History getHistory() {
		return history;
	}
	
	public static int colStringToInt(String col) {
		switch(col) {
			case "a": return 0;
			case "b": return 1;
			case "c": return 2;
			case "d": return 3;
			case "e": return 4;
			case "f": return 5;
			case "g": return 6;
			case "h": return 7;
		}
		return -1;
	}
	
	public static String ColumnName(int index) {
		switch(index) {
			case 0: return "a";
			case 1: return "b";
			case 2: return "c";
			case 3: return "d";
			case 4: return "e";
			case 5: return "f";
			case 6: return "g";
			case 7: return "h";
			default: throw new IllegalArgumentException();
		}
	}

	// Checks whether the color player is in check
	public boolean isInCheck(int color) {
		ArrayList<Position> p = new ArrayList<Position>();
		if(color == WHITE)
			return Figure.isFieldAttackable(this, whiteKingPos.Col, whiteKingPos.Row, FlipColor(color), p);
		else
			return Figure.isFieldAttackable(this, blackKingPos.Col, blackKingPos.Row, FlipColor(color), p);
	}
	
	public boolean isInCheck(int color, ArrayList<Position> attackers) {
		if(color == WHITE)
			return Figure.isFieldAttackable(this, whiteKingPos.Col, whiteKingPos.Row, FlipColor(color), attackers);
		else
			return Figure.isFieldAttackable(this, blackKingPos.Col, blackKingPos.Row, FlipColor(color), attackers);
	}
	
	public static int FlipColor(int color) {
		if(color == WHITE)
			return BLACK;
		else
			return WHITE;
	}

	public boolean isMat(int color) {
		ArrayList<Position> attackers  = new ArrayList<Position>();
		Position king = color == WHITE ? whiteKingPos : blackKingPos;
			if(isInCheck(color, attackers)) { // if king is in check
				if(Figure.KingCanMove(king.Col, king.Row, color, this)) // if king can move then clearly no checkmate
					return false;
				if(attackers.size() == 1 && Figure.isBlockable(attackers.get(0), this, king)) // if attacker is blockable then clearly no checkmate
					return false;
				ArrayList<Position> counter_attackers  = new ArrayList<Position>();
				if(attackers.size() == 1 && Figure.isFieldAttackable(this, attackers.get(0).Col,attackers.get(0).Row, 
						color, counter_attackers)) { // if the attacker is capturable then decide..
					
					if(counter_attackers.size() > 1) // if there are more than one counter attackers available then clarly no checkmate
						return false;
					if(counter_attackers.size() == 1 && !Figure.isAttackerKing(counter_attackers)) // if there is exactly one counter attacker and it is not the king then
						return false;																// clearly no checkmate
					if(counter_attackers.size() == 1 && Figure.isAttackerKing(counter_attackers)) { // if the king is the only figure which can capture the attacker
						if(!Figure.isFieldAttackable(this, attackers.get(0).Col, attackers.get(0).Row, Board.FlipColor(color), counter_attackers)) { // and if king is after capturing not in check again
							return false;																											// then no checkmate
						}
					}
				}
									
				return true;
			}
			return false;
	}
	
	public static String colorToString(int color) {
		return color == WHITE ? "White" : "Black";
	}
	
	protected void notifyListeners(Move move) {
		for(Listener l : listeners)
			l.nextMove(new MoveEvent(move));
	}
	
	protected void notifyListenersMat(int color) {
		for(Listener l : listeners)
			l.endState(new EndStateEvent(true, false, color));
	}
	
	public void notifyListenersRemis() {
		for(Listener l : listeners)
			l.endState(new EndStateEvent(false, true, -1));
	}
	
	public int level = 0;
}
