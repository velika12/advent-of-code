package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {

    private static final Pattern INPUT_PATTERN = Pattern.compile("Sensor at x=(-{0,1}\\d+), y=(-{0,1}\\d+): closest beacon is at x=(-{0,1}\\d+), y=(-{0,1}\\d+)");

    private static final int TARGET_Y = 2000000;

    public static void main(String[] args) throws FileNotFoundException {
        Set<Integer> beaconsOnTarget = new HashSet<>();
        Set<Integer> intersectionsOnX = new HashSet<>();

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day15.txt"))) {
            while (scanner.hasNextLine()) {
                //System.out.println();
                String line = scanner.nextLine();
                Matcher matcher = INPUT_PATTERN.matcher(line);
                if (matcher.matches()) {
                    int sensorX = Integer.parseInt(matcher.group(1));
                    int sensorY = Integer.parseInt(matcher.group(2));
                    int beaconX = Integer.parseInt(matcher.group(3));
                    int beaconY = Integer.parseInt(matcher.group(4));

                    //System.out.println(sensorX + " " + sensorY + " " + beaconX + " " + beaconY);

                    if (beaconY == TARGET_Y) {
                        beaconsOnTarget.add(beaconX);
                    }

                    int r = Math.abs(sensorX - beaconX) + Math.abs(sensorY - beaconY);

                    if (Math.abs(sensorY - TARGET_Y) <= r) {
                        int n = r - Math.abs(sensorY - TARGET_Y);

                        //System.out.println("r=" + r);
                        //System.out.println("n=" + n);

                        for (int x = sensorX - n; x <= sensorX + n; x++) {
                            //System.out.println("x=" + x);
                            intersectionsOnX.add(x);
                        }
                    }
                }
            }
        }

        System.out.println("intersectionsOnX: " + intersectionsOnX);
        System.out.println("beaconsOnTarget: " + beaconsOnTarget);

        intersectionsOnX.removeAll(beaconsOnTarget);

        System.out.println("Positions without a beacon on row y=" + TARGET_Y + ": " + intersectionsOnX.size());
    }
}
