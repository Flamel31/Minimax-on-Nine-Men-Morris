package model.minimax;

import java.util.ArrayList;
import java.util.List;

import model.GamePiece;
import model.NineMensMorris;
import model.actions.BoardAction;

public class TreeNode {
	// List of child nodes
	private List<TreeNode> childNodes;
	// Is Maximizing Player 
	private boolean isMaximizingPlayer;
	// String that represent the board state
	private String boardState;
	// Board action that lead to that board state
	private BoardAction boardAction;
	// Value of the node
	private int value;
	
	// Constructor for leaf node only
	public TreeNode(String boardState, BoardAction boardAction, boolean isMaximizingPlayer, int value) {
		this.boardState = boardState;
		this.boardAction = boardAction;
		this.isMaximizingPlayer = isMaximizingPlayer;
		this.value = value;
		childNodes = new ArrayList<>();
	}
	
	// Constructor for node that are not lead node
	public TreeNode(String boardState, BoardAction boardAction, boolean isMaximizingPlayer) {
		this(boardState,boardAction,isMaximizingPlayer,0);
	}
	
	// Constructor for root node only
	public TreeNode(String boardState) {
		this(boardState,null,true);
	}
	
	/*
	 * Return the child node at the selected index.
	 * If index is greater than size return null.
	 */
	public TreeNode getChildNode(int index) {
		return index < childNodes.size() ? childNodes.get(index) : null;
	}
	
	/*
	 * Return the child node with the same board state 
	 * passed as argument, null otherwise.
	 */
	public TreeNode getChildNode(String boardState) {
		for(TreeNode child : childNodes) {
			if(child.getBoardState().equals(boardState))
				return child;
		}
		return null;
	}
	
	public void generateChildNodes(int maxDepth) {
		generateChildNodes(maxDepth,0);
	}
	
	private void generateChildNodes(int maxDepth, int depth) {
		if(depth > maxDepth)
			return;
		// Only generate the child nodes once
		if(childNodes.isEmpty()) {
			// Create a game engine object
			NineMensMorris game = new NineMensMorris(getBoardState());
			// For each possible move
			for(BoardAction move : game.generateMoves(game.getTurn())) {
				// Clone the board state
				NineMensMorris clone = new NineMensMorris(getBoardState());
				// Perform the action
				clone.performAction(move);
				// Add the child node
				if(depth == maxDepth) childNodes.add(new TreeNode(clone.toString(),move,!isMaximizingPlayer,clone.value(GamePiece.BLACK_PIECE,GamePiece.WHITE_PIECE)));
				else childNodes.add(new TreeNode(clone.toString(),move,!isMaximizingPlayer));
			}
		}
		// Recursive call
		for(TreeNode child : childNodes) {
			child.generateChildNodes(maxDepth, depth+1);
		}
	}
	
	/*
	 * Return the child node that lead to the best value
	 */
	public TreeNode minimax() {
		return getChildNode(minimax(0,Integer.MIN_VALUE,Integer.MAX_VALUE));
	}
	
	/*
	 * Recursive function that find the best value on the tree using minimax with alpha-beta pruning.
	 * Return the index of the child node that lead to the best value.
	 */
	private int minimax(int depth, int alpha, int beta) {
		// If node is a leaf node
		if(childNodes.isEmpty()) 
			return value;
		// The best value found
	    int bestValue;
	    // The index that lead to the best value found (used only on depth 0)
	    int bestIndex = -1;
	    if(isMaximizingPlayer) {
	        bestValue = Integer.MIN_VALUE;
	        for(int i=0;i<childNodes.size();i++) {
	        	// Get the value of the child node
	        	int val = childNodes.get(i).minimax(depth+1,alpha,beta);
	        	// If the value is greater set the new best value
	        	bestValue = Math.max(val, bestValue);
	        	// If the value as change set the best index (used only on depth 0)
	        	if(bestValue == val) bestIndex = i;
	        	// If the best value is greater than alpha set new alpha
	        	alpha = Math.max(alpha, bestValue);
	        	// Pruning
	        	if(beta <= alpha) break;
	        }
	    }else {
	    	// Similar to the above loop but we don't need to save the index since the 
	    	// node at depth 0 is always maximizingPlayer
	    	bestValue = Integer.MAX_VALUE;
	    	for(TreeNode child : childNodes) {
	    		// If the value of the node is lower than the current best value set new best value
	    		bestValue = Math.min(bestValue, child.minimax(depth+1,alpha,beta));
	    		// If the best value is lower than beta set new beta
	    		beta = Math.min(beta, bestValue);
	    		// Pruning
	    		if(beta <= alpha) break;
	    	}
	    }
	    return depth == 0 ? bestIndex : bestValue;
	}
	
	/*
	 * Getters
	 */
	public String getBoardState() {
		return boardState;
	}
	
	public BoardAction getBoardAction() {
		return boardAction;
	}
	
	public int getValue() {
		return value;
	}
	
	/*
	 * Hash and Equals
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardState == null) ? 0 : boardState.hashCode());
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
		TreeNode other = (TreeNode) obj;
		if (boardState == null) {
			if (other.boardState != null)
				return false;
		} else if (!boardState.equals(other.boardState))
			return false;
		return true;
	}
	
}
