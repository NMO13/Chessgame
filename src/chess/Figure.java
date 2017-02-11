package chess;

import java.util.ArrayList;
import java.util.List;

public class Figure {
	public static final short PAWN = 1;
	public static final short ROOK = 2;
	public static final short KNIGHT = 3;
	public static final short BISHOP = 4;
	public static final short QUEEN = 5;
	public static final short KING = 6;

	
	public static final String PAWN_STRING = "P"; 
	public static final String ROOK_STRING = "R";
	public static final String KNIGHT_STRING = "N"; 
	public static final String BISHOP_STRING = "B"; 
	public static final String QUEEN_STRING = "Q"; 
	public static final String KING_STRING = "K"; 

	 
	public static final short WHITE_OFFSET = 0;
	public static final short BLACK_OFFSET = 10;
	
	public static final String WHITE_STRING = "w"; 
	public static final String BLACK_STRING = "b"; 
	
	private static boolean isHit = false;
	public static int getColor(short figureIndex) {
		if(figureIndex < BLACK_OFFSET)
			return Board.WHITE;
		else
			return Board.BLACK;
	}
	
	public static int getType(int figureIndex) {
		if(figureIndex > BLACK_OFFSET)
			return figureIndex - BLACK_OFFSET;
		else
			return figureIndex;
	}
	
	 public static String toString(int figureIndex){
		 if(figureIndex == 0)
			 return "  ";
		 StringBuilder sb = new StringBuilder();
		 if(figureIndex < Figure.BLACK_OFFSET) {
			 sb.append(WHITE_STRING);
		 }
		 else {
			 sb.append(BLACK_STRING);
			 figureIndex -= BLACK_OFFSET;
		 }
		 
		 switch(figureIndex) {
		 	case PAWN: sb.append(PAWN_STRING); break;
		 	case ROOK: sb.append(ROOK_STRING); break;
		 	case BISHOP: sb.append(BISHOP_STRING); break;
		 	case KNIGHT: sb.append(KNIGHT_STRING); break;
		 	case QUEEN: sb.append(QUEEN_STRING); break;
		 	case KING: sb.append(KING_STRING); break;
		 	default:
		 		throw new IllegalArgumentException();
		 }
		 return sb.toString();
	 } 
	 
	 public static String toString(int color, int type) {
		 StringBuilder sb = new StringBuilder();
		 if(color == Board.WHITE)
			 sb.append(WHITE_STRING);
		 else
			 sb.append(BLACK_STRING);
		 
		 switch(type) {
		 	case PAWN: sb.append(PAWN_STRING); break;
		 	case ROOK: sb.append(ROOK_STRING); break;
		 	case BISHOP: sb.append(BISHOP_STRING); break;
		 	case KNIGHT: sb.append(KNIGHT_STRING); break;
		 	case QUEEN: sb.append(QUEEN_STRING); break;
		 	case KING: sb.append(KING_STRING); break;
		 	default:
		 		throw new IllegalArgumentException();
		 }
		 return sb.toString();
			 
	 } 
	 
	 public static short fromString(String str) {
		 int index = 0;
		 if(str.length() == 2) {
			 if(String.valueOf(str.charAt(0)) != WHITE_STRING || String.valueOf(str.charAt(0)) != BLACK_STRING)
				 throw new IllegalArgumentException();
			 index = 1;
		 }
		 else if(str.length() > 2)
			 throw new IllegalArgumentException();
		 
		 switch(String.valueOf(str.charAt(index))) {
		 	case PAWN_STRING: return PAWN;
		 	case ROOK_STRING: return ROOK;
		 	case BISHOP_STRING: return BISHOP;
		 	case KNIGHT_STRING: return KNIGHT;
		 	case QUEEN_STRING: return QUEEN;
		 	case KING_STRING: return KING;
		 	default:
		 		throw new IllegalArgumentException();
		 		
		 	
		 
		 }
	 }
	
	 // Checks whether this field is attacked by color player
	 static boolean isFieldAttackable(Board board, final int col, final int row, int figureColor, ArrayList<Position> attackers) {
		 int startCount = attackers.size();
		 isFieldAttackableForPawns(board, col, row, figureColor, attackers);
		 isReachableExceptPawns(board, col, row, figureColor, attackers);
		 if(attackers.size() != startCount)
			 return true;
		 return false;
	 }
	 
	 static boolean canPawnsReachFieldWithoutAttacking(Board board, final int col, final int row, int figureColor, ArrayList<Position> attackers) {
		 int startCount = attackers.size();
		 if(!isFree(board, col, row))
			 return false;
		 if(figureColor == Board.WHITE) {
			 if(isValidField(col, row-1) && board.getFigures()[col][row-1] == PAWN + WHITE_OFFSET) {
				 attackers.add(new Position(col, row-1, board.getFigures()[col][row-1]));
			 }
			 if(row == 4 && isFree(board, col, 3) && board.getFigures()[col][2] == PAWN + WHITE_OFFSET) {
				 attackers.add(new Position(col, 4, board.getFigures()[col][2])); 
			 }
		 }
		 else {
			 if(isValidField(col, row+1) && board.getFigures()[col][row+1] == PAWN + BLACK_OFFSET) {
				 attackers.add(new Position(col, row+1, board.getFigures()[col][row-1]));
			 }
			 if(row == 5 && isFree(board, col, 6) && board.getFigures()[col][7] == PAWN + BLACK_OFFSET) {
				 attackers.add(new Position(col, 6, board.getFigures()[col][7])); 
			 }
		 }
		 if(startCount != attackers.size())
			 return true;
		 return false;
	 }
	 
	 static boolean isFieldAttackableForPawns(Board board, final int col, final int row, int figureColor, ArrayList<Position> attackers) {
		 int startCount = attackers.size();
		// check adversarial pawns
		 if(figureColor == Board.WHITE) {
			 if(isValidField(col+1, row-1) && board.getFigures()[col+1][row-1] == PAWN + WHITE_OFFSET) {
				 attackers.add(new Position(col+1, row-1, board.getFigures()[col+1][row-1]));
			 }
			 if(isValidField(col-1, row-1) && board.getFigures()[col-1][row-1] == PAWN + WHITE_OFFSET) {
				 attackers.add(new Position(col-1, row-1, board.getFigures()[col-1][row-1]));
			 }
			 if(row == 4) { // en passant
				Move prevMove = board.getHistory().prevMove;
				if(prevMove.getType() == PAWN && prevMove.getColor() == Board.BLACK) {// if there is a black pawn beside this position
					if(prevMove.getSourceRow() == 6 && prevMove.getDestRow() == 4) {
						assert(prevMove.getSourceCol() == prevMove.getDestCol());
						int prevDC = prevMove.getDestCol();
						int prevDR = prevMove.getDestRow();
						if(isValidField(prevDC+1, prevDR) && board.getFigures()[prevMove.getDestCol()+1][prevMove.getDestRow()] == PAWN + WHITE_OFFSET) {
							attackers.add(new Position(prevDC+1, prevMove.getDestRow(), board.getFigures()[prevMove.getDestCol()+1][prevMove.getDestRow()]));
						}
						else if(isValidField(prevDC-1, prevDR) && board.getFigures()[prevDC-1][prevDR] == PAWN + WHITE_OFFSET) {
							attackers.add(new Position(prevDC-1, prevDR, board.getFigures()[prevDC-1][prevDR]));
						}
					}
				}
			}
						
		 }
		 else {
			 if(isValidField(col+1, row+1) && board.getFigures()[col+1][row+1] == PAWN + BLACK_OFFSET) {
				 attackers.add(new Position(col+1, row+1, board.getFigures()[col+1][row+1]));
			 }
			 if(isValidField(col-1, row+1) && board.getFigures()[col-1][row+1] == PAWN + BLACK_OFFSET) {
				 attackers.add(new Position(col-1, row+1, board.getFigures()[col-1][row+1]));
			 }
			 if(row == 3) { // en passant
				Move prevMove = board.getHistory().prevMove;
				if(prevMove.getType() == PAWN && prevMove.getColor() == Board.WHITE) {// if there is a black pawn beside this position
					if(prevMove.getSourceRow() == 1 && prevMove.getDestRow() == 3) {
						assert(prevMove.getSourceCol() == prevMove.getDestCol());
						int prevDC = prevMove.getDestCol();
						int prevDR = prevMove.getDestRow();
						if(isValidField(prevDC+1, prevDR) && board.getFigures()[prevDC+1][prevDR] == PAWN + BLACK_OFFSET) {
							attackers.add(new Position(prevDC+1, prevDR, board.getFigures()[prevDC+1][prevDR]));
						}
						else if(isValidField(prevDC-1, prevDR) && board.getFigures()[prevDC-1][prevDR] == PAWN + BLACK_OFFSET) {
							attackers.add(new Position(prevDC-1, prevDR, board.getFigures()[prevDC-1][prevDR]));
						}
					}
				}
			}
		 }
		 if(attackers.size() != startCount)
			 return true;
		 return false;
	 }
	 
	 static boolean isReachableExceptPawns(Board board, final int col, final int row, int figureColor, ArrayList<Position> attackers) {
		 int startCount = attackers.size();
		 short offset = (figureColor == Board.WHITE) ? WHITE_OFFSET : BLACK_OFFSET; 
		 // check adverarial rook + queen
		 // go up
		 int c = col;
		 int r = row;
		 while(isValidField(c, ++r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == ROOK + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 // go down
		 c = col;
		 r = row;
		 while(isValidField(c, --r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == ROOK + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 // go right
		 c = col;
		 r = row;
		 while(isValidField(++c, r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == ROOK + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 // go left
		 c = col;
		 r = row;
		 while(isValidField(--c, r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == ROOK + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 
		 // check adversarial bishop + queen
		 // go right up
		 c = col;
		 r = row;
		 while(isValidField(++c, ++r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == BISHOP + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 // go right down
		 c = col;
		 r = row;
		 while(isValidField(++c, --r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == BISHOP + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 // go left up
		 c = col;
		 r = row;
		 while(isValidField(--c, ++r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == BISHOP + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		 // go left down
		 c = col;
		 r = row;
		 while(isValidField(--c, --r) && isFree(board, c, r));
		 if(isValidField(c, r) && (board.getFigures()[c][r] == BISHOP + offset || board.getFigures()[c][r] == QUEEN + offset)) {
			 attackers.add(new Position(c, r, board.getFigures()[c][r]));
		 }
		// check adversarial knight
		 c = col;
		 r = row;
		 if(isValidField(c+1, r+2) && board.getFigures()[c+1][r+2] == KNIGHT + offset)
			 attackers.add(new Position(c+1, r+2, board.getFigures()[c+1][r+2]));
		 if(isValidField(c+2, r+1) && board.getFigures()[c+2][r+1] == KNIGHT + offset)
			 attackers.add(new Position(c+2, r+1, board.getFigures()[c+2][r+1]));
		 if(isValidField(c+2, r-1) && board.getFigures()[c+2][r-1] == KNIGHT + offset)
			 attackers.add(new Position(c+2, r-1, board.getFigures()[c+2][r-1]));
		 if(isValidField(c+1, r-2) && board.getFigures()[c+1][r-2] == KNIGHT + offset)
			 attackers.add(new Position(c+1, r-2, board.getFigures()[c+1][r-2]));
		 if(isValidField(c-1, r-2) && board.getFigures()[c-1][r-2] == KNIGHT + offset)
			 attackers.add(new Position(c-1, r-2, board.getFigures()[c-1][r-2]));
		 if(isValidField(c-2, r-1) && board.getFigures()[c-2][r-1] == KNIGHT + offset)
			 attackers.add(new Position(c-2, r-1, board.getFigures()[c-2][r-1]));
		 if(isValidField(c-1, r+2) && board.getFigures()[c-1][r+2] == KNIGHT + offset)
			 attackers.add(new Position(c-1, r+2, board.getFigures()[c-1][r+2]));
		 if(isValidField(c-2, r+1) && board.getFigures()[c-2][r+1] == KNIGHT + offset)
			 attackers.add(new Position(c-2, r+1, board.getFigures()[c-2][r+1]));
		 // check adversarial king
		 c = col;
		 r = row;
		 if(isValidField(c+1, r) && board.getFigures()[c+1][r] == KING + offset)
			 attackers.add(new Position(c+1, r, board.getFigures()[c+1][r]));
		 if(isValidField(c+1, r+1) && board.getFigures()[c+1][r+1] == KING + offset)
			 attackers.add(new Position(c+1, r+1, board.getFigures()[c+1][r+1]));
		 if(isValidField(c, r+1) && board.getFigures()[c][r+1] == KING + offset)
			 attackers.add(new Position(c, r+1, board.getFigures()[c][r+1]));
		 if(isValidField(c+1, r-1) && board.getFigures()[c+1][r-1] == KING + offset)
			 attackers.add(new Position(c+1, r-1, board.getFigures()[c+1][r-1]));
		 if(isValidField(c, r-1) && board.getFigures()[c][r-1] == KING + offset)
			 attackers.add(new Position(c, r-1, board.getFigures()[c][r-1]));
		 if(isValidField(c-1, r-1) && board.getFigures()[c-1][r-1] == KING + offset)
			 attackers.add(new Position(c-1, r-1, board.getFigures()[c-1][r-1]));
		 if(isValidField(c-1, r) && board.getFigures()[c-1][r] == KING + offset)
			 attackers.add(new Position(c-1, r, board.getFigures()[c-1][r]));
		 if(isValidField(c-1, r+1) && board.getFigures()[c-1][r+1] == KING + offset)
			 attackers.add(new Position(c-1, r+1, board.getFigures()[c-1][r+1]));
		 
		 if(attackers.size() > startCount)
			 return true;
		 return false;
	 }
	 
	 public static boolean IsEnPassant(Board board, final int col, final int row) {
		 short[][] figures = board.getFigures();
		 short figureIndex = figures[col][row];
		 
		 if(figureIndex != 0) {
				int figureType = Figure.getType(figureIndex);
				int figureColor = Figure.getColor(figureIndex);
				if(figureType == PAWN) {
					if(figureColor == Board.WHITE) {
						if(row == 4) { // en passant
							Move prevMove = board.getHistory().prevMove;
							if(prevMove.getType() == PAWN && prevMove.getColor() == Board.BLACK) {// if there is a black pawn beside this position
								if(prevMove.getSourceRow() == 6 && prevMove.getDestRow() == 4) {// and the previous move of the oponent was by two rows
									assert(prevMove.getSourceCol() == prevMove.getDestCol());
									return true;
								}
							}
						}
					}
					else {
						if(row == 3) { // en passant
							Move prevMove = board.getHistory().prevMove;
							if(prevMove.getType() == PAWN && prevMove.getColor() == Board.WHITE) {// if there is a black pawn beside this position
								if(prevMove.getSourceRow() == 1 && prevMove.getDestRow() == 3) {// and the previous move of the oponent was by two rows
									assert(prevMove.getSourceCol() == prevMove.getDestCol());
									return true;
								}
							}
						}
					}
				}	
		 }
		 return false;
	 }
		
	 public static List<Move> getValidMoves(Board board, final int col, final int row) {
		ArrayList<Move> moves = new ArrayList<Move>();
		short[][] figures = board.getFigures();
		short figureIndex = figures[col][row];
		
		if(figureIndex != 0) {
			int figureType = Figure.getType(figureIndex);
			int figureColor = Figure.getColor(figureIndex);
			
			if(figureType == PAWN) {
				if(figureColor == Board.WHITE) {
					if(isValidField(col, row+1) && isFree(board, col, row+1)) {
						if(row == 6) { // pawn promotion
							moves.add(new Move(figureColor, figureType, col, row, col, row+1, QUEEN, false));
							moves.add(new Move(figureColor, figureType, col, row, col, row+1, ROOK, false));
							moves.add(new Move(figureColor, figureType, col, row, col, row+1, KNIGHT, false));
							moves.add(new Move(figureColor, figureType, col, row, col, row+1, BISHOP, false));
						}
						else
							moves.add(new Move(figureColor, figureType, col, row, col, row+1, -1, false));
						if(row == 1 && isFree(board, col, row + 2)) { // starting position two steps forward
							moves.add(new Move(figureColor, figureType, col, row, col, row+2, -1, false));
						}
					}
					// can we hit something?
					if(isValidField(col+1, row+1) && !isFree(board, col+1, row+1) && Figure.getColor(figures[col+1][row+1]) == Board.BLACK) {
						if(row == 6) { // pawn promotion
							moves.add(new Move(figureColor, figureType, col, row, col+1, row+1, QUEEN, true));
							moves.add(new Move(figureColor, figureType, col, row, col+1, row+1, ROOK, true));
							moves.add(new Move(figureColor, figureType, col, row, col+1, row+1, KNIGHT, true));
							moves.add(new Move(figureColor, figureType, col, row, col+1, row+1, BISHOP, true));
						}
						else {
							moves.add(new Move(figureColor, figureType, col, row, col+1, row+1, -1, true));
						}
					}
					if(isValidField(col-1, row+1) && !isFree(board, col-1, row+1) && Figure.getColor(figures[col-1][row+1]) == Board.BLACK) {
						if(row == 6) { // pawn promotion
							moves.add(new Move(figureColor, figureType, col, row, col-1, row+1, QUEEN, true));
							moves.add(new Move(figureColor, figureType, col, row, col-1, row+1, ROOK, true));
							moves.add(new Move(figureColor, figureType, col, row, col-1, row+1, KNIGHT, true));
							moves.add(new Move(figureColor, figureType, col, row, col-1, row+1, BISHOP, true));
						}
						else {
							moves.add(new Move(figureColor, figureType, col, row, col-1, row+1, -1, true));
						}
					}
					if(row == 4) { // en passant
						Move prevMove = board.getHistory().prevMove;
						if(prevMove.getType() == PAWN && prevMove.getColor() == Board.BLACK) {// if there is a black pawn beside this position
							if(prevMove.getSourceRow() == 6 && prevMove.getDestRow() == 4) {// and the previous move of the oponent was by two rows
								assert(prevMove.getSourceCol() == prevMove.getDestCol());
								if(prevMove.getDestCol() == col+1) {// then capture it!
									moves.add(new Move(figureColor, figureType, col, row, col+1, row+1, -1, true));
								}
								else if(prevMove.getDestCol() == col-1) {
									moves.add(new Move(figureColor, figureType, col, row, col-1, row+1, -1, true));
								}
							}
						}
					}
				}
				else { //figureColor == Board.BLACK
					if(isValidField(col, row-1) && isFree(board, col, row-1)) {
						if(row == 1) { // pawn promotion
							moves.add(new Move(figureColor, figureType, col, row, col, row-1, QUEEN, false));
							moves.add(new Move(figureColor, figureType, col, row, col, row-1, ROOK, false));
							moves.add(new Move(figureColor, figureType, col, row, col, row-1, KNIGHT, false));
							moves.add(new Move(figureColor, figureType, col, row, col, row-1, BISHOP, false));
						}
						else
							moves.add(new Move(figureColor, figureType, col, row, col, row-1, -1, false));
						if(row == 6 && isFree(board, col, row-2)) { // starting position two steps forward
							moves.add(new Move(figureColor, figureType, col, row, col, row-2, -1, false));
						}
					}
					// can we hit something?
					if(isValidField(col+1, row-1) && !isFree(board, col+1, row-1) && Figure.getColor(figures[col+1][row-1]) == Board.WHITE) {
						if(row == 1) { // pawn promotion
							moves.add(new Move(figureColor, figureType, col, row, col+1, row-1, QUEEN, true));
							moves.add(new Move(figureColor, figureType, col, row, col+1, row-1, ROOK, true));
							moves.add(new Move(figureColor, figureType, col, row, col+1, row-1, KNIGHT, true));
							moves.add(new Move(figureColor, figureType, col, row, col+1, row-1, BISHOP, true));
						}
						else {
							moves.add(new Move(figureColor, figureType, col, row, col+1, row-1, -1, true));
						}
					}
					if(isValidField(col-1, row-1) && !isFree(board, col-1, row-1) && Figure.getColor(figures[col-1][row-1]) == Board.WHITE) {
						if(row == 1) { // pawn promotion
							moves.add(new Move(figureColor, figureType, col, row, col-1, row-1, QUEEN, true));
							moves.add(new Move(figureColor, figureType, col, row, col-1, row-1, ROOK, true));
							moves.add(new Move(figureColor, figureType, col, row, col-1, row-1, KNIGHT, true));
							moves.add(new Move(figureColor, figureType, col, row, col-1, row-1, BISHOP, true));
						}
						else {
							moves.add(new Move(figureColor, figureType, col, row, col-1, row-1, -1, true));
						}
					}
					if(row == 3) { // en passant
						Move prevMove = board.getHistory().prevMove;
						if(prevMove.getType() == PAWN && prevMove.getColor() == Board.WHITE) {// if there is a black pawn beside this position
							if(prevMove.getSourceRow() == 1 && prevMove.getDestRow() == 3) {// and the previous move of the oponent was by two rows
								assert(prevMove.getSourceCol() == prevMove.getDestCol());
								if(prevMove.getDestCol() == col+1) {// then capture it!
									moves.add(new Move(figureColor, figureType, col, row, col+1, row-1, -1, true));
								}
								else if(prevMove.getDestCol() == col-1) {// then capture it!
									moves.add(new Move(figureColor, figureType, col, row, col-1, row-1, -1, true));
								}
							}
						}
					}
				}
			}			
			if (figureType == ROOK || figureType == QUEEN) { 
				// go up
				boolean goOn = true;
				int c = col;
				int r = row;
				while(goOn) {
					r++;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
				
				// go down
				goOn = true;
				c = col;
				r = row;
				while(goOn) {
					r--;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
				
				// go left
				goOn = true;
				c = col;
				r = row;
				while(goOn) {
					c--;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
				
				// go right
				goOn = true;
				c = col;
				r = row;
				while(goOn) {
					c++;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
			} 
				 
			if (figureType == KNIGHT) { 
				int c = col;
				int r = row;
				if(isValidDestination(board, figureColor, c+1, r+2)) {
					moves.add(new Move(figureColor, figureType, col, row, c+1, r+2, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c+2, r+1)) {
					moves.add(new Move(figureColor, figureType, col, row, c+2, r+1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c+2, r-1)) {
					moves.add(new Move(figureColor, figureType, col, row, c+2, r-1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c+1, r-2)) {
					moves.add(new Move(figureColor, figureType, col, row, c+1, r-2, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-1, r-2)) {
					moves.add(new Move(figureColor, figureType, col, row, c-1, r-2, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-2, r-1)) {
					moves.add(new Move(figureColor, figureType, col, row, c-2, r-1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-1, r+2)) {
					moves.add(new Move(figureColor, figureType, col, row, c-1, r+2, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-2, r+1)) {
					moves.add(new Move(figureColor, figureType, col, row, c-2, r+1, -1, isHit));
				}
				
			} 
			 
			if(figureType == BISHOP || figureType == QUEEN) {
				// go right up
				boolean goOn = true;
				int c = col;
				int r = row;
				while(goOn) {
					c++;
					r++;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
				// go right down
				goOn = true;
				c = col;
				r = row;
				while(goOn) {
					c++;
					r--;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
				// go left up
				goOn = true;
				c = col;
				r = row;
				while(goOn) {
					c--;
					r++;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
				// go left down
				goOn = true;
				c = col;
				r = row;
				while(goOn) {
					c--;
					r--;
					if(isValidDestination(board, figureColor, c, r)) {
						moves.add(new Move(figureColor, figureType, col, row, c, r, -1, isHit));
						if(isHit)
							goOn = false;
					}
					else
						goOn = false;
				}
			} 
			 
			if (figureType == KING) { 
				CheckCastling(board, figureColor, moves);
				int c = col;
				int r = row;
				ArrayList<Position> p = new ArrayList<Position>();
				if(isValidDestination(board, figureColor, c, r+1) && !isFieldAttackable(board, c, r+1, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c, r+1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c, r-1) && !isFieldAttackable(board, c, r-1, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c, r-1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c+1, r) && !isFieldAttackable(board, c+1, r, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c+1, r, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-1, r) && !isFieldAttackable(board, c-1, r, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c-1, r, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c+1, r+1) && !isFieldAttackable(board, c+1, r+1, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c+1, r+1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-1, r-1) && !isFieldAttackable(board, c-1, r-1, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c-1, r-1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c-1, r+1) && !isFieldAttackable(board, c-1, r+1, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c-1, r+1, -1, isHit));
				}
				if(isValidDestination(board, figureColor, c+1, r-1) && !isFieldAttackable(board, c+1, r-1, Board.FlipColor(figureColor), p)) {
					moves.add(new Move(figureColor, figureType, col, row, c+1, r-1, -1, isHit));
				}
				
			}
		}
		return moves;
	}
	
	private static void CheckCastling(Board board, int figureColor, ArrayList<Move> moves) {
		History history = board.getHistory();
		ArrayList<Position> p = new ArrayList<Position>();
		if(figureColor == Board.WHITE) {
			if (!history.WhiteKingMoved) {
	            if (!history.RWhiteRookMovedOrCaptured 
	            		&& board.getFigures()[5][0] == Board.EMPTY 
	            		&& board.getFigures()[6][0] == Board.EMPTY
	            		&& !isFieldAttackable(board, 5, 0, Board.FlipColor(figureColor), p)
	            		&& !isFieldAttackable(board, 5, 0, Board.FlipColor(figureColor), p) 
	            		&& !board.isInCheck(figureColor)) {
	                    	moves.add(new Move(figureColor, true, false));
	                	}
	            if (!history.LWhiteRookMovedOrCaptured
	            		&& board.getFigures()[1][0] == Board.EMPTY
        				&& board.getFigures()[2][0] == Board.EMPTY
						&& board.getFigures()[3][0] == Board.EMPTY
	            		&& !isFieldAttackable(board, 2, 0, Board.FlipColor(figureColor), p)
	            		&& !isFieldAttackable(board, 3, 0, Board.FlipColor(figureColor), p)
	            		&& !board.isInCheck(figureColor)) {
	            			moves.add(new Move(figureColor, false, true));
	            }
	        }
		}
		else {
			if (!history.BlackKingMoved) {
	            if (!history.LBlackRookMovedOrCaptured 
	            		&& board.getFigures()[5][7] == Board.EMPTY 
	            		&& board.getFigures()[6][7] == Board.EMPTY
	            		&& !isFieldAttackable(board, 5, 7, Board.FlipColor(figureColor), p)
	            		&& !isFieldAttackable(board, 5, 7, Board.FlipColor(figureColor), p) 
	            		&& !board.isInCheck(figureColor)) {
	                    	moves.add(new Move(figureColor, true, false));
	                	}
	            if (!history.RBlackRookMovedOrCaptured
	            		&& board.getFigures()[1][7] == Board.EMPTY
        				&& board.getFigures()[2][7] == Board.EMPTY
						&& board.getFigures()[3][7] == Board.EMPTY
	            		&& !isFieldAttackable(board, 2, 7, Board.FlipColor(figureColor), p)
	            		&& !isFieldAttackable(board, 3, 7, Board.FlipColor(figureColor), p)
	            		&& !board.isInCheck(figureColor)) {
	            			moves.add(new Move(figureColor, false, true));
	            }
	        }
		}
	}

	// Doesn't check en passant
	public static boolean IsHit(Board board, int color, int col, int row) {
		if(isValidDestination(board, color, col, row))
			return isHit;
		return false;
	}
	
	static private boolean isValidDestination(Board board, int color, int col, int row) {
		if(!isValidField(col, row))
			return false;
		short destFigureIndex = board.getFigures()[col][row];
		int figureColor = Figure.getColor(destFigureIndex);
		if(isFree(board, col, row)) {
			isHit = false;
			return true;
		}
		if(figureColor == color) {
			return false;
		}
		if(destFigureIndex == KING) {// adversarial king
			return false;
		}
		else {
			isHit = true;
			return true;
		}
	} 
			 
	static private boolean isFree(Board board, int col, int row) {
		return board.getFigures()[col][row] == Board.EMPTY;
	} 
	
	static private boolean isValidField(int col, int row) {
		if(col > 7 || col < 0 || row > 7 || row < 0)
			return false;
		return true;
	}

	public static boolean KingCanMove(int c, int r, int color, Board board) {
		// TODO same code as in getValidMoves() in king part
		ArrayList<Position> p = new ArrayList<Position>();
		
		if(isValidDestination(board, color, c, r+1) && !isFieldAttackable(board, c, r+1, Board.FlipColor(color), p)) {
			return true;
		}
		if(isValidDestination(board, color, c, r-1) && !isFieldAttackable(board, c, r-1, Board.FlipColor(color), p)) {
			return true;
		}
		if(isValidDestination(board, color, c+1, r) && !isFieldAttackable(board, c+1, r, Board.FlipColor(color), p)) {
			return true;
		}
		if(isValidDestination(board, color, c-1, r) && !isFieldAttackable(board, c-1, r, Board.FlipColor(color), p)) {
			return true;
		}
		if(isValidDestination(board, color, c+1, r+1) && !isFieldAttackable(board, c+1, r+1, Board.FlipColor(color), p)) {
			return true;
		}
		if(isValidDestination(board, color, c-1, r-1) && !isFieldAttackable(board, c-1, r-1, Board.FlipColor(color), p)) {
			return true;
		}
		if(isValidDestination(board, color, c-1, r+1) && !isFieldAttackable(board, c-1, r+1, Board.FlipColor(color), p)) {
			return true;
		}
		return false;
	}

	public static boolean isAttackerKing(ArrayList<Position> attacker) {
		return attacker.size() == 1 && Figure.getType(attacker.get(0).FigureIndex) == KING;
	}
	// can I put a figure in between?
	// precondition: king is in check
	public static boolean isBlockable(Position p, Board b, Position kingPos) {
		assert(Figure.getColor(kingPos.FigureIndex)) != Figure.getColor(p.FigureIndex);
		ArrayList<Position> attackers = new ArrayList<Position>();
		int type = Figure.getType(p.FigureIndex);
		if(type == ROOK || type == QUEEN) {
			if(p.Col - kingPos.Col == 0) {
				if(kingPos.Row < p.Row) {
					for(int i = kingPos.Row+1; i < p.Row; i++) {
						attackers.clear();
						assert(isFree(b, kingPos.Col, i));
						isReachableExceptPawns(b, kingPos.Col, i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						canPawnsReachFieldWithoutAttacking(b, kingPos.Col, i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						if(attackers.size() > 0 && !isAttackerKing(attackers))
							return true;
					}
				}
				else {
					assert(kingPos.Row > p.Row);
					for(int i = p.Row+1; i < kingPos.Row; i++) {
						attackers.clear();
						assert(isFree(b, kingPos.Col, i));
						isReachableExceptPawns(b, kingPos.Col, i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						canPawnsReachFieldWithoutAttacking(b, kingPos.Col, i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						if(attackers.size() > 0 && !isAttackerKing(attackers))
							return true;
					}
				}
				return false;
			}
			else if(p.Row - kingPos.Row == 0) {
				if(kingPos.Col < p.Col) {
					for(int i = kingPos.Col+1; i < p.Row; i++) {
						attackers.clear();
						assert(isFree(b, i, kingPos.Row));
						isReachableExceptPawns(b, i, kingPos.Row, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						canPawnsReachFieldWithoutAttacking(b, i, kingPos.Row, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						if(attackers.size() > 0 && !isAttackerKing(attackers))
							return true;
					}
				}
				else {
					assert(kingPos.Col > p.Col);
					for(int i = p.Row+1; i < kingPos.Row; i++) {
						attackers.clear();
						assert(isFree(b, i, kingPos.Row));
						isReachableExceptPawns(b, i, kingPos.Row, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						canPawnsReachFieldWithoutAttacking(b, i, kingPos.Row, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
						if(attackers.size() > 0 && !isAttackerKing(attackers))
							return true;
					}

				}
				return false;
			}
		}
		if(type == BISHOP || type == QUEEN) {
			int rowDiff = kingPos.Row - p.Row;
			int colDiff = kingPos.Col - p.Col;
			assert(Math.abs(rowDiff) == Math.abs(colDiff));
			if(colDiff > 0 && rowDiff> 0) {
				for(int i = 1; i < rowDiff; i++) {
					attackers.clear();
					assert(isFree(b, kingPos.Col - i, kingPos.Row - i));
					isReachableExceptPawns(b, kingPos.Col - i, kingPos.Row - i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					canPawnsReachFieldWithoutAttacking(b, kingPos.Col - i, kingPos.Row - i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					if(attackers.size() > 0 && !isAttackerKing(attackers))
						return true;
				}
			}
			if(colDiff < 0 && rowDiff < 0) {
				rowDiff *= -1;
				for(int i = 1; i < rowDiff; i++) {
					attackers.clear();
					assert(isFree(b, kingPos.Col + i, kingPos.Row + i));
					isReachableExceptPawns(b, kingPos.Col + i, kingPos.Row + i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					canPawnsReachFieldWithoutAttacking(b, kingPos.Col + i, kingPos.Row + i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					if(attackers.size() > 0 && !isAttackerKing(attackers))
						return true;
				}
				rowDiff *= -1;
			}
			if(colDiff < 0 && rowDiff > 0) {
				for(int i = 1; i < rowDiff; i++) {
					attackers.clear();
					assert(isFree(b, kingPos.Col + i, kingPos.Row - i));
					isReachableExceptPawns(b, kingPos.Col + i, kingPos.Row - i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					canPawnsReachFieldWithoutAttacking(b, kingPos.Col + i, kingPos.Row - i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					if(attackers.size() > 0 && !isAttackerKing(attackers))
						return true;
				}
			}
			if(colDiff > 0 && rowDiff < 0) {
				rowDiff *= -1;
				for(int i = 1; i < rowDiff; i++) {
					attackers.clear();
					assert(isFree(b, kingPos.Col - i, kingPos.Row + i));
					isReachableExceptPawns(b, kingPos.Col - i, kingPos.Row + i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					canPawnsReachFieldWithoutAttacking(b, kingPos.Col - i, kingPos.Row + i, Board.FlipColor(Figure.getColor(p.FigureIndex)), attackers);
					if(attackers.size() > 0 && !isAttackerKing(attackers))
						return true;
				}
				rowDiff *= -1;
			}
			return false;
		}
		return false;
	}

	public static boolean IsPartOfPawnChain(Board board, int color, int c, int r) {
		short offset = (color == Board.WHITE) ? WHITE_OFFSET : BLACK_OFFSET;
		if(isValidField(c+1, r+1) && board.getFigures()[c+1][r+1] == Figure.PAWN + offset)
			return true;
		if(isValidField(c-1, r+1) && board.getFigures()[c-1][r+1] == Figure.PAWN + offset)
			return true;
		if(isValidField(c+1, r-1) && board.getFigures()[c+1][r-1] == Figure.PAWN + offset)
			return true;
		if(isValidField(c-1, r-1) && board.getFigures()[c-1][r-1] == Figure.PAWN + offset)
			return true;
		return false;
	}

	public static boolean IsPawnPartOfDoublePawn(Board board, int color, int c,
			int r) {
		int counter = 1;
		short offset = (color == Board.WHITE) ? WHITE_OFFSET : BLACK_OFFSET;
		for(int i = 0; i < 8; i++) {
			if(i == r)
				continue;
			if(board.getFigures()[c][i] == Figure.PAWN + offset)
				counter++;
		}
		if(counter > 1)
			return true;
		return false;
	}

	public static boolean IsConnectedRook(Board board, int color, int c,
			int r) {
		short offset = (color == Board.WHITE) ? WHITE_OFFSET : BLACK_OFFSET;
		
		for(int i = c+1; i < 8; i++) {
			if(board.getFigures()[i][r] == Figure.ROOK + offset)
				return true;
			if(board.getFigures()[i][r] != Board.EMPTY)
				break;
		}
		for(int i = c-1; i >= 0; i--) {
			if(board.getFigures()[i][r] == Figure.ROOK + offset)
				return true;
			if(board.getFigures()[i][r] != Board.EMPTY)
				break;
		}
		for(int i = r+1; i < 8; i++) {
			if(board.getFigures()[c][i] == Figure.ROOK + offset)
				return true;
			if(board.getFigures()[c][i] != Board.EMPTY)
				break;
		}
		for(int i = r-1; i >= 0; i--) {
			if(board.getFigures()[c][i] == Figure.ROOK + offset)
				return true;
			if(board.getFigures()[c][i] != Board.EMPTY)
				break;
		}		
		return false;
	}
}
