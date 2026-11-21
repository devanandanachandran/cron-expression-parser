import java.util.TreeSet;
import java.util.Set;

public class CronParser {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println(
                "Usage: java CronParser \"<cron expression>\""
            );
            System.exit(1);
        }

        try {
            String cronExpression = args[0].trim();

            
            String[] parts = cronExpression.split("\\s+", 6);

            if (parts.length != 6) {
                throw new IllegalArgumentException(
                    "Cron expression must contain five time fields and a command."
                );
            }

            String minute = expandField(parts[0], 0, 59);
            String hour = expandField(parts[1], 0, 23);
            String dayOfMonth = expandField(parts[2], 1, 31);
            String month = expandField(parts[3], 1, 12);
            String dayOfWeek = expandField(parts[4], 0, 6);
            String command = parts[5];

            printRow("minute", minute);
            printRow("hour", hour);
            printRow("day of month", dayOfMonth);
            printRow("month", month);
            printRow("day of week", dayOfWeek);
            printRow("command", command);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    
    public static String expandField(
            String field,
            int minimum,
            int maximum) {

        if (field == null || field.trim().isEmpty()) {
            throw new IllegalArgumentException("Cron field cannot be empty.");
        }

        Set<Integer> values = new TreeSet<>();

        String[] parts = field.split(",", -1);

        for (String part : parts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException(
                    "Invalid empty value in field: " + field
                );
            }

            parsePart(part, minimum, maximum, values);
        }

        StringBuilder result = new StringBuilder();

        for (int value : values) {
            if (result.length() > 0) {
                result.append(" ");
            }

            result.append(value);
        }

        return result.toString();
    }

   
    private static void parsePart(
            String part,
            int minimum,
            int maximum,
            Set<Integer> values) {

        String base;
        int step = 1;

        int slashIndex = part.indexOf('/');

        if (slashIndex != -1) {

           
            if (slashIndex != part.lastIndexOf('/')) {
                throw new IllegalArgumentException(
                    "Invalid step expression: " + part
                );
            }

            base = part.substring(0, slashIndex);
            String stepText = part.substring(slashIndex + 1);

            step = parseNumber(stepText, "step");

            if (step <= 0) {
                throw new IllegalArgumentException(
                    "Step must be greater than zero: " + part
                );
            }

        } else {
            base = part;
        }

        int start;
        int end;

        if (base.equals("*")) {

            start = minimum;
            end = maximum;

        } else if (base.contains("-")) {

            
            if (base.indexOf('-') != base.lastIndexOf('-')) {
                throw new IllegalArgumentException(
                    "Invalid range: " + part
                );
            }

            String[] range = base.split("-", -1);

            if (range.length != 2 ||
                range[0].isEmpty() ||
                range[1].isEmpty()) {

                throw new IllegalArgumentException(
                    "Invalid range: " + part
                );
            }

            start = parseNumber(range[0], "range start");
            end = parseNumber(range[1], "range end");

            validateRange(start, minimum, maximum);
            validateRange(end, minimum, maximum);

            if (start > end) {
                throw new IllegalArgumentException(
                    "Range start cannot exceed range end: " + part
                );
            }

        } else {

            
            start = parseNumber(base, "value");
            validateRange(start, minimum, maximum);

            if (slashIndex != -1) {
                
                end = maximum;
            } else {
                end = start;
            }
        }

        
        for (int value = start; value <= end; ) {

            values.add(value);

            
            if (end - value < step) {
                break;
            }

            value += step;
        }
    }

    
    private static int parseNumber(String text, String description) {

        if (text == null || !text.matches("\\d+")) {
            throw new IllegalArgumentException(
                "Invalid " + description + ": " + text
            );
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Number is too large: " + text
            );
        }
    }

    
    private static void validateRange(
            int value,
            int minimum,
            int maximum) {

        if (value < minimum || value > maximum) {
            throw new IllegalArgumentException(
                "Value " + value +
                " is outside the allowed range " +
                minimum + "-" + maximum
            );
        }
    }

    
    private static void printRow(String fieldName, String value) {

        System.out.printf("%-14s%s%n", fieldName, value);
    }
}