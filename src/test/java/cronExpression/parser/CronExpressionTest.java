package cronExpression.parser;

import cronExpression.exceptions.EmptyExpressionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cronExpression.errorMessages.ErrorMessages.EMPTY_EXPRESSION;

class CronExpressionTest {

    @Test
    void testValidCronExpression() throws EmptyExpressionException {
        /* Given */
        String expression = "*/3 2 * * 2-3 /usr";

        /* When */
        CronExpression cronExpression = new CronExpression(expression);

        /* Then */
        Assertions.assertNotNull(cronExpression.getExpression());
        Assertions.assertEquals(expression, cronExpression.getExpression());
    }

    @Test
    void testEmptyExpressionExceptionInCronExpression() {
        // checking for null expression
        /* When */
        Exception exception = Assertions.assertThrows(EmptyExpressionException.class, () -> new CronExpression(null));

        /* Then */
        Assertions.assertEquals(EMPTY_EXPRESSION, exception.getMessage());

        // checking for empty expression
        /* When */
        exception = Assertions.assertThrows(EmptyExpressionException.class, () -> new CronExpression(""));

        /* Then */
        Assertions.assertEquals(EMPTY_EXPRESSION, exception.getMessage());

        // checking for blank expression
        /* When */
        exception = Assertions.assertThrows(EmptyExpressionException.class, () -> new CronExpression(" "));

        /* Then */
        Assertions.assertEquals(EMPTY_EXPRESSION, exception.getMessage());
    }

    @Test
    void testValidCronExpressionWithTrim() throws EmptyExpressionException {
        /* Given */
        String expression = "  */3 2 * * 2-3 /usr  ";

        /* When */
        CronExpression cronExpression = new CronExpression(expression);

        /* Then */
        Assertions.assertNotNull(cronExpression.getExpression());
        Assertions.assertEquals(expression.trim(), cronExpression.getExpression());
    }

}
