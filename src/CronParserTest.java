public class CronParserTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        // Wildcard
        check(
            "Wildcard",
            "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59",
            CronParser.expandField("*", 0, 59)
        );

        // Step
        check(
            "Step",
            "0 15 30 45",
            CronParser.expandField("*/15", 0, 59)
        );

        // Range
        check(
            "Range",
            "1 2 3 4 5",
            CronParser.expandField("1-5", 0, 59)
        );

        // Comma-separated values
        check(
            "Comma",
            "1 5 10",
            CronParser.expandField("1,5,10", 0, 59)
        );

        // Range with step
        check(
            "Range with step",
            "1 3 5 7 9",
            CronParser.expandField("1-10/2", 0, 59)
        );

        // Single value
        check(
            "Single value",
            "5",
            CronParser.expandField("5", 0, 59)
        );

        // Single value with step
        check(
            "Single value with step",
            "5 15 25 35 45 55",
            CronParser.expandField("5/10", 0, 59)
        );

        // Duplicate values
        check(
            "Duplicate values",
            "1 3 5",
            CronParser.expandField("5,1,5,3", 0, 59)
        );

        // Day of month range
        check(
            "Day of month",
            "1 15",
            CronParser.expandField("1,15", 1, 31)
        );

        // Month wildcard
        check(
            "Month wildcard",
            "1 2 3 4 5 6 7 8 9 10 11 12",
            CronParser.expandField("*", 1, 12)
        );

        // Invalid inputs
        checkInvalid("Out of range", "70", 0, 59);
        checkInvalid("Invalid month", "13", 1, 12);
        checkInvalid("Zero step", "*/0", 0, 59);
        checkInvalid("Descending range", "5-1", 0, 59);
        checkInvalid("Empty component", "1,,5", 0, 59);
        checkInvalid("Invalid number", "abc", 0, 59);
        checkInvalid("Multiple slashes", "*/2/3", 0, 59);
        checkInvalid("Missing range value", "1-", 0, 59);

        System.out.println();
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(
            String testName,
            String expected,
            String actual) {

        if (expected.equals(actual)) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            System.out.println("Expected: " + expected);
            System.out.println("Actual:   " + actual);
            failed++;
        }
    }

    private static void checkInvalid(
            String testName,
            String input,
            int minimum,
            int maximum) {

        try {
            CronParser.expandField(input, minimum, maximum);

            System.out.println("[FAIL] " + testName);
            System.out.println("Expected an exception.");
            failed++;

        } catch (IllegalArgumentException e) {
            System.out.println("[PASS] " + testName);
            passed++;
        }
    }
}