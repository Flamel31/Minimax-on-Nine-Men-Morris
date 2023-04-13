package model.actions;

import model.BoardCoordinate;

public class BoardMove implements BoardAction{
	
	private static final long serialVersionUID = 1610117661086876075L;
	
	// The old coordinates of the piece
	private BoardCoordinate oldCoordinates;
	// The new coordinates of the piece
	private BoardCoordinate newCoordinates;	
	
	public BoardMove(BoardCoordinate oldCoordinates, BoardCoordinate newCoordinates) {
		this.oldCoordinates = oldCoordinates;
		this.newCoordinates = newCoordinates;
	}
	
	public BoardCoordinate getOldCoordinates() {
		return oldCoordinates;
	}
	
	public void setOldCoordinates(BoardCoordinate oldCoordinates) {
		this.oldCoordinates = oldCoordinates;
	}
	
	public BoardCoordinate getNewCoordinates() {
		return newCoordinates;
	}
	
	public void setNewCoordinates(BoardCoordinate newCoordinates) {
		this.newCoordinates = newCoordinates;
	}

	@Override
	public String toString() {
		return "BoardMove [oldCoordinates=" + oldCoordinates + ", newCoordinates=" + newCoordinates + "]";
	}
}
