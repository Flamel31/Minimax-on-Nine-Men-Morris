package model;

public enum GamePhase {
	PLACING_PIECES, MOVING_PIECES, REMOVING_PIECES, GAME_ENDED;
	
	public static char toChar(GamePhase phase) {
		switch(phase) {
			case GAME_ENDED:
				return 'E';
			case MOVING_PIECES:
				return 'M';
			case PLACING_PIECES:
				return 'P';
			case REMOVING_PIECES:
				return 'R';
		}
		return 'U';
	}
	
	public static GamePhase fromChar(char c) {
		switch(c) {
			case 'E':
				return GAME_ENDED;
			case 'M':
				return MOVING_PIECES;
			case 'P':
				return PLACING_PIECES;
			case 'R':
				return REMOVING_PIECES;
		}
		return null;
	}
	
	public static String toString(GamePhase phase) {
		switch(phase) {
			case GAME_ENDED:
				return "GAME ENDED";
			case MOVING_PIECES:
				return "MOVING PIECES";
			case PLACING_PIECES:
				return "PLACING PIECES";
			case REMOVING_PIECES:
				return "REMOVING PIECES";
			default:
				return "";
		}
	}
}
