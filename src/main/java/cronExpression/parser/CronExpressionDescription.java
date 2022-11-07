package cronExpression.parser;

import cronExpression.enums.OutputFieldName;
import cronExpression.exceptions.IncorrectArgumentsException;
import cronExpression.exceptions.InvalidInputException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static cronExpression.errorMessages.ErrorMessages.*;

/*
 Author: anubhavh
 */
public class CronExpressionDescription {
    private final String[] expressionParts;

    public static final String SPACE = " ";

    // Used for formatting output so that first 14 columns are taken by fieldName
    private static final String OUTPUT_FORMAT_STRING = "%-14s";

    /*
        Splits the input cron expression by SPACE and throws exception if number of arguments passed is less than or more than 6
        Input: CronExpression
     */
    public CronExpressionDescription(CronExpression cronExpression) throws IncorrectArgumentsException {
        this.expressionParts = cronExpression.getExpression().split(SPACE);
        if(this.expressionParts.length != 6) {
            throw new IncorrectArgumentsException(INCORRECT_ARGUMENTS);
        }
        // Converting any occurrence of */1 to * since they are same in result
        for(int i = 0; i < expressionParts.length; i++) {
            if(expressionParts[i].contentEquals("*/1")) {
                expressionParts[i] = "*";
            }
        }
    }

    /*
        Output format is defined as minute, hour, dayOfMonth, month, dayOfWeek and command and
        corresponding index of array is 0, 1, 2, 3, 4, 5
        No validation is done on command passed in input hence separating its function to getCommandDescription
     */
    public String getDescription() throws InvalidInputException {
        List<OutputFieldName> outputFieldNameList = Arrays.asList(
                OutputFieldName.MINUTE, OutputFieldName.HOUR, OutputFieldName.DAY_OF_MONTH, OutputFieldName.MONTH,
                OutputFieldName.DAY_OF_WEEK
        );

        StringBuilder descriptionBuilder = new StringBuilder();

        for(int i = 0; i < outputFieldNameList.size(); i++) {
            descriptionBuilder.append(getExpressionPartDescription(expressionParts[i], outputFieldNameList.get(i)));
        }
        descriptionBuilder.append(getCommandDescription(expressionParts));

        return descriptionBuilder.toString();
    }

    /*
        Input: 1) String expressionPart: refers to part of input expression mapped to fieldName
               2) OutputFieldName: specific part of cron expression in reference to input expression
        Gets description of input expression part after checking if it contains standard special characters
        (*, * /, -, ,)
        Function calls each individual function for special characters
     */
    private String getExpressionPartDescription(String expressionPart, OutputFieldName outputFieldName) throws InvalidInputException, NumberFormatException {
        StringBuilder expressionPartExpanded = new StringBuilder();

        if(expressionPart.contains("*/")) {
            // Ignoring ${number}/${number} as it is not standard
            expressionPartExpanded.append(expressionForAsteriskSlash(expressionPart, outputFieldName));
        } else if(expressionPart.contains("*")) {
            expressionPartExpanded.append(expressionForAsterisk(expressionPart, outputFieldName));
        } else if(expressionPart.contains(",")) {
            expressionPartExpanded.append(expressionForComma(expressionPart, outputFieldName));
        } else if(expressionPart.contains("-")) {
            expressionPartExpanded.append(expressionForHyphen(expressionPart, outputFieldName));
        } else {
            try {
                int begin = outputFieldName.getBegin();
                int end = outputFieldName.getEnd();
                int intExpressionPart = Integer.parseInt(expressionPart);
                if(begin <= intExpressionPart && end >= intExpressionPart) {
                    expressionPartExpanded.append(expressionPart);
                } else {
                    throw new InvalidInputException(
                            MessageFormat.format(
                                    INVALID_RANGE, outputFieldName.getName(),
                                    begin, end
                            )
                    );
                }
            } catch (NumberFormatException ex) {
                throw new InvalidInputException(
                        MessageFormat.format(
                                INVALID_RANGE, outputFieldName.getName(),
                                outputFieldName.getBegin(), outputFieldName.getEnd()
                        )
                );
            }
        }

        return buildDescription(outputFieldName.getName(), expressionPartExpanded.toString());
    }

    /*
        * / ${number} represent steps of ${number} within range of OutputFieldName
        Ex: * / 2 denotes every 2nd day of the week when OutputFieldName is dayOfWeek
     */
    private StringBuilder expressionForAsteriskSlash(String expressionPart, OutputFieldName outputFieldName) throws InvalidInputException {
        int begin = outputFieldName.getBegin();
        int end = outputFieldName.getEnd();
        StringBuilder expressionPartExpanded = new StringBuilder();

        String[] parts = expressionPart.split("\\*/");

        // First part after split should be empty as expected format is of form */${number}
        if(parts.length == 2 && parts[0].length() == 0 && parts[1].length() > 0) {
            try {
                int increment = Integer.parseInt(parts[1]);
                for (int i = begin; i <= end; i = i + increment) {
                    expressionPartExpanded.append(i).append(" ");
                }
            } catch (NumberFormatException exception) {
                // Adding try catch block to catch any exceptions thrown from Integer.parseInt(parts[1])
                // Possible exceptions can occur if alphabets are passed which are invalid as per range
                throw new InvalidInputException(
                        MessageFormat.format(
                                INVALID_RANGE, outputFieldName.getName(),
                                begin, end
                        )
                );
            }
        } else {
            throw new InvalidInputException(
                    MessageFormat.format(
                            INVALID_RANGE, outputFieldName.getName(),
                            begin, end
                    )
            );
        }
        return expressionPartExpanded;
    }

    /*
        Function for special character '*' where all values in outputFieldName range gets printed
     */
    private StringBuilder expressionForAsterisk(String expressionPart, OutputFieldName outputFieldName) throws InvalidInputException{
        StringBuilder expressionPartExpanded = new StringBuilder();
        int begin = outputFieldName.getBegin();
        int end = outputFieldName.getEnd();

        if(expressionPart.length() == 1) {
            for(int i = begin; i <= end; i++) {
                expressionPartExpanded.append(i).append(" ");
            }
        } else {
            throw new InvalidInputException(
                    MessageFormat.format(
                            INVALID_RANGE, outputFieldName.getName(),
                            begin, end
                    )
            );
        }
        return expressionPartExpanded;
    }

    /*
        Function for special character ',' where input data in range of outputFieldName are printed else error is thrown
     */
    private StringBuilder expressionForComma(String expressionPart, OutputFieldName outputFieldName) throws InvalidInputException {
        StringBuilder expressionPartExpanded = new StringBuilder();
        int begin = outputFieldName.getBegin();
        int end = outputFieldName.getEnd();
        String[] parts = expressionPart.split(",");
        if(parts.length == 2) {
            try {
                int startPart = Integer.parseInt(parts[0]);
                int endPart = Integer.parseInt(parts[1]);
                if(begin <= startPart && begin <= endPart && end >= startPart && end >= endPart) {
                    expressionPartExpanded.append(startPart).append(" ").append(endPart);
                } else {
                    throw new InvalidInputException(
                            MessageFormat.format(
                                    INVALID_RANGE_START_END, outputFieldName.getName(),
                                    startPart, endPart, begin, end
                            )
                    );
                }
            } catch (NumberFormatException numberFormatException) {
                throw new InvalidInputException(
                        MessageFormat.format(
                                INVALID_RANGE, outputFieldName.getName(),
                                begin, end
                        )
                );
            }
        } else {
            throw new InvalidInputException(
                    MessageFormat.format(
                            INVALID_RANGE, outputFieldName.getName(),
                            begin, end
                    )
            );
        }
        return expressionPartExpanded;
    }

    /*
        Function for special character '-' where input range in range of outputFieldName are printed else error is thrown
     */
    private StringBuilder expressionForHyphen(String expressionPart, OutputFieldName outputFieldName) throws InvalidInputException {
        StringBuilder expressionPartExpanded = new StringBuilder();
        int begin = outputFieldName.getBegin();
        int end = outputFieldName.getEnd();

        // Acceptable format: ${number}-${number} in range for field
        String[] parts = expressionPart.split("-");
        if(parts.length == 2) {
            try {
                int startPart = Integer.parseInt(parts[0]);
                int endPart = Integer.parseInt(parts[1]);
                if(startPart > endPart || startPart < begin || startPart > end
                        || endPart < (begin + 1) || endPart > end) {
                    throw new InvalidInputException(
                            MessageFormat.format(
                                    INVALID_RANGE_START_END, outputFieldName.getName(), startPart, endPart,
                                    begin, end
                            )
                    );
                }
                for(int i = startPart; i <= endPart; i++) {
                    expressionPartExpanded.append(i).append(" ");
                }
            } catch (NumberFormatException numberFormatException) {
                throw new InvalidInputException(
                        MessageFormat.format(
                                INVALID_RANGE, outputFieldName.getName(),
                                begin, end
                        )
                );
            }
        } else {
            throw new InvalidInputException(
                    MessageFormat.format(
                            INVALID_RANGE, outputFieldName.getName(),
                            begin, end
                    )
            );
        }
        return expressionPartExpanded;
    }

    private String getCommandDescription(String[] expressionParts) {
        // Command should be present in the last place in the provided input
        final String COMMAND = "command";
        return String.format(OUTPUT_FORMAT_STRING, COMMAND) + expressionParts[5] + System.lineSeparator();
    }

    private String buildDescription(String outputFieldName, String toAppend) {
        return String.format(OUTPUT_FORMAT_STRING, outputFieldName) + toAppend.trim() + System.lineSeparator();
    }
}