package cronExpression.errorMessages;

public class ErrorMessages {
    public static final String INVALID_RANGE_START_END = "Incorrect range passed for {0}. {1} and {2} are not as per supported format. Acceptable Range is from {3} to {4}. Please refer to README.md for supported inputs.";

    public static final String INVALID_RANGE = "Incorrect data passed for {0}. Acceptable range is from {1} to {2}. Please refer to README.md for supported inputs.";

    public static final String EMPTY_EXPRESSION = "No cron expression passed. Please check README.md for supported inputs.";

    public static final String INCORRECT_ARGUMENTS = "Number of fields present are less than 6. Please check README.md for supported inputs.";

    private ErrorMessages() {}
}
