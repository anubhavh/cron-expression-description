package cronExpression.exceptions;

public class EmptyExpressionException extends Exception {
	private final String message;
	
	public EmptyExpressionException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}