package chess;

import java.util.EventObject;

public class MoveEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MoveEvent(Move arg0) {
		super(arg0);
	}

}
