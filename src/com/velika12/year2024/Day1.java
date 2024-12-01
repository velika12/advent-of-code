package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day1 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day1.txt"))) {

            LinkedList<Integer> left = new LinkedList<>();
            LinkedList<Integer> right = new LinkedList<>();

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] s = str.split("   ");
                left.add(Integer.parseInt(s[0]));
                right.add(Integer.parseInt(s[1]));
            }

            left.sort(Comparator.naturalOrder());
            right.sort(Comparator.naturalOrder());

            System.out.println("left: " + left);
            System.out.println("right: " + right);

            long totalDistance = IntStream.range(0, left.size())
                    .map(i -> Math.abs(left.get(i) - right.get(i)))
                    .sum();

            System.out.println("Total distance: " + totalDistance);

            Map<Integer, Long> rightDictionary = right.stream().collect(
                    Collectors.groupingBy(Function.identity(), Collectors.counting()));

            long similarityScore = left.stream()
                    .filter(rightDictionary::containsKey)
                    .mapToLong(e -> e * rightDictionary.get(e))
                    .sum();

            System.out.println("Similarity score: " + similarityScore);
        }
    }
}
