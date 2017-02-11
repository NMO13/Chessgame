package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chess.Board;
import chess.EndStateEvent;
import chess.Figure;
import chess.Move;
import chess.MoveEvent;
import chess.Listener;

//TODO use random to choose a random move if more than 1 are equally good
public class HumanPlayerGUIAppWindow extends JFrame implements Listener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int TOPOFFSET = 26;
	static int RIGHTOFFSET = 8;
	static int LEFTOFFSET = 8;
	static int BOTTOMOFFSET = 100;
	
	static int CELLSIZE = 72;

	public Move move = null;
    
    private boolean drawRect = false;
    int rectX = -1;
    int rectY = -1;

	Board board = null;
	int myColor = -1;

	JTextArea textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane();
	
	public volatile boolean canMove = false;
	private boolean lockArea = false;
	
	public HumanPlayerGUIAppWindow(Board board, int color) {
		//this.getContentPane().setLayout(null);
		this.board = board;
		this.myColor = color;
		this.initWindow();
	}

	protected void initWindow() {

		this.setTitle("Martin's Chess Game  Turn: " + Board.colorToString(myColor));

		this.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				closeIt();
			}
			public void windowDeiconified(WindowEvent e) { }
			public void windowIconified(WindowEvent e) { }
			public void windowOpened(WindowEvent e) { }
			public void windowActivated(WindowEvent e) { }
			public void windowClosed(WindowEvent e) { }
			public void windowDeactivated(WindowEvent e) { }
		});

		BoardPanel bp = new BoardPanel();
		bp.setBackground(Color.BLACK);
		bp.setPreferredSize(new Dimension(CELLSIZE*8, CELLSIZE*8));
		getContentPane().add(bp, BorderLayout.NORTH);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		scrollPane.setPreferredSize(new Dimension(CELLSIZE*8, 100));
		getContentPane().add(scrollPane, BorderLayout.SOUTH);
	}

	

	public void closeIt(){
		System.exit(-1);
	}

	@Override
	public void nextMove(MoveEvent m) {
		Move move = (Move) m.getSource();
		if(move.getColor() ==  Board.BLACK) {
			textArea.append(" - ");
		    textArea.append(m.getSource().toString());
		    textArea.append("\n");
		}
		else
			textArea.append(m.getSource().toString());
		repaint();
	}

	
	class BoardPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int sourceRow = -1;
		private int sourceCol = -1;
		private int destRow = -1;
		private int destCol = -1;
	    private int figureIndex = -1;
	    
		BoardPanel() {
			this.addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e) { }
				public void mouseEntered(MouseEvent e) { }
				public void mouseExited(MouseEvent e) { }
				public void mousePressed(MouseEvent e) { }
				public void mouseReleased(MouseEvent e) {
					if(!lockArea)
						ReactOnClick(e.getX(), e.getY());
				}
			});
		}
		
		public void ReactOnClick(int X, int Y) {
			if(!canMove)
				return;
			int col = X / CELLSIZE;
			int row = Y / CELLSIZE;
			row = 7-row;
			if(sourceRow < 0) {
				// first click
				figureIndex = board.getFigures()[col][row];
				if(figureIndex > 0) {
					// figure is clicked
					if(Figure.getColor(((short)figureIndex)) == myColor) {
						sourceCol = col;
						sourceRow = row;
						drawRect = true;
						rectX = CELLSIZE * sourceCol;
						rectY = CELLSIZE * (7-sourceRow);
					} else {
						sourceCol = -1;
						sourceRow = -1;					
					}
				}
				else {
					sourceCol = -1;
					sourceRow = -1;
				}
			}
			else {
				destCol = col;
				destRow = row;
				boolean isHit = Figure.IsHit(board, myColor, col, row);
				if(!isHit) { // check en passant
					if(Figure.IsEnPassant(board, col, row))
						isHit = true; // maybe en passant
				}
				Move m = Move.CreateCastlingMove(sourceCol, sourceRow, destCol, destRow, Figure.getType(figureIndex), myColor);
				if(m == null) {
					short newType = -1;
					if(destRow == 7 && Figure.getType(figureIndex) == Figure.PAWN)
						newType = Figure.QUEEN;
					m = new Move(myColor, Figure.getType(figureIndex), sourceCol, sourceRow, destCol, destRow, newType, isHit);
				}
					
				List<Move> validMoves = board.getValidMoves(myColor);
				if(validMoves.size() == 0) {
					HumanPlayerGUIAppWindow.this.move = null;
				}
				if(Move.MoveListIncludesMove(validMoves, m)) {
					HumanPlayerGUIAppWindow.this.move = m;
				}
				sourceCol = -1;
				sourceRow = -1;
				figureIndex = -1;

				drawRect = false;
			}

			this.repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if((col + row) % 2 == 0) {
						g.setColor(Color.BLACK);
						g.fillRect(col*CELLSIZE, (7-row)*CELLSIZE, CELLSIZE, CELLSIZE);
						short figureIndex = board.getFigures()[col][row];
						String in = getImageName(figureIndex, Board.BLACK);
						drawImage(g, in, (col)*CELLSIZE, (7-row)*CELLSIZE);
					}
					else {
						g.setColor(Color.WHITE);
						g.fillRect(col*CELLSIZE, (7-row)*CELLSIZE, CELLSIZE, CELLSIZE);
						short figureIndex = board.getFigures()[col][row];
						String in = getImageName(figureIndex, Board.WHITE);
						drawImage(g, in, (col)*CELLSIZE, (7-row)*CELLSIZE);
					}
				}

			}

			if (drawRect) {
				g.setColor(Color.RED);
				g.drawRect(rectX, rectY, CELLSIZE, CELLSIZE);
			}
		}
		
		private void drawImage(Graphics g, String fn, int x, int y) {
			BufferedImage img = null;
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				if(fn == null)
					return;
				InputStream input = classLoader.getResourceAsStream(fn);
				try {
					img = ImageIO.read(input);
				} catch (IOException e) {
					e.printStackTrace();
				}
			g.drawImage(img, x, y, null);
		}

		private String getImageName(short figureIndex, int fieldColor) {
			if(fieldColor == Board.BLACK) {
				if(figureIndex == Figure.PAWN + Figure.BLACK_OFFSET)
					return "pawn_black_black.gif";
				if(figureIndex == Figure.PAWN + Figure.WHITE_OFFSET)
					return "pawn_white_black.gif";
				
				if(figureIndex == Figure.ROOK + Figure.BLACK_OFFSET)
					return "rook_black_black.gif";
				if(figureIndex == Figure.ROOK + Figure.WHITE_OFFSET)
					return "rook_white_black.gif";
				
				if(figureIndex == Figure.KNIGHT + Figure.BLACK_OFFSET)
					return "knight_black_black.gif";
				if(figureIndex == Figure.KNIGHT + Figure.WHITE_OFFSET)
					return "knight_white_black.gif";
				
				if(figureIndex == Figure.BISHOP + Figure.BLACK_OFFSET)
					return "bishop_black_black.gif";
				if(figureIndex == Figure.BISHOP + Figure.WHITE_OFFSET)
					return "bishop_white_black.gif";
				
				if(figureIndex == Figure.QUEEN + Figure.BLACK_OFFSET)
					return "queen_black_black.gif";
				if(figureIndex == Figure.QUEEN + Figure.WHITE_OFFSET)
					return "queen_white_black.gif";
				
				if(figureIndex == Figure.KING + Figure.BLACK_OFFSET)
					return "king_black_black.gif";
				if(figureIndex == Figure.KING + Figure.WHITE_OFFSET)
					return "king_white_black.gif";
			}
			else {
				if(figureIndex == Figure.PAWN + Figure.BLACK_OFFSET)
					return "pawn_black_white.gif";
				if(figureIndex == Figure.PAWN + Figure.WHITE_OFFSET)
					return "pawn_white_white.gif";
				
				if(figureIndex == Figure.ROOK + Figure.BLACK_OFFSET)
					return "rook_black_white.gif";
				if(figureIndex == Figure.ROOK + Figure.WHITE_OFFSET)
					return "rook_white_white.gif";
				
				if(figureIndex == Figure.KNIGHT + Figure.BLACK_OFFSET)
					return "knight_black_white.gif";
				if(figureIndex == Figure.KNIGHT + Figure.WHITE_OFFSET)
					return "knight_white_white.gif";
				
				if(figureIndex == Figure.BISHOP + Figure.BLACK_OFFSET)
					return "bishop_black_white.gif";
				if(figureIndex == Figure.BISHOP + Figure.WHITE_OFFSET)
					return "bishop_white_white.gif";
				
				if(figureIndex == Figure.QUEEN + Figure.BLACK_OFFSET)
					return "queen_black_white.gif";
				if(figureIndex == Figure.QUEEN + Figure.WHITE_OFFSET)
					return "queen_white_white.gif";
				
				if(figureIndex == Figure.KING + Figure.BLACK_OFFSET)
					return "king_black_white.gif";
				if(figureIndex == Figure.KING + Figure.WHITE_OFFSET)
					return "king_white_white.gif";
				
			}
			return null;
		}
		
		
	}

	@Override
	public void endState(EndStateEvent e) {
		if(e.mat) {
			if(e.color == Board.WHITE)
				textArea.append("\nWhite is mat");
			else
				textArea.append("\nBlack is mat");
		}
		else if(e.remis) {
			textArea.append("\nRemis");
		}
		else
			throw new IllegalArgumentException();
		lockArea  = true;
	}
}