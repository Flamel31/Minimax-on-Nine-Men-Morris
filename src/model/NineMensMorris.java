package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.actions.BoardAction;
import model.actions.BoardAdd;
import model.actions.BoardAddDelete;
import model.actions.BoardDelete;
import model.actions.BoardMove;
import model.actions.BoardMoveDelete;

public class NineMensMorris{
	
	// 7x7 Matrix that represent the game board
	private GamePiece[][] board;
	// Phase of the game
	private GamePhase phase;
	// Turn player (BlackPiece/WhitePiece)
	private GamePiece turn;
	// Number of pieces for each player
	private Map<GamePiece,Integer> pieces;
	// Pieces added on the board
	private int addedPieces;
	// Listeners of the game
	private List<BoardListener> boardListeners;
	
	public NineMensMorris(List<GamePiece> points, GamePhase phase, GamePiece turn,int addedPieces){
		// Pieces on the board
		pieces = new HashMap<>();
		pieces.put(GamePiece.WHITE_PIECE, 0);
		pieces.put(GamePiece.BLACK_PIECE, 0);
		// Init board if the 
		board = new GamePiece[GameConst.BOARD_SIZE][GameConst.BOARD_SIZE];
		for(int i=0;i< GameConst.pointCoordinates.size();i++) {
			GamePiece point = (i < points.size()) ? points.get(i) : GamePiece.EMPTY;
			setGamePiece(GameConst.pointCoordinates.get(i),point);
			if(pieces.containsKey(point)) 
				pieces.put(point,pieces.get(point)+1);
		}
		this.phase = phase;
		this.turn = turn;
		this.addedPieces = addedPieces;
		// Init listeners list
		boardListeners = new ArrayList<>();
	}
	
	public NineMensMorris(String s) {
		this(GamePiece.fromString(s.substring(0, 24)),
				GamePhase.fromChar(s.charAt(24)),
				GamePiece.fromChar(s.charAt(25)),
				Integer.parseInt(s.substring(26)));
	}
	
	public NineMensMorris() {
		this(new ArrayList<>(),GamePhase.PLACING_PIECES,GamePiece.WHITE_PIECE,0);
	}
	
	/*
	 * Getters  for phase and turn
	 */

	public GamePhase getPhase() {
		return phase;
	}


	public GamePiece getTurn() {
		return turn;
	}

	/*
	 * Return a value that represent the game piece at the provided coordinates.
	 */
	
	public GamePiece getGamePiece(BoardCoordinate coords) {
		return board[coords.getRow()][coords.getColumn()];
	}
	
	/*
	 * Set a the value that represent the game piece at the provided coordinates.
	 */
	
	public void setGamePiece(BoardCoordinate coords, GamePiece piece) {
		board[coords.getRow()][coords.getColumn()] = piece;
	}
	
	/*
	 * Change the turn current turn if the game is not ended.
	 */
	public void changeTurn(){
		GamePiece nextTurn = GamePiece.swapPieceColor(turn);
		if(phase!=GamePhase.GAME_ENDED) {
			if(phase==GamePhase.MOVING_PIECES && !canMovePieces(nextTurn)) {
				changePhase(GamePhase.GAME_ENDED);
			}else {
				turn = nextTurn;
				for(BoardListener list : boardListeners) {
					list.onChangeTurn(nextTurn);
				}
			}
		}
	}
	
	/*
	 * Change the phase
	 */
	public void changePhase(GamePhase phase) {
		this.phase = phase;
		for(BoardListener list : boardListeners) {
			list.onPhaseChange(phase);
		}
	}
	
	/*
	 * Return if the piece at a given coordinate is part of a mills.
	 */
	public boolean checkMills(BoardCoordinate coords) {
		GamePiece color = getGamePiece(coords);
		BoardCoordinate[] coordsConn = GameConst.pointConnections.get(coords);
		return (checkMillsDirection(coordsConn[GameConst.LEFT],color,GameConst.LEFT) && checkMillsDirection(coordsConn[GameConst.RIGHT],color,GameConst.RIGHT)) ||
				(checkMillsDirection(coordsConn[GameConst.TOP],color,GameConst.TOP) && checkMillsDirection(coordsConn[GameConst.BOTTOM],color,GameConst.BOTTOM));
	}
	
	/*
	 * Utility function that explore the board in a given direction
	 * to check if that point has a piece of a given color.
	 */
	private boolean checkMillsDirection(BoardCoordinate coords, GamePiece color, int direction) {
		if(coords == null)
			return true;
		return getGamePiece(coords) == color && 
				checkMillsDirection(GameConst.pointConnections.get(coords)[direction],color,direction);
	}
	
	/*
	 * Return if moving a piece from a given coordinates to another coordinates 
	 * is a valid move.
	 */
	public boolean checkValidMove(BoardCoordinate oldCoords,BoardCoordinate newCoords) {
		// Check if the new coordinates are empty and there is a piece in the old coordinates
		if(getGamePiece(newCoords)==GamePiece.EMPTY && GamePiece.isPieceColor(getGamePiece(oldCoords))){
			// If flying mode 
			if(pieces.get(getGamePiece(oldCoords)) == GameConst.FLYING_PIECES) return true;
			// If normal move mode, check if the new coordinates are connected to the old coordinates
			for(BoardCoordinate coords : GameConst.pointConnections.get(oldCoords)) {
				if(coords != null && coords.equals(newCoords)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Return if moving a piece from a given coordinates to another coordinates 
	 * create a mills.
	 */
	public boolean checkMoveCreateMills(BoardCoordinate oldCoords,BoardCoordinate newCoords) {
		boolean result = false;
		if(getGamePiece(newCoords)==GamePiece.EMPTY && GamePiece.isPieceColor(getGamePiece(oldCoords))){
			setGamePiece(newCoords,getGamePiece(oldCoords));
			setGamePiece(oldCoords,GamePiece.EMPTY);
			result = checkMills(newCoords);
			setGamePiece(oldCoords,getGamePiece(newCoords));
			setGamePiece(newCoords,GamePiece.EMPTY);
		}
		return result;
	}

	/*
	 * Return if adding a piece  at a given coordinates to another coordinates 
	 * create a mills.
	 */
	public boolean checkAddCreateMills(BoardCoordinate coords,GamePiece color) {
		boolean result = false;
		if(getGamePiece(coords)==GamePiece.EMPTY){
			setGamePiece(coords,color);
			result = checkMills(coords);
			setGamePiece(coords,GamePiece.EMPTY);
		}
		return result;
	}
	
	/*
	 * Return if the player of the color passed as argument can make delete 
	 * pieces controlled by the opponent.
	 */
	public boolean canDeletePieces(GamePiece color){
		GamePiece oppositeColor = GamePiece.swapPieceColor(color);
		for(BoardCoordinate coords : GameConst.pointCoordinates) {
			if(getGamePiece(coords)==oppositeColor && !checkMills(coords))
				return true;
		}
		return false;
	}
	
	/*
	 * Return if the player of the color passed as argument can make moves.
	 */
	public boolean canMovePieces(GamePiece color) {
		for(BoardCoordinate coords : GameConst.pointCoordinates) {
			if(getGamePiece(coords) == color) {
				// If flying mode the list of possible points is composed by all the EMPTY points
				// Otherwise check EMPTY points connected to the current point coordinates
				List<BoardCoordinate> connections = pieces.get(color) == GameConst.FLYING_PIECES ? GameConst.pointCoordinates : Arrays.asList(GameConst.pointConnections.get(coords));
				for(BoardCoordinate conns : connections) {
					if(conns != null && getGamePiece(conns) == GamePiece.EMPTY)
						return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Add a piece of the selected color at a given coordinate.
	 * If the piece create a mills the phase will change in Removing Pieces.
	 */
	public boolean addPiece(BoardCoordinate coords, GamePiece color) {
		if(phase == GamePhase.PLACING_PIECES && getGamePiece(coords) == GamePiece.EMPTY){
			setGamePiece(coords,color);
			pieces.replace(color, pieces.get(color)+1);
			addedPieces++;
			for(BoardListener list : boardListeners) {
				list.onBoardAction(new BoardAdd(coords),turn);
			}
			if(checkMills(coords) && canDeletePieces(turn)){
				changePhase(GamePhase.REMOVING_PIECES);
			}else{
				changePhase(addedPieces == GameConst.PIECES ? GamePhase.MOVING_PIECES : GamePhase.PLACING_PIECES);
				changeTurn();
			}
			return true;
		}
		return false;
	}
	
	/*
	 * Move a piece from at a given coordinates to another coordinates.
	 * If the move create a mills the phase will change in Removing Pieces.
	 */
	public boolean movePiece(BoardCoordinate oldCoords,BoardCoordinate newCoords) {
		GamePiece color = getGamePiece(oldCoords);
		if(phase==GamePhase.MOVING_PIECES && turn==color && checkValidMove(oldCoords,newCoords)){
			setGamePiece(oldCoords,GamePiece.EMPTY);
			setGamePiece(newCoords,color);
			for(BoardListener list : boardListeners) {
				list.onBoardAction(new BoardMove(oldCoords,newCoords),turn);
			}
			if(checkMills(newCoords) && canDeletePieces(turn)) changePhase(GamePhase.REMOVING_PIECES);
			else changeTurn();
			return true;
		}
		return false;
	}
	
	/*
	 * Remove a piece at a given coordinates.
	 * If there are less than 3 pieces of that color the game ends.
	 */
	public boolean removePiece(BoardCoordinate coords) {
		GamePiece color = getGamePiece(coords);
		if(phase==GamePhase.REMOVING_PIECES && GamePiece.isPieceColor(color) && turn!=color && !checkMills(coords)){
			pieces.replace(color, pieces.get(color)-1);
			setGamePiece(coords,GamePiece.EMPTY);
			for(BoardListener list : boardListeners) {
				list.onBoardAction(new BoardDelete(coords),turn);
			}
			if(addedPieces == GameConst.PIECES)
				changePhase(pieces.get(color) < GameConst.FLYING_PIECES ? GamePhase.GAME_ENDED : GamePhase.MOVING_PIECES);
			else
				changePhase(GamePhase.PLACING_PIECES);
			changeTurn();
			return true;
		}
		return false;
	}
	
	/*
	 * Perform a certain board action.
	 */
	public boolean performAction(BoardAction boardAction) {
		boolean result = false;
		if(boardAction instanceof BoardAdd) {
			BoardAdd boardAdd = (BoardAdd) boardAction;
			result = addPiece(boardAdd.getCoordinates(), turn);
			if(result && boardAction instanceof BoardAddDelete) {
				result = removePiece(((BoardAddDelete) boardAction).getRemoveCoordinates());
			}
		}else if(boardAction instanceof BoardMove) {
			BoardMove boardMove = (BoardMove) boardAction;
			result = movePiece(boardMove.getOldCoordinates(),boardMove.getNewCoordinates());
			if(result && boardAction instanceof BoardMoveDelete) {
				result = removePiece(((BoardMoveDelete) boardAction).getRemoveCoordinates());
			}
		}else if(boardAction instanceof BoardDelete) {
			BoardDelete boardDelete = (BoardDelete) boardAction;
			result = removePiece(boardDelete.getCoordinates());
		}
		return result;
	}
	
	/*
	 * Return a list containing all the possible actions from the current state
	 * of the board for a given player.
	 */
	public List<BoardAction> generateMoves(GamePiece color){
		ArrayList<BoardAction> list = new ArrayList<>();
		GamePiece opponentColor = GamePiece.swapPieceColor(color);
		if(phase == GamePhase.MOVING_PIECES) {
			for(BoardCoordinate coords : GameConst.pointCoordinates) {
				if(getGamePiece(coords) == color) {
					// If flying mode the list of possible points is composed by all the EMPTY points
					// Otherwise check EMPTY points connected to the current point coordinates
					List<BoardCoordinate> connections = pieces.get(color) == GameConst.FLYING_PIECES ? GameConst.pointCoordinates : Arrays.asList(GameConst.pointConnections.get(coords));
					for(BoardCoordinate conns : connections) {
						if(conns != null && getGamePiece(conns) == GamePiece.EMPTY) {
							if(checkMoveCreateMills(coords,conns)) {
								// If the move created a Mills
								// Generate all the possible move + delete of the piece
								for(BoardCoordinate coordsOpponent : GameConst.pointCoordinates) {
									if(getGamePiece(coordsOpponent)==opponentColor && !checkMills(coordsOpponent)) {
										list.add(new BoardMoveDelete(coords,conns,coordsOpponent));
									}
								}
							}else {
								// If the move didn't create a Mills
								// Add the single move
								list.add(new BoardMove(coords,conns));
							}
						}
						
					}
				}
			}
		}else if(phase == GamePhase.PLACING_PIECES) {
			for(BoardCoordinate coords : GameConst.pointCoordinates) {
				if(getGamePiece(coords)==GamePiece.EMPTY) {
					if(checkAddCreateMills(coords,color)) {
						// If the move created a Mills
						// Generate all the possible move + delete of the piece
						for(BoardCoordinate coordsOpponent : GameConst.pointCoordinates) {
							if(getGamePiece(coordsOpponent)==opponentColor && !checkMills(coordsOpponent)) {
								list.add(new BoardAddDelete(coords,coordsOpponent));
							}
						}
					}else {
						// If the move didn't create a Mills
						// Add the single move
						list.add(new BoardAdd(coords));
					}
				}
			}
		}else if(phase == GamePhase.REMOVING_PIECES) {
			for(BoardCoordinate coords : GameConst.pointCoordinates) {
				if(getGamePiece(coords)==opponentColor && !checkMills(coords)) {
					list.add(new BoardDelete(coords));
				}
			}
		}
		return list;
	}
	
	/*
	 * Return an integer value that represent how good the current board state
	 * is for the 2 players.
	 * An high value represent a good board for player1 
	 * a low value represent a good board for player2.
	 */
	public int value(GamePiece player1,GamePiece player2) {
		return value(player1) - value(player2);
	}
	
	private int value(GamePiece color) {
		// If the game ended return the max or min value
		if(phase == GamePhase.GAME_ENDED) 
			return turn == color ? GameConst.WIN_VALUE : GameConst.LOSE_VALUE;
		// 1 point for each possible move
		int value = generateMoves(color).size();
		// If we are in placing pieces count also the number of pieces in hand
		if(phase == GamePhase.PLACING_PIECES) {
			int piecesInHand = color == GamePiece.WHITE_PIECE ? (int)Math.ceil(addedPieces/2) : (int)Math.floor(addedPieces/2);
			value += GameConst.PIECE_VALUE * piecesInHand;
		}
		// For each piece still on the board add PIECE_VALUE points
		value += GameConst.PIECE_VALUE * pieces.get(color);
		// Give more value to the pieces that are in a mill
		for(BoardCoordinate coords : GameConst.pointCoordinates) {
			if(getGamePiece(coords) == color && checkMills(coords)) {
				value += GameConst.MILL_VALUE;
			}
		}

		return value;
	}
	
	/*
	 * Add a listener to the list of board listeners.
	 */
	public void addListener(BoardListener listener) {
		boardListeners.add(listener);
	}
	
	/*
	 * Return a string that represent the state of the current board.
	 * The first 24 characters represent a point on the board.
	 * The next 2 characters represent the current phase and turn player.
	 * The last characters represent the pieces placed by the two players.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(BoardCoordinate coords : GameConst.pointCoordinates) {
			stringBuilder.append(GamePiece.toChar(getGamePiece(coords)));
		}
		stringBuilder.append(GamePhase.toChar(phase));
		stringBuilder.append(GamePiece.toChar(turn));
		stringBuilder.append(addedPieces);
		return stringBuilder.toString();
	}
}
