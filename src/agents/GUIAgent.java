package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import model.actions.BoardAction;
import view.GameClient;

public class GUIAgent extends Agent{
	
	private static final long serialVersionUID = -7768223334635651294L;

	private GameClient gui;
	
	public GUIAgent(GameClient gui) {
		super();
		this.gui = gui;
	}
	
	public void turnChanged(String newGameState) {
		this.addBehaviour(new OneShotBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				// Send message to the logger
				ACLMessage log = new ACLMessage(ACLMessage.INFORM);
				log.addReceiver(new AID("LoggerAgent",AID.ISLOCALNAME));
				log.setContent("Requesting the Minimax Agent for a move.");
				send(log);
				// Minimax Agent AID
				AID aidMinimax = new AID("MinimaxAgent", AID.ISLOCALNAME);
				// Prepare a new message to send
				ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				// Set the receiver
				message.addReceiver(aidMinimax);
				// Set the content (the new board state)
				message.setContent(newGameState);
				// Send the message
				send(message);
				// Wait for a response
				ACLMessage response = myAgent.blockingReceive();
				try {
					BoardAction action = (BoardAction) response.getContentObject();
					gui.performBoardAction(action);
				} catch (UnreadableException e) {
					ACLMessage error = new ACLMessage(ACLMessage.FAILURE);
					error.addReceiver(new AID("LoggerAgent",AID.ISLOCALNAME));
					error.setContent("Received unreadable response from the Minimax Agent!");
					send(error);
				}
			}
			
		});
	}
	
	public void settingsChanged(int maxDepth) {
		this.addBehaviour(new OneShotBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				message.addReceiver(new AID("MinimaxAgent", AID.ISLOCALNAME));
				message.setContent(String.valueOf(maxDepth));
				myAgent.send(message);
			}
			
		});
	}
}
