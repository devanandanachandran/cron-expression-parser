# Cron Expression Parser

This is a Java command-line program that parses a cron expression and expands its five time fields into readable values.

## Requirements
Java JDK 8 or later.

## How to Run

Compile the files:

    javac -d out src/CronParser.java src/CronParserTest.java

Run the program:

    java -cp out CronParser "*/15 0 1,15 * 1-5 /usr/bin/find"

## Running Tests

Run the test file:

    java -cp out CronParserTest

The tests cover wildcards, ranges, steps, comma-separated values, duplicate values and invalid inputs.

## Approach

The program separates the cron expression into five time fields and a command. It expands each field based on its allowed range.

It supports wildcards (*), ranges (-), steps (/) and comma-separated values (,). A TreeSet is used to sort the values and remove duplicates.

## Assumptions

- The input contains five time fields followed by a command.
- Day of week uses 0 for Sunday and 6 for Saturday.
- The command is displayed but not executed.

## Limitations

- Named months and weekdays are not supported.
- Cron shortcuts such as @yearly are not supported.
- Special expressions such as L, W and # are not supported.
- The program expands fields but does not calculate actual execution times.