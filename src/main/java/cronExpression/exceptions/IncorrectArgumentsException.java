package cronExpression.exceptions;

public class IncorrectArgumentsException extends Exception {
	private final String message;
	
	public IncorrectArgumentsException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
