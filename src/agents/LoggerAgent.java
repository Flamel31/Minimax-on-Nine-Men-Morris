package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class LoggerAgent extends Agent{
	private static final long serialVersionUID = 1502013469363685450L;

	// Template of the message it should Log
	private static final MessageTemplate template = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchPerformative(ACLMessage.FAILURE)); 
	// Where the logger should log the message
	private LoggerOutput loggerOutput;
	
	
	public LoggerAgent(LoggerOutput loggerOutput) {
		this.loggerOutput = loggerOutput;
	}
	
	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new CyclicBehaviour(this) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void action() {
				ACLMessage message = receive(template);
				if(message != null && loggerOutput != null) {
					if(message.getPerformative() == ACLMessage.FAILURE) {
						loggerOutput.logError(message.getSender().getName(),message.getContent());
					}else {
						loggerOutput.logMessage(message.getSender().getName(),message.getContent());
					}
					
				}else {
					block();
				}
			}
		});
	}

}
