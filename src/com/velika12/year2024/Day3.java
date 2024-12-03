package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    private static boolean instructionsEnabled = true;

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day3.txt"))) {
            long sum = 0;
            long sumWithInstructions = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                sum += scanAndSum(str);
                sumWithInstructions += scanWithInstructionsAndSum(str);
            }

            System.out.println("Sum: " + sum);
            System.out.println("Sum with instructions: " + sumWithInstructions);
        }
    }

    private static long scanAndSum(String str) {
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        long sum = 0;

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            //System.out.println("Found: " + matcher.group(0));
            sum += (long) Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }

        return sum;
    }

    private static long scanWithInstructionsAndSum(String str) {
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");
        long sum = 0;

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String matched = matcher.group(0);
            //System.out.println("Found: " + matched);

            if (matched.startsWith("mul")) {
                if (instructionsEnabled) {
                    sum += (long) Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                }
            } else if (matched.startsWith("don't")) {
                instructionsEnabled = false;
            } else {
                instructionsEnabled = true;
            }
        }

        return sum;
    }
}
