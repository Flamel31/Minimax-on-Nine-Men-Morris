package agents;

public interface LoggerOutput {
	/*
	 * Log the information received as argument.
	 */
	public void logMessage(String who,String message);
	
	/*
	 * Log an error received as argument.
	 */
	public void logError(String who, String error);
}
