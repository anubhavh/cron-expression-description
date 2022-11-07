package cronExpression.exceptions;

public class InvalidInputException extends Exception {
    private final String message;

    public InvalidInputException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
