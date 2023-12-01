package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Day1 {

    private static final Map<String, Character> spelledDigits = Map.of(
            "one", '1',
            "two", '2',
            "three", '3',
            "four", '4',
            "five", '5',
            "six", '6',
            "seven", '7',
            "eight", '8',
            "nine", '9'
    );

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day1.txt"))) {

            int calibrationSum = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
//                System.out.print(str.toCharArray());
//                System.out.print(" = ");
//                System.out.println(findCalibrationValue(str.toCharArray()));

                calibrationSum += findCalibrationValue(str.toCharArray());
            }

            System.out.println("Sum of calibration values: " + calibrationSum);
        }
    }

    private static Integer findCalibrationValue(char[] chars) {
        LinkedList<Character> digits = new LinkedList<>();

        int currentIndex = 0;

        while (currentIndex < chars.length) {
            char c = chars[currentIndex];

            if (Character.isDigit(c)) {
                digits.add(c);
            } else {
                String spelledDigit = detectSpelledDigit(chars, currentIndex);
                if (spelledDigit != null) {
                    digits.add(spelledDigits.get(spelledDigit));
                    currentIndex += (spelledDigit.length() - 1);
                }
            }

            currentIndex++;
        }

        return Integer.parseInt(String.valueOf(new char[] { digits.getFirst(), digits.getLast() }));
    }

    private static String detectSpelledDigit(char[] chars, int startIndex) {
        StringBuilder sb = new StringBuilder();

        for (int i = startIndex; i < chars.length; i++) {
            sb.append(chars[i]);

            String candidate = sb.toString();

            if (!isSpelledDigitStart(candidate)) {
                return null;
            }

            if (spelledDigits.containsKey(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private static boolean isSpelledDigitStart(String value) {
        return spelledDigits.keySet().stream().anyMatch(spelledDigit -> spelledDigit.startsWith(value));
    }
}
