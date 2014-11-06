package core.dataStructure.roundRobin.exceptions;

public class RoundRobinIllegalAccessException extends RuntimeException {

	private static final long serialVersionUID = -6965135471694836059L;
	
	private String message = "";
	
	public RoundRobinIllegalAccessException(String message) {
		this.message = message;
	}
	
	public RoundRobinIllegalAccessException() {
		
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
