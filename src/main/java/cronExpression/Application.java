package cronExpression;

import cronExpression.parser.CronExpression;
import cronExpression.parser.CronExpressionDescription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
		String expression = args[0];
        try {
        	CronExpression cronExpression = new CronExpression(expression);
			CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);
			logger.info(cronExpressionDescription.getDescription());
		} catch (Exception exception) {
			// Catches NumberFormatException, IncorrectArgumentsException and EmptyExpressionException
			// Have different exceptions here if operations performed on them are different
			logger.error(exception.getMessage());
		}
	}
}