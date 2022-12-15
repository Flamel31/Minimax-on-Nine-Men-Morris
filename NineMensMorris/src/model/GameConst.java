package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConst {
	// Size of the board
	public static final int BOARD_SIZE = 7;
	// Total number of pieces
	public static final int PIECES = 18;
	// Number of pieces to enter FLYING mode
	public static final int FLYING_PIECES = 3;
	// Directions
	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	// Values
	public static final int WIN_VALUE = 999;
	public static final int LOSE_VALUE = -999;
	public static final int PIECE_VALUE = 5;
	public static final int MILL_VALUE = 1;
	
	public static List<BoardCoordinate> pointCoordinates;
	
	public static Map<BoardCoordinate,BoardCoordinate[]> pointConnections;
	
	static {
		// Adding point coordinates
		pointCoordinates = new ArrayList<>();
		pointCoordinates.add(new BoardCoordinate(0,0));
		pointCoordinates.add(new BoardCoordinate(0,3));
		pointCoordinates.add(new BoardCoordinate(0,6));
		pointCoordinates.add(new BoardCoordinate(1,1));
		pointCoordinates.add(new BoardCoordinate(1,3));
		pointCoordinates.add(new BoardCoordinate(1,5));
		pointCoordinates.add(new BoardCoordinate(2,2));
		pointCoordinates.add(new BoardCoordinate(2,3));
		pointCoordinates.add(new BoardCoordinate(2,4));
		pointCoordinates.add(new BoardCoordinate(3,0));
		pointCoordinates.add(new BoardCoordinate(3,1));
		pointCoordinates.add(new BoardCoordinate(3,2));
		pointCoordinates.add(new BoardCoordinate(3,4));
		pointCoordinates.add(new BoardCoordinate(3,5));
		pointCoordinates.add(new BoardCoordinate(3,6));
		pointCoordinates.add(new BoardCoordinate(4,2));
		pointCoordinates.add(new BoardCoordinate(4,3));
		pointCoordinates.add(new BoardCoordinate(4,4));
		pointCoordinates.add(new BoardCoordinate(5,1));
		pointCoordinates.add(new BoardCoordinate(5,3));
		pointCoordinates.add(new BoardCoordinate(5,5));
		pointCoordinates.add(new BoardCoordinate(6,0));
		pointCoordinates.add(new BoardCoordinate(6,3));
		pointCoordinates.add(new BoardCoordinate(6,6));
		// Setting point connections
		pointConnections = new HashMap<>();
		// First row
		pointConnections.put(new BoardCoordinate(0,0), new BoardCoordinate[] 
				{null,new BoardCoordinate(0,3),new BoardCoordinate(3,0),null});
		pointConnections.put(new BoardCoordinate(0,3), new BoardCoordinate[] 
				{null,new BoardCoordinate(0,6), new BoardCoordinate(1,3), new BoardCoordinate(0,0)});
		pointConnections.put(new BoardCoordinate(0,6), new BoardCoordinate[] 
				{null,null,new BoardCoordinate(3,6),new BoardCoordinate(0,3)});
		// Second row
		pointConnections.put(new BoardCoordinate(1,1), new BoardCoordinate[] 
				{null,new BoardCoordinate(1,3),new BoardCoordinate(3,1), null});
		pointConnections.put(new BoardCoordinate(1,3), new BoardCoordinate[] 
				{new BoardCoordinate(0,3),new BoardCoordinate(1,5),new BoardCoordinate(2,3),new BoardCoordinate(1,1)});
		pointConnections.put(new BoardCoordinate(1,5), new BoardCoordinate[] 
				{null,null,new BoardCoordinate(3,5),new BoardCoordinate(1,3)});
		// Third row
		pointConnections.put(new BoardCoordinate(2,2), new BoardCoordinate[] 
				{null,new BoardCoordinate(2,3),new BoardCoordinate(3,2),null});
		pointConnections.put(new BoardCoordinate(2,3), new BoardCoordinate[] 
				{new BoardCoordinate(1,3),new BoardCoordinate(2,4),null,new BoardCoordinate(2,2)});
		pointConnections.put(new BoardCoordinate(2,4), new BoardCoordinate[] 
				{null,null,new BoardCoordinate(3,4),new BoardCoordinate(2,3)});
		// Fourth row
		pointConnections.put(new BoardCoordinate(3,0), new BoardCoordinate[] 
				{new BoardCoordinate(0,0),new BoardCoordinate(3,1),new BoardCoordinate(6,0),null});
		pointConnections.put(new BoardCoordinate(3,1), new BoardCoordinate[] 
				{new BoardCoordinate(1,1),new BoardCoordinate(3,2),new BoardCoordinate(5,1),new BoardCoordinate(3,0)});
		pointConnections.put(new BoardCoordinate(3,2), new BoardCoordinate[] 
				{new BoardCoordinate(2,2),null,new BoardCoordinate(4,2),new BoardCoordinate(3,1)});
		pointConnections.put(new BoardCoordinate(3,4), new BoardCoordinate[] 
				{new BoardCoordinate(2,4),new BoardCoordinate(3,5),new BoardCoordinate(4,4),null});
		pointConnections.put(new BoardCoordinate(3,5), new BoardCoordinate[] 
				{new BoardCoordinate(1,5),new BoardCoordinate(3,6),new BoardCoordinate(5,5),new BoardCoordinate(3,4)});
		pointConnections.put(new BoardCoordinate(3,6), new BoardCoordinate[] 
				{new BoardCoordinate(0,6),null,new BoardCoordinate(6,6),new BoardCoordinate(3,5)});
		// Fifth row
		pointConnections.put(new BoardCoordinate(4,2), new BoardCoordinate[] 
				{new BoardCoordinate(3,2),new BoardCoordinate(4,3),null,null});
		pointConnections.put(new BoardCoordinate(4,3), new BoardCoordinate[] 
				{null,new BoardCoordinate(4,4),new BoardCoordinate(5,3),new BoardCoordinate(4,2)});
		pointConnections.put(new BoardCoordinate(4,4), new BoardCoordinate[] 
				{new BoardCoordinate(3,4),null,null,new BoardCoordinate(4,3)});
		// Sixth row
		pointConnections.put(new BoardCoordinate(5,1), new BoardCoordinate[] 
				{new BoardCoordinate(3,1),new BoardCoordinate(5,3),null,null});
		pointConnections.put(new BoardCoordinate(5,3), new BoardCoordinate[] 
				{new BoardCoordinate(4,3),new BoardCoordinate(5,5),new BoardCoordinate(6,3),new BoardCoordinate(5,1)});
		pointConnections.put(new BoardCoordinate(5,5), new BoardCoordinate[] 
				{new BoardCoordinate(3,5),null,null,new BoardCoordinate(5,3)});
		// Seventh row
		pointConnections.put(new BoardCoordinate(6,0), new BoardCoordinate[] 
				{new BoardCoordinate(3,0),new BoardCoordinate(6,3),null,null});
		pointConnections.put(new BoardCoordinate(6,3), new BoardCoordinate[] 
				{new BoardCoordinate(5,3),new BoardCoordinate(6,6),null,new BoardCoordinate(6,0)});
		pointConnections.put(new BoardCoordinate(6,6), new BoardCoordinate[] 
				{new BoardCoordinate(3,6),null,null,new BoardCoordinate(6,3)});
	}
}
