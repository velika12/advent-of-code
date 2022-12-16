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
    private static final int DISTRESS_BEACON_BOUNDARY = 4000000;

    public static void main(String[] args) throws FileNotFoundException {
        Set<Sensor> sensors = new HashSet<>();

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day15.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = INPUT_PATTERN.matcher(line);
                if (matcher.matches()) {
                    Beacon beacon = new Beacon(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
                    Sensor sensor = new Sensor(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), beacon);
                    sensors.add(sensor);
                }
            }
        }

        research1(sensors);
        research2(sensors);
    }

    private static void research1(Set<Sensor> sensors) {
        Set<Integer> beaconsOnTarget = new HashSet<>();
        Set<Integer> intersectionsOnX = new HashSet<>();

        sensors.forEach(sensor -> {
            if (sensor.beacon.y == TARGET_Y) {
                beaconsOnTarget.add(sensor.beacon.x);
            }
            if (sensor.covers(sensor.x, TARGET_Y)) {
                int n = sensor.r - Math.abs(sensor.y - TARGET_Y);

                for (int x = sensor.x - n; x <= sensor.x + n; x++) {
                    intersectionsOnX.add(x);
                }
            }
        });

        intersectionsOnX.removeAll(beaconsOnTarget);

        System.out.println("Positions without a beacon on row y=" + TARGET_Y + ": " + intersectionsOnX.size());
    }

    private static void research2(Set<Sensor> sensors) {
        Beacon distressBeacon = findDistressBeacon(sensors);
        System.out.println("Distress beacon: " + distressBeacon);

        long frequency = distressBeacon.x * 4000000L + distressBeacon.y;
        System.out.println("Frequency: " + frequency);
    }

    private static Beacon findDistressBeacon(Set<Sensor> sensors) {
        for (Sensor sensor : sensors) {
            int x = sensor.x - sensor.r - 1;
            int y = sensor.y;

            while (x < sensor.x) {
                if (isDistressBeacon(x, y, sensor, sensors)) {
                    return new Beacon(x, y);
                }

                x++;
                y++;
            }

            while (x < sensor.x + sensor.r + 1) {
                if (isDistressBeacon(x, y, sensor, sensors)) {
                    return new Beacon(x, y);
                }

                x++;
                y--;
            }

            while (x > sensor.x) {
                if (isDistressBeacon(x, y, sensor, sensors)) {
                    return new Beacon(x, y);
                }

                x--;
                y--;
            }

            while (x > sensor.x - sensor.r - 1) {
                if (isDistressBeacon(x, y, sensor, sensors)) {
                    return new Beacon(x, y);
                }

                x--;
                y++;
            }
        }

        throw new IllegalArgumentException("No beacon found");
    }

    private static boolean isDistressBeacon(int x, int y, Sensor currentSensor, Set<Sensor> sensors) {
        if (x < 0 || x > DISTRESS_BEACON_BOUNDARY || y < 0 || y > DISTRESS_BEACON_BOUNDARY) {
            return false;
        }

        for (Sensor anotherSensor : sensors) {
            if (currentSensor == anotherSensor) {
                continue;
            }

            if (anotherSensor.covers(x, y)) {
                return false;
            }
        }

        return true;
    }
}

class Sensor {
    public int x;
    public int y;
    public int r;

    public Beacon beacon;

    public Sensor(int x, int y, Beacon beacon) {
        this.x = x;
        this.y = y;
        this.beacon = beacon;
        this.r = getDistanceTo(beacon.x, beacon.y);
    }

    public boolean covers(int x, int y) {
        return getDistanceTo(x, y) <= this.r;
    }

    private int getDistanceTo(int x, int y) {
        return Math.abs(this.x - x) + Math.abs(this.y - y);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", beacon=" + beacon +
                '}';
    }
}

class Beacon {
    public int x;
    public int y;

    public Beacon(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Beacon{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
