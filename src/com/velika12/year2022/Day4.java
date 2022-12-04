package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day4 {

    private static boolean fullyContains(Integer[] assignment1, Integer[] assignment2) {
        return assignment1[0] <= assignment2[0] && assignment1[1] >= assignment2[1];
    }

    private static boolean overlap(Integer[] assignment1, Integer[] assignment2) {
        return !(assignment1[1] < assignment2[0] || assignment2[1] < assignment1[0]);
    }

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day4.txt"))) {

            long containsCounter = 0;
            long overlapCounter = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.next();
                String[] assignments = line.split(",");
                Integer[] firstElfAssignment = Arrays.stream(assignments[0].split("-")).map(Integer::parseInt).toArray(Integer[]::new);
                Integer[] secondElfAssignment = Arrays.stream(assignments[1].split("-")).map(Integer::parseInt).toArray(Integer[]::new);

                if (fullyContains(firstElfAssignment, secondElfAssignment) || fullyContains(secondElfAssignment, firstElfAssignment) ) {
                    containsCounter++;
                }

                if (overlap(firstElfAssignment, secondElfAssignment)) {
                    overlapCounter++;
                }
            }

            System.out.println("Pairs with fully contains: " + containsCounter);
            System.out.println("Pairs with overlap: " + overlapCounter);
        }
    }
}
