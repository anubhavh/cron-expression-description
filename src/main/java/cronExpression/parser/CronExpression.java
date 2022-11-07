package cronExpression.parser;

import cronExpression.exceptions.EmptyExpressionException;

import static cronExpression.errorMessages.ErrorMessages.EMPTY_EXPRESSION;

public class CronExpression {
    private final String expression;
    
    public CronExpression(String expression) throws EmptyExpressionException {
		// StringUtil's isBlank can also be used here
		if(null == expression || expression.trim().length() == 0) {
			throw new EmptyExpressionException(EMPTY_EXPRESSION);
		}
		// Trim removes any extra white spaces
		this.expression = expression.trim();
    }

	public String getExpression() {
		return this.expression;
	}
}