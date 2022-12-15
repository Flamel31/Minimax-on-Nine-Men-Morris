package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.BoardCoordinate;
import model.GameConst;
import model.GamePiece;
import model.NineMensMorris;

public class NineMensMorrisCanvas extends JPanel{

	private static final long serialVersionUID = 7357716233971746737L;
	// Background color
	private static final Color BACKGROUND_COLOR = new Color(255, 249, 168, 255);
	// Line 
	private static final Color LINE_COLOR = new Color(77, 53, 15, 255);
	private static final int LINE_WIDTH = 8;
	private static final Stroke LINE_STROKE = new BasicStroke(LINE_WIDTH);
	// Piece
	private static final int PIECE_SIZE = 40;
	private static final Color WHITE_COLOR = Color.WHITE;
	private static final Color BLACK_COLOR = new Color(25, 25, 25, 255);
	private static final Color SELECTED_COLOR = new Color(255, 146, 74, 255);
	private static final Stroke PIECE_STROKE = new BasicStroke(1);
	
	// Game associated with the canvas
	private NineMensMorris game;
	// Color of the player
	private GamePiece playerColor;
	// Width of each cell
	private int cellW;
	// Height of each cell
	private int cellH;
	// Width of the canvas that can be drawn
	private int w;
	// Height of the canvas that can be drawn
	private int h;
	// Selected piece
	private BoardCoordinate selected;
	
	public NineMensMorrisCanvas(GamePiece playerColor) {
		super();
		setPlayerColor(playerColor);
		initialize();
	}
	
	@Override
	public void paintComponent (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// Set Background color
		g2.setBackground(BACKGROUND_COLOR);
		g2.clearRect(0, 0, getWidth(), getHeight());
		// Evaluate the the size of each cell
		cellW = getWidth() / GameConst.BOARD_SIZE;
		cellH = getHeight() / GameConst.BOARD_SIZE;
		// Evaluate the part of the canvas that can be drawn
		w = cellW * GameConst.BOARD_SIZE;
		h = cellH * GameConst.BOARD_SIZE;
        // Print Board Line	
		g2.setStroke(LINE_STROKE);
        g2.setColor(LINE_COLOR);
        g2.drawRect(cellW/2, cellH/2, w - cellW, h - cellH);
        g2.drawRect(cellW + (cellW/2), cellH + (cellH/2), w - (3*cellW), h - (3*cellH));
        g2.drawRect(2*cellW + (cellW/2), 2*cellH + (cellH/2), w - (5*cellW), h - (5*cellH));
        g2.drawLine(cellW/2, 3*cellH+(cellH/2), 2*cellW+(cellW/2), 3*cellH+(cellH/2));
        g2.drawLine(4*cellW +(cellW/2), 3*cellH+(cellH/2), 6*cellW+(cellW/2), 3*cellH+(cellH/2));
        g2.drawLine(3*cellW+(cellW/2), cellH/2, 3*cellW+(cellW/2), 2*cellH+(cellH/2));
        g2.drawLine(3*cellW+(cellW/2), 4*cellH+(cellH/2), 3*cellW+(cellW/2), 6*cellH+(cellH/2));
        // If there is no game active
        if(game == null) return;
        // Draw the game pieces
        g2.setStroke(PIECE_STROKE);
        for(BoardCoordinate coords : GameConst.pointCoordinates) {
			if(GamePiece.isPieceColor(game.getGamePiece(coords))) {
				// Piece color
				if(coords.equals(selected)) g2.setColor(SELECTED_COLOR);
				else g2.setColor(game.getGamePiece(coords)==GamePiece.BLACK_PIECE ? BLACK_COLOR : WHITE_COLOR);
				// Draw piece
				g2.fillArc(coords.getColumn()*cellW+(cellW/2)-(PIECE_SIZE/2), 
						coords.getRow()*cellH+(cellH/2)-(PIECE_SIZE/2), 
						PIECE_SIZE, PIECE_SIZE, 0, 360);
				// Draw piece stroke
				g2.setColor(Color.BLACK);
				g2.drawArc(coords.getColumn()*cellW+(cellW/2)-(PIECE_SIZE/2), 
						coords.getRow()*cellH+(cellH/2)-(PIECE_SIZE/2), 
						PIECE_SIZE, PIECE_SIZE, 0, 360);
			}
        }
    }
	
	public void initialize() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BoardCoordinate coords = getBoardCoordinate(e);
				if(game != null && game.getTurn() == getPlayerColor() && coords != null) {
					boolean validAction = true;
					switch(game.getPhase()) {
						case MOVING_PIECES:
							// If there is a piece selected
							if(selected != null) {
								// If we pressed a different point otherwise deselect the piece
								if(!selected.equals(coords))
									validAction = game.movePiece(selected, coords);
								selected = null;
							}else{
								// Select the piece
								if(game.getGamePiece(coords)==getPlayerColor())
									selected = coords;
							}
							break;
						case PLACING_PIECES:
							validAction = game.addPiece(coords, getPlayerColor());
							break;
						case REMOVING_PIECES:
							validAction = game.removePiece(coords);
							break;
						default:
							break;
					}
					NineMensMorrisCanvas.this.repaint();
					// repaint();
					if(!validAction) showErrorMessage("Invalid action","The performed action is not valid, please make another move.");
				}
			}
		});
	}

	public NineMensMorris getGame() {
		return game;
	}

	public void setGame(NineMensMorris game) {
		this.game = game;
	}
	
	public GamePiece getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(GamePiece playerColor) {
		this.playerColor = playerColor;
	}
	
	private void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private BoardCoordinate getBoardCoordinate(MouseEvent e) {
		if(e.getX() <= w && e.getY() <= h) return new BoardCoordinate(e.getY()/cellH,e.getX()/cellW);
		return null;
	}
}
