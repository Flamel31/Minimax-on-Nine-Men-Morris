package model;

import model.actions.BoardAction;

public interface BoardListener {
	
	public void onBoardAction(BoardAction action,GamePiece turnPlayer);
	
	public void onChangeTurn(GamePiece newTurnPlayer);
	
	public void onPhaseChange(GamePhase newPhase);
}
