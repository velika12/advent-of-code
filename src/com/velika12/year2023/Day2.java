package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 {

    private static final Map<String, Integer> colorMap = Map.of(
            "red", 12,
            "green", 13,
            "blue", 14
    );

    private static final Map<String, Pattern> colorPatternMap = colorMap.keySet().stream()
            .collect(Collectors.toMap(color -> color, color -> Pattern.compile("\\d+ " + color)));

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day2.txt"))) {
            int sumOfPossibleGameIds = 0;
            int sumOfPowers = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] parts = str.split(": ");

                if (isGamePossible(parts[1])) {
                    sumOfPossibleGameIds += extractGameId(parts[0]);
                }

                sumOfPowers += calculatePowerOfMinPossibleSetOfCubes(parts[1]);
            }

            System.out.println("Sum of possible game ids: " + sumOfPossibleGameIds);
            System.out.println("Sum of powers: " + sumOfPowers);
        }
    }

    private static int calculatePowerOfMinPossibleSetOfCubes(String gameStr) {
        return colorPatternMap.values().stream()
                .map(colorPattern -> findMaxNumOfCubesForColor(gameStr, colorPattern))
                .reduce((num1, num2) -> num1 * num2)
                .orElse(0);
    }

    private static boolean isGamePossible(String gameStr) {
        for (Map.Entry<String, Integer> colorEntry : colorMap.entrySet()) {
            if (!checkColor(gameStr, colorPatternMap.get(colorEntry.getKey()), colorEntry.getValue())) {
                return false;
            }
        }

        return true;
    }

    private static int findMaxNumOfCubesForColor(String gameStr, Pattern pattern) {
        Matcher matcher  = pattern.matcher(gameStr);
        int max = 0;

        while (matcher.find()) {
            String cubeStr = matcher.group();
            String[] parts = cubeStr.split(" ");
            int numOfCubes = Integer.parseInt(parts[0]);

            if (numOfCubes > max) {
                max = numOfCubes;
            }
        }

        return max;
    }

    private static boolean checkColor(String gameStr, Pattern pattern, int limit) {
        Matcher matcher  = pattern.matcher(gameStr);
        while (matcher.find()) {
            String cubeStr = matcher.group();
            String[] parts = cubeStr.split(" ");
            int numOfCubes = Integer.parseInt(parts[0]);

            if (numOfCubes > limit) {
                return false;
            }
        }

        return true;
    }

    private static Integer extractGameId(String titleStr) {
        String[] parts = titleStr.split(" ");
        return Integer.parseInt(parts[1]);
    }
}
