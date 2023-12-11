package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day6 {

    public static void main(String[] args) throws FileNotFoundException {

        // part 1
        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day6.txt"))) {

            String str = scanner.nextLine();
            str = str.replaceFirst("Time:\\s+", "");
            List<Long> times = Arrays.stream(str.split("\\s+")).map(Long::parseLong).toList();

            str = scanner.nextLine();
            str = str.replaceFirst("Distance:\\s+", "");
            List<Long> distances = Arrays.stream(str.split("\\s+")).map(Long::parseLong).toList();

            long multiplyBeat = 1;

            for (int raceNum = 0; raceNum < times.size(); raceNum++) {
                Long time = times.get(raceNum);
                Long distance = distances.get(raceNum);

                multiplyBeat *= calculateBeatCounter(time, distance);
            }

            System.out.println("Multiply beat counters: " + multiplyBeat);
        }

        // part 2
        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day6.txt"))) {

            String str = scanner.nextLine();
            str = str.replaceFirst("Time:\\s+", "");
            str = str.replaceAll("\\s+", "");
            Long time = Long.parseLong(str);

            str = scanner.nextLine();
            str = str.replaceFirst("Distance:\\s+", "");
            str = str.replaceAll("\\s+", "");
            Long distance = Long.parseLong(str);

            long multiplyBeat = calculateBeatCounter(time, distance);

            System.out.println("Multiply beat counters: " + multiplyBeat);
        }
    }

    private static long calculateBeatCounter(long time, long distance) {
        long beatCounter = 0;

        for (long v = 1; v < time; v++) {
            long s = (time - v) * v;

            if (s > distance) {
                beatCounter++;
            }
        }

        return beatCounter;
    }
}