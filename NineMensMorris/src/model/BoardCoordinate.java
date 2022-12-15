package model;

import java.io.Serializable;

public class BoardCoordinate implements Serializable{

	private static final long serialVersionUID = 1610117661086876075L;
	
	private int row;
	private int column;
	
	public BoardCoordinate(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	

	@Override
	public int hashCode() {
		int result;
		result = 31 * column;
		result = 31 * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardCoordinate other = (BoardCoordinate) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[row=" + row + ", column=" + column + "]";
	}
}
