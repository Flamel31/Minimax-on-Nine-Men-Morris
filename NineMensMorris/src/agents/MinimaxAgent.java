package agents;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.minimax.TreeNode;

public class MinimaxAgent extends Agent{

	private static final long serialVersionUID = -7293167382262244598L;
	private static final MessageTemplate template = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM),MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
	private TreeNode minimaxTree;
	private int maxDepth = 3;
	
	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new CyclicBehaviour(this) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void action() {
				ACLMessage message = receive(template);
				if(message != null) {
					if(message.getPerformative() == ACLMessage.INFORM) {
						maxDepth = Integer.valueOf(message.getContent());
						// Send message to the logger
						ACLMessage log = new ACLMessage(ACLMessage.INFORM);
						log.addReceiver(new AID("LoggerAgent",AID.ISLOCALNAME));
						log.setContent("Max depth of the tree changed to "+maxDepth+".");
						send(log);
					}else if(message.getPerformative() == ACLMessage.REQUEST) {
						String boardState = message.getContent();
						// If the tree has been already constructed try to move to the new root
						minimaxTree = (minimaxTree != null) ? minimaxTree.getChildNode(boardState) : null;
						// Build the tree if the new root was not found or the tree was not initialized yet
						if(minimaxTree == null) {
							minimaxTree = new TreeNode(boardState);
							// Send message to the logger
							ACLMessage log = new ACLMessage(ACLMessage.INFORM);
							log.addReceiver(new AID("LoggerAgent",AID.ISLOCALNAME));
							log.setContent("New minimax tree builded of depth "+maxDepth+".");
							send(log);
						}
						// Compute child nodes up to a certain depth
						minimaxTree.generateChildNodes(maxDepth);
						// Add OneShot Behaviour that evaluate the optimal board action and
						// send it back to the GUI Agent.
						myAgent.addBehaviour(new OneShotBehaviour(myAgent) {
							
							private static final long serialVersionUID = 1L;
							
							@Override
							public void action() {
								// Evaluate the optimal node for the current depth
								TreeNode optimalNode = minimaxTree.minimax();
								try {
									// Prepare the message
									ACLMessage replyMessage = new ACLMessage(ACLMessage.INFORM);
									replyMessage.addReceiver(message.getSender());
									replyMessage.setContentObject(optimalNode.getBoardAction());
									// Send message
									send(replyMessage);
								} catch (IOException e) {
									ACLMessage error = new ACLMessage(ACLMessage.FAILURE);
									error.addReceiver(new AID("LoggerAgent",AID.ISLOCALNAME));
									error.setContent("Unable to serialize the optimal board action: "+optimalNode.getBoardAction());
									send(error);
								}
								// The new root is the optimal node
								minimaxTree = optimalNode;
							}
							
						} );
					}
					
				}else {
					block();
				}
			}
			
		});
	}
}
