package cronExpression.parser;

import cronExpression.enums.OutputFieldName;
import cronExpression.exceptions.EmptyExpressionException;
import cronExpression.exceptions.IncorrectArgumentsException;
import cronExpression.exceptions.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static cronExpression.errorMessages.ErrorMessages.*;

class CronExpressionDescriptionTest {

    @Test
    void testValidCronExpressionForCronExpressionDescription()
            throws IncorrectArgumentsException, EmptyExpressionException {
        /* Given */
        CronExpression cronExpression = new CronExpression("*/3 2 * * 2-3 /usr");

        /* When */
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* Then */
        Assertions.assertNotNull(cronExpressionDescription);
    }

    @Test
    void testIncorrectArgumentsExceptions() {
        /* When */
        Exception exception = Assertions.assertThrows(IncorrectArgumentsException.class,
                () -> new CronExpressionDescription(new CronExpression("*/3 2 * * 2-3 /usr /usr")));

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(INCORRECT_ARGUMENTS, exception.getMessage());

        /* When */
        exception = Assertions.assertThrows(IncorrectArgumentsException.class,
                () -> new CronExpressionDescription(new CronExpression("*/3 2 * * 2-3")));

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(INCORRECT_ARGUMENTS, exception.getMessage());
    }

    @Test
    void testGetDescription() throws InvalidInputException, EmptyExpressionException, IncorrectArgumentsException {
        /* Given */
        String expression = "*/30 20 1-3 3 2-3 /usr";

        CronExpression cronExpression = new CronExpression(expression);
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        String expressionDescription = "minute        0 30" + System.lineSeparator() +
                "hour          20" + System.lineSeparator() +
                "day of month  1 2 3" + System.lineSeparator() +
                "month         3" + System.lineSeparator() +
                "day of week   2 3" + System.lineSeparator() +
                "command       /usr" + System.lineSeparator();

        /* When */
        String description = cronExpressionDescription.getDescription();

        /* Then */
        Assertions.assertNotNull(description);
        Assertions.assertEquals(expressionDescription, description);

        /* Given */
        expression = "*/15 0 1,15 * 1-5 /usr/bin/find";

        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        expressionDescription = "minute        0 15 30 45" + System.lineSeparator() +
                "hour          0" + System.lineSeparator() +
                "day of month  1 15" + System.lineSeparator() +
                "month         1 2 3 4 5 6 7 8 9 10 11 12" + System.lineSeparator() +
                "day of week   1 2 3 4 5" + System.lineSeparator() +
                "command       /usr/bin/find" + System.lineSeparator();

        /* When */
        description = cronExpressionDescription.getDescription();

        /* Then */
        Assertions.assertNotNull(description);
        Assertions.assertEquals(expressionDescription, description);
    }

    @Test
    void testExceptionsInGetDescription() throws EmptyExpressionException, IncorrectArgumentsException {
        /* Given */
        String expression = "*/15 59 1,15 * 1-5 /usr/bin/find";

        CronExpression cronExpression = new CronExpression(expression);
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        Exception exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                INVALID_RANGE, OutputFieldName.HOUR.getName(),
                        OutputFieldName.HOUR.getBegin(), OutputFieldName.HOUR.getEnd()
        ), exception.getMessage());

        /* Given */
        expression = "*/15 0 1,15 A 1-5 /usr/bin/find";

        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.MONTH.getName(),
                        OutputFieldName.MONTH.getBegin(), OutputFieldName.MONTH.getEnd()
                ), exception.getMessage());
    }

    @Test
    void testInvalidAsteriskSlash() throws EmptyExpressionException, IncorrectArgumentsException {
        /* Given */
        String expression = "*/A 20 1-3 3 2-3 /usr";

        CronExpression cronExpression = new CronExpression(expression);
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        Exception exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.MINUTE.getName(), OutputFieldName.MINUTE.getBegin(),
                        OutputFieldName.MINUTE.getEnd()
                ), exception.getMessage());

        /* Given */
        expression = "*/*/B 20 1-3 3 2-3 /usr";

        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.MINUTE.getName(), OutputFieldName.MINUTE.getBegin(),
                        OutputFieldName.MINUTE.getEnd()
                ), exception.getMessage());
    }

    @Test
    void testInvalidAsterisk() throws EmptyExpressionException, IncorrectArgumentsException {
        /* Given */
        String expression = "*/30 ** 1-3 3 2-3 /usr";

        CronExpression cronExpression = new CronExpression(expression);
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        Exception exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.HOUR.getName(), OutputFieldName.HOUR.getBegin(),
                        OutputFieldName.HOUR.getEnd()
                ), exception.getMessage());
    }

    @Test
    void testInvalidComma() throws EmptyExpressionException, IncorrectArgumentsException {
        /* Given */
        String expression = "*/3 2 * * ,, /usr";

        CronExpression cronExpression = new CronExpression(expression);
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        Exception exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.DAY_OF_WEEK.getName(), OutputFieldName.DAY_OF_WEEK.getBegin(),
                        OutputFieldName.DAY_OF_WEEK.getEnd()
                ), exception.getMessage());

        /* Given */
        expression = "*/30 20 1,59 3 2-3 /usr";

        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE_START_END, OutputFieldName.DAY_OF_MONTH.getName(), 1, 59,
                        OutputFieldName.DAY_OF_MONTH.getBegin(), OutputFieldName.DAY_OF_MONTH.getEnd()
                ), exception.getMessage());

        /* Given */
        expression = "*/30 20 A,B 3 2-3 /usr";

        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.DAY_OF_MONTH.getName(),
                        OutputFieldName.DAY_OF_MONTH.getBegin(), OutputFieldName.DAY_OF_MONTH.getEnd()
                ), exception.getMessage());
    }

    @Test
    void testInvalidHyphen() throws EmptyExpressionException, IncorrectArgumentsException {
        /* Given */
        String expression = "*/30 20 1-3 -- 2-3 /usr";

        CronExpression cronExpression = new CronExpression(expression);
        CronExpressionDescription cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        Exception exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.MONTH.getName(), OutputFieldName.MONTH.getBegin(),
                        OutputFieldName.MONTH.getEnd()
                ), exception.getMessage());

        /* Given */
        expression = "*/30 20 1-3 3 -1-3 /usr";
        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.DAY_OF_WEEK.getName(), OutputFieldName.DAY_OF_WEEK.getBegin(),
                        OutputFieldName.DAY_OF_WEEK.getEnd()
                ), exception.getMessage());

        /* Given */
        expression = "*/30 20 1-3 3 1-9 /usr";
        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE_START_END, OutputFieldName.DAY_OF_WEEK.getName(), 1, 9,
                        OutputFieldName.DAY_OF_WEEK.getBegin(), OutputFieldName.DAY_OF_WEEK.getEnd()
                ), exception.getMessage());

        /* Given */
        expression = "*/30 20 1-3 3 A-B /usr";
        cronExpression = new CronExpression(expression);
        cronExpressionDescription = new CronExpressionDescription(cronExpression);

        /* When */
        exception = Assertions.assertThrows(InvalidInputException.class,
                cronExpressionDescription::getDescription);

        /* Then */
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(
                MessageFormat.format(
                        INVALID_RANGE, OutputFieldName.DAY_OF_WEEK.getName(), OutputFieldName.DAY_OF_WEEK.getBegin(),
                        OutputFieldName.DAY_OF_WEEK.getEnd()
                ), exception.getMessage());
    }
}
