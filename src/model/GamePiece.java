package model;

import java.util.ArrayList;
import java.util.List;

public enum GamePiece {
	EMPTY, WHITE_PIECE, BLACK_PIECE;
	
	public static GamePiece swapPieceColor(GamePiece color) {
		return (color == WHITE_PIECE) ? BLACK_PIECE : WHITE_PIECE;
	}
	
	public static boolean isPieceColor(GamePiece color) {
		return color == WHITE_PIECE || color == BLACK_PIECE;
	}
	
	public static GamePiece fromChar(char c) {
		switch(c) {
			case 'E':
				return EMPTY;
			case 'W':
				return WHITE_PIECE;
			case 'B':
				return BLACK_PIECE;
		}
		return null;
	}
	
	public static char toChar(GamePiece piece) {
		switch(piece) {
			case EMPTY:
				return 'E';
			case WHITE_PIECE:
				return 'W';
			case BLACK_PIECE:
				return 'B';
		}
		return 'U';
	}
	
	public static List<GamePiece> fromString(String s){
		ArrayList<GamePiece> list = new ArrayList<GamePiece>();
		for(int i=0;i<s.length();i++) {
			list.add(fromChar(s.charAt(i)));
		}
		return list;
	}
	
	public static String toString(GamePiece piece) {
		switch(piece) {
			case EMPTY:
				return "EMPTY";
			case WHITE_PIECE:
				return "WHITE";
			case BLACK_PIECE:
				return "BLACK";
			default:
				return "";
		}
	}
}
