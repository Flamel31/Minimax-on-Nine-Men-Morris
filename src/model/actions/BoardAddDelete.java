package model.actions;

import model.BoardCoordinate;

public class BoardAddDelete extends BoardAdd{
	
	private static final long serialVersionUID = 1610117661086876075L;
	
	// Coordinates of the piece that need to be removed
	private BoardCoordinate removeCoordinates;
	
	public BoardAddDelete(BoardCoordinate coordinates, BoardCoordinate removeCoordinates) {
		super(coordinates);
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
		return "BoardAddDelete [removeCoordinates=" + removeCoordinates + "]";
	}

}
