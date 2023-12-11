package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day4.txt"))) {

            int pointsSum = 0;
            HashMap<Integer, Integer> countMap = new HashMap<>();

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                pointsSum += getCardPoints(str);
                fillCardInstanceCount(str, countMap);
            }

            System.out.println("Sum of all card points: " + pointsSum);

            int totalCount = countMap.values().stream()
                    .reduce(Integer::sum)
                    .get();

            System.out.println("Total card count: " + totalCount);
        }
    }

    private static int getCardPoints(String line) {
        String[] parts = line.split(":\\s+");
        String[] numberParts = parts[1].split("\\s+\\|\\s+");
        String[] winningParts = numberParts[0].split("\\s+");
        String[] givenParts = numberParts[1].split("\\s+");

        Set<Integer> winningNumbers = Arrays.stream(winningParts).map(Integer::parseInt).collect(Collectors.toSet());
        Set<Integer> givenNumbers = Arrays.stream(givenParts).map(Integer::parseInt).collect(Collectors.toSet());

        givenNumbers.retainAll(winningNumbers);

        return givenNumbers.isEmpty() ? 0 : (int) Math.pow(2, givenNumbers.size() - 1);
    }

    private static void fillCardInstanceCount(String line, Map<Integer, Integer> map) {
        String[] parts = line.split(":\\s+");
        String[] cardParts = parts[0].split("\\s+");
        String[] numberParts = parts[1].split("\\s+\\|\\s+");
        String[] winningParts = numberParts[0].split("\\s+");
        String[] givenParts = numberParts[1].split("\\s+");

        int currentCardNumber = Integer.parseInt(cardParts[1]);
        Integer currentCount = map.get(currentCardNumber);
        map.put(currentCardNumber, currentCount == null ? 1 : currentCount + 1);

        Set<Integer> winningNumbers = Arrays.stream(winningParts).map(Integer::parseInt).collect(Collectors.toSet());
        Set<Integer> givenNumbers = Arrays.stream(givenParts).map(Integer::parseInt).collect(Collectors.toSet());

        givenNumbers.retainAll(winningNumbers);

        for (int i = 0; i < map.get(currentCardNumber); i++) {
            for (int cardNumber = currentCardNumber + 1; cardNumber <= currentCardNumber + givenNumbers.size(); cardNumber++) {
                Integer count = map.get(cardNumber);
                map.put(cardNumber, count == null ? 1 : count + 1);
            }
        }
    }
}
