package model.actions;

import model.BoardCoordinate;

public class BoardAdd implements BoardAction{
	
	private static final long serialVersionUID = 1610117661086876075L;
	
	// Coordinates of the piece to be added
	private BoardCoordinate coordinates;

	public BoardAdd(BoardCoordinate coordinates) {
		this.coordinates = coordinates;
	}

	public BoardCoordinate getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(BoardCoordinate coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "BoardAdd [coordinates=" + coordinates + "]";
	}
}
