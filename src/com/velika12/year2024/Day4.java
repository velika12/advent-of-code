package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day4 {

    private static final List<List<Character>> wordSearch = new ArrayList<>();
    private static final Set<Point> recentXmasPoints = new LinkedHashSet<>();
    private static final Set<Point> xmasPoints = new LinkedHashSet<>();
    private static final Set<Point> x_masPoints = new LinkedHashSet<>();

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day4.txt"))) {

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                List<Character> line = str.chars().mapToObj( c -> (char) c).toList();
                wordSearch.add(line);
            }

            countAllXmas();
            countAllX_mas();
        }
    }

    private static void countAllXmas() {
        int count = 0;

        for (int y = 0; y < wordSearch.size(); y++) {
            for (int x = 0; x < wordSearch.get(0).size(); x++) {
                if (wordSearch.get(y).get(x).equals('X')) {
                    for (Direction direction : Direction.values()) {
                        count += searchXmas(x, y, 'X', direction);
                    }
                }
            }
        }

        printWordSearch();

        System.out.println("Xmas count: " + count);
    }

    private static void countAllX_mas() {
        int count = 0;

        for (int y = 0; y < wordSearch.size(); y++) {
            for (int x = 0; x < wordSearch.get(0).size(); x++) {
                if (wordSearch.get(y).get(x).equals('M')) {
                    count += searchX_mas(x, y);
                }
            }
        }

        printWordSearch();

        System.out.println("X-mas count: " + count);
    }

    private static int searchX_mas(int x, int y) {
        int xCount = 0;
        int halfCount = searchXmas(x, y, 'M', Direction.DOWN_RIGHT);
        if (halfCount == 1) {
            if (wordSearch.get(y).get(x+2).equals('M')) {
                halfCount = searchXmas(x+2, y, 'M', Direction.DOWN_LEFT);
                if (halfCount == 1) {
                    x_masPoints.addAll(recentXmasPoints);
                    xCount++;
                }
            }
            recentXmasPoints.clear();
        }

        halfCount = searchXmas(x, y, 'M', Direction.DOWN_LEFT);
        if (halfCount == 1) {
            if (wordSearch.get(y+2).get(x).equals('M')) {
                halfCount = searchXmas(x, y+2, 'M', Direction.UP_LEFT);
                if (halfCount == 1) {
                    x_masPoints.addAll(recentXmasPoints);
                    xCount++;
                }
            }
            recentXmasPoints.clear();
        }

        halfCount = searchXmas(x, y, 'M', Direction.UP_LEFT);
        if (halfCount == 1) {
            if (wordSearch.get(y).get(x-2).equals('M')) {
                halfCount = searchXmas(x-2, y, 'M', Direction.UP_RIGHT);
                if (halfCount == 1) {
                    x_masPoints.addAll(recentXmasPoints);
                    xCount++;
                }
            }
            recentXmasPoints.clear();
        }

        halfCount = searchXmas(x, y, 'M', Direction.UP_RIGHT);
        if (halfCount == 1) {
            if (wordSearch.get(y-2).get(x).equals('M')) {
                halfCount = searchXmas(x, y-2, 'M', Direction.DOWN_RIGHT);
                if (halfCount == 1) {
                    x_masPoints.addAll(recentXmasPoints);
                    xCount++;
                }
            }
            recentXmasPoints.clear();
        }

        return xCount;
    }

    private static int searchXmas(int x, int y, char current, Direction direction) {
        int count = 0;
        Point point = new Point(x, y, current);

        Character next = detectNext(current);
        if (next == null) {
            xmasPoints.add(point);
            recentXmasPoints.add(point);
            return 1;
        }

        if (y > 0 && direction == Direction.UP) {
            if (wordSearch.get(y-1).get(x).equals(next)) {
                count += searchXmas(x, y-1, next, direction);
            }
        }
        if (y > 0 && x < wordSearch.get(0).size() - 1 && direction == Direction.UP_RIGHT) {
            if (wordSearch.get(y-1).get(x+1).equals(next)) {
                count = searchXmas(x+1, y-1, next, direction);
            }
        }
        if (x < wordSearch.get(0).size() - 1 && direction == Direction.RIGHT) {
            if (wordSearch.get(y).get(x+1).equals(next)) {
                count = searchXmas(x+1, y, next, direction);
            }
        }
        if (x < wordSearch.get(0).size() - 1 && y < wordSearch.size() - 1 && direction == Direction.DOWN_RIGHT) {
            if (wordSearch.get(y+1).get(x+1).equals(next)) {
                count = searchXmas(x+1, y+1, next, direction);
            }
        }
        if (y < wordSearch.size() - 1 && direction == Direction.DOWN) {
            if (wordSearch.get(y+1).get(x).equals(next)) {
                count = searchXmas(x, y+1, next, direction);
            }
        }
        if (y < wordSearch.size() - 1 && x > 0 && direction == Direction.DOWN_LEFT) {
            if (wordSearch.get(y+1).get(x-1).equals(next)) {
                count = searchXmas(x-1, y+1, next, direction);
            }
        }
        if (x > 0 && direction == Direction.LEFT) {
            if (wordSearch.get(y).get(x-1).equals(next)) {
                count = searchXmas(x-1, y, next, direction);
            }
        }
        if (x > 0 && y > 0 && direction == Direction.UP_LEFT) {
            if (wordSearch.get(y-1).get(x-1).equals(next)) {
                count = searchXmas(x-1, y-1, next, direction);
            }
        }

        if (count > 0) {
            xmasPoints.add(point);
            recentXmasPoints.add(point);
        }

        return count;
    }

    private static Character detectNext(char current) {
        return switch (current) {
            case 'X' -> 'M';
            case 'M' -> 'A';
            case 'A' -> 'S';
            default -> null;
        };
    }

    private static void printWordSearch() {
        System.out.println("---");
        for (int y = 0; y < wordSearch.size(); y++) {
            for (int x = 0; x < wordSearch.get(0).size(); x++) {
                char c = wordSearch.get(y).get(x);
                Point point = new Point(x, y, c);
                if (x_masPoints.contains(point)) {
                    System.out.print(green(c));
                } else if (xmasPoints.contains(point)) {
                    System.out.print(red(c));
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
        System.out.println("---");
    }

    private static String red(char c) {
        return "\u001B[31m" + c + "\u001B[0m";
    }

    private static String green(char c) {
        return "\u001B[32m" + c + "\u001B[0m";
    }

    private record Point(int x, int y, char c) {}

    private enum Direction {
        UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT;
    }
}
