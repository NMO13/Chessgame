package chess;


public class History {	
	// for castling: checks whether the rook has moved or the rook was captured
	boolean BlackKingMoved = false;
	boolean LBlackRookMovedOrCaptured = false;
	boolean RBlackRookMovedOrCaptured = false;
	boolean WhiteKingMoved = false;
	boolean LWhiteRookMovedOrCaptured = false;
	boolean RWhiteRookMovedOrCaptured = false;
	public Move prevMove = null; // en passant
	
	public History clone() {
		History h = new History();
		h.BlackKingMoved = BlackKingMoved;
		h.LBlackRookMovedOrCaptured = LBlackRookMovedOrCaptured;
		h.RBlackRookMovedOrCaptured = RBlackRookMovedOrCaptured;
		h.WhiteKingMoved = WhiteKingMoved;
		h.LWhiteRookMovedOrCaptured = LWhiteRookMovedOrCaptured;
		h.RWhiteRookMovedOrCaptured = RWhiteRookMovedOrCaptured;
		h.prevMove = prevMove;
		return h;
	}
}
