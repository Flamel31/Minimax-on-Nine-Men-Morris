package model.actions;

import model.BoardCoordinate;

public class BoardMoveDelete extends BoardMove {

	private static final long serialVersionUID = 1610117661086876075L;
	
	// Coordinates of the piece that need to be removed
	private BoardCoordinate removeCoordinates;
	
	public BoardMoveDelete(BoardCoordinate oldCoordinates, BoardCoordinate newCoordinates, BoardCoordinate removeCoordinates) {
		super(oldCoordinates, newCoordinates);
		this.setRemoveCoordinates(removeCoordinates);
	}

	public BoardCoordinate getRemoveCoordinates() {
		return removeCoordinates;
	}

	public void setRemoveCoordinates(BoardCoordinate removeCoordinates) {
		this.removeCoordinates = removeCoordinates;
	}

	@Override
	public String toString() {
		return "BoardMoveDelete [removeCoordinates=" + removeCoordinates + "]";
	}

}
